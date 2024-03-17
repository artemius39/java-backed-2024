package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.exception.LinkNotTrackedException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.LinkRepository;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDateTime;
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
        Long id = findByUrl(link.getUrl());
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
        Long id = findByUrl(link.getUrl());
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
            (resultSet, i) -> toLink(resultSet),
            userId
        );
    }

    @Override
    public Collection<Link> findByLastUpdateTime(Duration timeSinceLastUpdate) {
        return jdbcTemplate.query(
            "select * from link where last_updated < ?",
            (resultSet, i) -> toLink(resultSet),
            LocalDateTime.now().minus(timeSinceLastUpdate)
        );
    }

    @Override
    public List<Long> findByLinkId(Long id) {
        return jdbcTemplate.queryForList("select user_id from user_link where link_id=?", Long.class, id);
    }

    private Link toLink(ResultSet resultSet) throws SQLException {
        return new Link(
            resultSet.getLong(1),
            URI.create(resultSet.getString(2)),
            resultSet.getTimestamp(3).toInstant().atOffset(ZoneOffset.UTC)
        );
    }

    private Long findByUrl(URI url) {
        List<Long> ids = jdbcTemplate.query(
            "select id from link where url=?",
            new Object[] {url},
            new int[] {Types.VARCHAR},
            (resultSet, rowNum) -> resultSet.getLong("id")
        );
        return ids.isEmpty() ? null : ids.getFirst();
    }
}
