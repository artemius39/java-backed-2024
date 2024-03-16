package edu.java.scrapper.repository;

import edu.java.scrapper.model.User;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
            connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"user\" (id, created_at) values (?, now())",
                    Statement.RETURN_GENERATED_KEYS
                );
                preparedStatement.setLong(1, user.getId());
                return preparedStatement;
            },
            keyHolder
        );

        Map<String, Object> keys = keyHolder.getKeys();
        assert keys != null;
        Timestamp timestamp = (Timestamp) keys.get("created_at");
        user.setCreatedAt(timestamp.toLocalDateTime().atOffset(ZoneOffset.UTC));
        return user;
    }

    @Override
    @Transactional
    public void remove(User user) {
        jdbcTemplate.update("delete from user_link where user_id=?", user.getId());
        jdbcTemplate.update("delete from link where id not in (select distinct link_id from user_link)");
        jdbcTemplate.update("delete from \"user\" where id=?", user.getId());
    }
}
