package edu.java.scrapper.repository.jooq;

import edu.java.scrapper.exception.ChatNotFoundException;
import edu.java.scrapper.exception.LinkNotTrackedException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.LinkRepository;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.model.jooq.Tables.LINK;
import static edu.java.scrapper.model.jooq.Tables.USER;
import static edu.java.scrapper.model.jooq.Tables.USER_LINK;

@Repository
@AllArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Override
    @Transactional
    public Link add(Link link, User user) {
        Long id = findByUrl(link.getUrl());
        if (id == null) {
            id = dslContext.insertInto(LINK)
                .set(LINK.URL, link.getUrl().toString())
                .set(LINK.LAST_UPDATED, OffsetDateTime.now())
                .returningResult(LINK.ID)
                .fetchAnyInto(Long.class);
        }
        link.setId(id);

        int count = dslContext.selectCount()
            .from(USER)
            .where(USER.ID.eq(user.getId()))
            .execute();
        if (count == 0) {
            throw new ChatNotFoundException(user.getId());
        }

        dslContext.insertInto(USER_LINK)
            .set(USER_LINK.USER_ID, user.getId())
            .set(USER_LINK.LINK_ID, link.getId())
            .execute();

        return link;
    }

    @Override
    @Transactional
    public Link remove(Link link, User user) {
        Long id = findByUrl(link.getUrl());
        if (id == null) {
            throw new LinkNotTrackedException(link.getUrl().toString());
        }
        link.setId(id);
        int deleted = dslContext.deleteFrom(USER_LINK)
            .where(USER_LINK.LINK_ID.eq(link.getId())
                .and(USER_LINK.USER_ID.eq(user.getId())))
            .execute();
        if (deleted == 0) {
            throw new LinkNotTrackedException(link.getUrl().toString());
        }
        int count = dslContext.select()
            .from(USER_LINK)
            .where(USER_LINK.LINK_ID.eq(link.getId()))
            .execute();
        if (count == 0) {
            dslContext.deleteFrom(LINK)
                .where(LINK.ID.eq(link.getId()))
                .execute();
        }
        return link;
    }

    @Override
    public Collection<Link> findByUserId(Long userId) {
        return dslContext.select()
            .from(LINK)
            .join(USER_LINK)
            .on(LINK.ID.eq(USER_LINK.LINK_ID))
            .where(USER_LINK.USER_ID.eq(userId))
            .fetchInto(Link.class);
    }

    @Override
    public Collection<Link> findByLastUpdateTime(Duration timeSinceLastUpdate) {
        return dslContext.select()
            .from(LINK)
            .where(LINK.LAST_UPDATED.lt(OffsetDateTime.now().minus(timeSinceLastUpdate)))
            .fetchInto(Link.class);
    }

    @Override
    public List<Long> findByLinkId(Long id) {
        return dslContext.select(USER.ID)
            .from(USER)
            .join(USER_LINK)
            .on(USER.ID.eq(USER_LINK.USER_ID))
            .where(USER_LINK.LINK_ID.eq(id))
            .fetchInto(Long.class);
    }

    private Long findByUrl(URI url) {
        return dslContext.select(LINK.ID)
            .from(LINK)
            .where(LINK.URL.eq(url.toString()))
            .fetchAnyInto(Long.class);
    }
}
