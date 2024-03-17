package edu.java.scrapper.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.jdbc.JdbcUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcUserRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void insertTest() {
        UserRepository repository = new JdbcUserRepository(jdbcTemplate);
        User user = new User();
        user.setId(1L);
        user = repository.add(user);

        assertThat(user.getId()).isOne();
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        UserRepository repository = new JdbcUserRepository(jdbcTemplate);
        User user = new User();
        user.setId(1L);

        jdbcTemplate.update("insert into \"user\" (id, created_at) values (1, now())");
        jdbcTemplate.update("insert into link (id, url, last_updated) values (1, 'example.com', now())");
        jdbcTemplate.update("insert into user_link (user_id, link_id) values (1, 1)");
        repository.remove(user);

        Integer userCount = jdbcTemplate.queryForObject("select count(*) from \"user\" where id=1", Integer.class);
        Integer linkCount = jdbcTemplate.queryForObject("select count(*) from link where id=1", Integer.class);
        Integer userLinkCount =
            jdbcTemplate.queryForObject("select count(*) from user_link where user_id=1", Integer.class);

        assertThat(userCount).isZero();
        assertThat(linkCount).isZero();
        assertThat(userLinkCount).isZero();
    }
}
