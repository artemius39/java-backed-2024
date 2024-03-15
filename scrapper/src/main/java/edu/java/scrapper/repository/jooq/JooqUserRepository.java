package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.UserRepository;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.model.jooq.Tables.LINK;
import static edu.java.scrapper.model.jooq.Tables.USER;
import static edu.java.scrapper.model.jooq.Tables.USER_LINK;

@Repository
@AllArgsConstructor
public class JooqUserRepository implements UserRepository {
    private final DSLContext dslContext;

    @Override
    public User add(User user) {
        OffsetDateTime now = OffsetDateTime.now();
        dslContext.insertInto(USER)
            .set(USER.ID, user.getId())
            .set(USER.CREATED_AT, now)
            .execute();
        user.setCreatedAt(now);
        return user;
    }

    @Override
    @Transactional
    public void remove(User user) {
        dslContext.deleteFrom(USER_LINK)
            .where(USER_LINK.USER_ID.eq(user.getId()))
            .execute();
        dslContext.deleteFrom(LINK)
            .where(LINK.ID.notIn(
                dslContext.selectDistinct(USER_LINK.LINK_ID)
                    .from(USER_LINK)
                    .where(USER_LINK.LINK_ID.eq(LINK.ID))
            ))
            .execute();
        dslContext.deleteFrom(USER)
            .where(USER.ID.eq(user.getId()))
            .execute();
    }
}
