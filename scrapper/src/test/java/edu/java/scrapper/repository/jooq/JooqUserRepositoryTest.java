package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.UserRepository;
import java.time.OffsetDateTime;
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
class JooqUserRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private DSLContext dslContext;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        UserRepository userRepository = new JooqUserRepository(dslContext);
        User user = new User(1L);

        userRepository.add(user);
        User selectedUser = dslContext.select(USER.ID, USER.CREATED_AT)
            .from(USER)
            .where(USER.ID.eq(1L))
            .fetchAnyInto(User.class);

        assertThat(selectedUser).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        UserRepository userRepository = new JooqUserRepository(dslContext);
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
            .set(USER_LINK.USER_ID, 1L)
            .set(USER_LINK.LINK_ID, 1L)
            .execute();
        User user = dslContext.select(USER.ID, USER.CREATED_AT)
            .from(USER)
            .where(USER.ID.eq(1L))
            .fetchAnyInto(User.class);

        userRepository.remove(user);
        int userCount = dslContext.select()
            .from(USER)
            .where(USER.ID.eq(1L))
            .execute();
        int linkCount = dslContext.select()
            .from(LINK)
            .where(LINK.ID.eq(1L))
            .execute();
        int userLinkCount = dslContext.select()
            .from(USER_LINK)
            .where(USER_LINK.USER_ID.eq(1L))
            .execute();

        assertThat(userCount).isZero();
        assertThat(linkCount).isZero();
        assertThat(userLinkCount).isZero();
    }
}
