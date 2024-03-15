package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.LinkRepository;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.model.jooq.Tables.LINK;
import static edu.java.scrapper.model.jooq.Tables.USER;
import static edu.java.scrapper.model.jooq.Tables.USER_LINK;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JooqLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private DSLContext dslContext;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        LinkRepository linkRepository = new JooqLinkRepository(dslContext);
        Link link = new Link();
        link.setUrl(URI.create("example.com"));
        User user = new User();
        user.setId(1L);
        dslContext.insertInto(USER)
            .set(USER.ID, 1L)
            .set(USER.CREATED_AT, OffsetDateTime.now())
            .execute();

        linkRepository.add(link, user);
        int linkCount = dslContext.selectCount()
            .from(LINK)
            .where(LINK.ID.eq(1L))
            .execute();
        int userLinkCount = dslContext.selectCount()
            .from(USER_LINK)
            .where(USER_LINK.LINK_ID.eq(1L).and(USER_LINK.USER_ID.eq(1L)))
            .execute();

        assertThat(linkCount).isOne();
        assertThat(userLinkCount).isOne();
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        LinkRepository linkRepository = new JooqLinkRepository(dslContext);
        Link link = new Link();
        link.setId(1L);
        link.setUrl(URI.create("example.com"));
        User user = new User();
        user.setId(1L);

        dslContext.insertInto(USER)
            .set(USER.ID, 1L)
            .set(USER.CREATED_AT, OffsetDateTime.now())
            .execute();
        dslContext.insertInto(LINK)
            .set(LINK.ID, 1L)
            .set(LINK.URL, "example.com")
            .set(LINK.LAST_UPDATED, OffsetDateTime.now())
            .execute();
        dslContext.insertInto(USER_LINK)
            .set(USER_LINK.LINK_ID, 1L)
            .set(USER_LINK.USER_ID, 1L)
            .execute();

        linkRepository.remove(link, user);

        int linkCount = dslContext.select()
            .from(LINK)
            .where(LINK.ID.eq(1L))
            .execute();
        int userLinkCount = dslContext.select()
            .from(USER_LINK)
            .where(USER_LINK.LINK_ID.eq(1L)
                .and(USER_LINK.USER_ID.eq(1L)))
            .execute();

        assertThat(linkCount).isZero();
        assertThat(userLinkCount).isZero();
    }

    @Test
    @Transactional
    @Rollback
    void findByUserIdTest() {
        LinkRepository linkRepository = new JooqLinkRepository(dslContext);
        OffsetDateTime now = OffsetDateTime.now();

        dslContext.insertInto(USER)
            .set(USER.ID, 1L)
            .set(USER.CREATED_AT, now)
            .newRecord()
            .set(USER.ID, 2L)
            .set(USER.CREATED_AT, now)
            .execute();
        dslContext.insertInto(LINK)
            .set(LINK.ID, 1L)
            .set(LINK.LAST_UPDATED, now)
            .set(LINK.URL, "example.com")
            .newRecord()
            .set(LINK.ID, 2L)
            .set(LINK.LAST_UPDATED, now)
            .set(LINK.URL, "example.org")
            .newRecord()
            .set(LINK.ID, 3L)
            .set(LINK.LAST_UPDATED, now)
            .set(LINK.URL, "example.example")
            .execute();
        dslContext.insertInto(USER_LINK)
            .set(USER_LINK.USER_ID, 1L)
            .set(USER_LINK.LINK_ID, 1L)
            .newRecord()
            .set(USER_LINK.USER_ID, 1L)
            .set(USER_LINK.LINK_ID, 2L)
            .execute();

        Collection<Link> links = linkRepository.findByUserId(1L);

        assertThat(links).containsExactly(
            new Link(1L, URI.create("example.com"), now),
            new Link(2L, URI.create("example.org"), now)
        );
    }

    @Test
    @Transactional
    @Rollback
    void findByLastUpdateTimeTest() {
        LinkRepository linkRepository = new JooqLinkRepository(dslContext);
        OffsetDateTime tooRecently = OffsetDateTime.now();
        OffsetDateTime oldEnough = OffsetDateTime.now().minusYears(1);
        dslContext.insertInto(LINK)
            .set(LINK.ID, 1L)
            .set(LINK.URL, "example.com")
            .set(LINK.LAST_UPDATED, tooRecently)
            .newRecord()
            .set(LINK.ID, 2L)
            .set(LINK.URL, "example.org")
            .set(LINK.LAST_UPDATED, oldEnough)
            .execute();

        Collection<Link> links = linkRepository.findByLastUpdateTime(Duration.of(1, ChronoUnit.DAYS));

        assertThat(links).containsExactly(new Link(2L, URI.create("example.org"), oldEnough));
    }

    @Test
    @Transactional
    @Rollback
    void findByLinkIdTest() {
        LinkRepository linkRepository = new JooqLinkRepository(dslContext);
        OffsetDateTime now = OffsetDateTime.now();
        dslContext.insertInto(USER)
            .set(USER.ID, 1L)
            .set(USER.CREATED_AT, now)
            .newRecord()
            .set(USER.ID, 2L)
            .set(USER.CREATED_AT, now)
            .newRecord()
            .set(USER.ID, 3L)
            .set(USER.CREATED_AT, now)
            .execute();
        dslContext.insertInto(LINK)
            .set(LINK.ID, 1L)
            .set(LINK.URL, "example.com")
            .set(LINK.LAST_UPDATED, now)
            .execute();
        dslContext.insertInto(USER_LINK)
            .set(USER_LINK.USER_ID, 1L)
            .set(USER_LINK.LINK_ID, 1L)
            .newRecord()
            .set(USER_LINK.USER_ID, 2L)
            .set(USER_LINK.LINK_ID, 1L)
            .execute();

        List<Long> ids = linkRepository.findByLinkId(1L);

        assertThat(ids).containsExactlyInAnyOrder(1L, 2L);
    }
}
