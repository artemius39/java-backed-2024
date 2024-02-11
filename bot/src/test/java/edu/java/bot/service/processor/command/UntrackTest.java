package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UntrackTest {
    @Test
    void untrackDoesNotRequestLinkIfUserDoesNotTrackAnyLinks() {
        Untrack untrack = new Untrack();
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);
        user.setLinks(Set.of());

        String response = untrack.process(user);

        assertThat(response).isEqualTo("Вы не отслеживаете никакие сайты");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }

    @Test
    void untrackDoesRequestLinkIfUserDoesTrackLinks() {
        Untrack untrack = new Untrack();
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);
        user.setLinks(Set.of("google.com"));

        String response = untrack.process(user);

        assertThat(response).isEqualTo("Введите ссылку, которую вы хотите прекратить отслеживать "
                                       + "или \"Отмена\", чтобы отменить операцию. "
                                       + "Список отслеживаемых сайтов:\ngoogle.com");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_LINK_TO_REMOVE);
    }
}
