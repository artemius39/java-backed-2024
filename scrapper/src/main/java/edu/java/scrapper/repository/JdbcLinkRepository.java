package edu.java.scrapper.repository;

import edu.java.scrapper.exception.LinkNotTrackedException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Link add(Link link, User user) {
        link.setId(insertIfNotExists(link));
        jdbcTemplate.update("insert into user_link (user_id, link_id) values (?, ?)", user.getId(), link.getId());
        return link;
    }

    // if link is already saved, return its id, otherwise, insert it and return auto generated id
    private Long insertIfNotExists(Link link) {
        Long id = find(link);
        if (id != null) {
            return id;
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into link (url, last_updated) values (?, now())",
                    Statement.RETURN_GENERATED_KEYS
                );
                preparedStatement.setString(1, link.getUrl().toString());
                return preparedStatement;
            },
            keyHolder
        );
        Map<String, Object> keys = keyHolder.getKeys();
        assert keys != null;
        return (Long) keys.get("id");
    }

    @Override
    public Link remove(Link link, User user) {
        Long id = find(link);
        if (id == null) {
            throw new LinkNotTrackedException(link.getUrl().toString());
        }

        link.setId(id);
        jdbcTemplate.update("delete from user_link where user_id=? and link_id=?", user.getId(), link.getId());
        Long count = jdbcTemplate.queryForObject(
            "select count(*) from user_link where link_id=?",
            Long.class,
            link.getId()
        );
        assert count != null;
        if (count == 0) {
            jdbcTemplate.update("delete from link where id=?", link.getId());
        }
        return link;
    }

    @Override
    public Collection<Link> findByUserId(Long userId) {
        return jdbcTemplate.query(
            "select * from link where id in (select link_id from user_link where user_id=?)",
            (resultSet, i) -> new Link(
                resultSet.getLong(1),
                URI.create(resultSet.getString(2)),
                resultSet.getTimestamp(3).toInstant().atOffset(ZoneOffset.UTC)
            ),
            userId
        );
    }

    private Long find(Link link) {
        List<Long> ids = jdbcTemplate.query(
            "select id from link where url=?",
            new Object[] {link.getUrl().toString()},
            new int[] {Types.VARCHAR},
            (resultSet, rowNum) -> resultSet.getLong("id")
        );
        return ids.isEmpty() ? null : ids.getFirst();
    }
}
