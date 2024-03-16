package edu.java.scrapper.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        LinkRepository linkRepository = new JdbcLinkRepository(jdbcTemplate);
        Link link = new Link();
        link.setUrl(URI.create("example.com"));
        User user = new User();
        user.setId(1L);
        jdbcTemplate.update("insert into \"user\" (id, created_at) values (1, now())");

        link = linkRepository.add(link, user);
        Integer linkCount = jdbcTemplate.queryForObject(
            "select count(*) from link where id=?",
            Integer.class,
            link.getId()
        );
        Integer userLinkCount = jdbcTemplate.queryForObject(
            "select count(*) from user_link where user_id=1 and link_id=?",
            Integer.class,
            link.getId()
        );

        assertThat(linkCount).isOne();
        assertThat(userLinkCount).isOne();
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        LinkRepository linkRepository = new JdbcLinkRepository(jdbcTemplate);
        Link link = new Link();
        link.setId(1L);
        link.setUrl(URI.create("example.com"));
        User user = new User();
        user.setId(1L);

        jdbcTemplate.update("insert into \"user\" (id, created_at) values (1, now())");
        jdbcTemplate.update("insert into link (id, url, last_updated) values (1, 'example.com', now())");
        jdbcTemplate.update("insert into user_link (user_id, link_id) values (1, 1)");

        linkRepository.remove(link, user);

        Integer linkCount = jdbcTemplate.queryForObject(
            "select count(*) from link where id=?",
            Integer.class,
            link.getId()
        );
        Integer userLinkCount = jdbcTemplate.queryForObject(
            "select count(*) from user_link where user_id=1 and link_id=?",
            Integer.class,
            link.getId()
        );

        assertThat(linkCount).isZero();
        assertThat(userLinkCount).isZero();
    }
}
