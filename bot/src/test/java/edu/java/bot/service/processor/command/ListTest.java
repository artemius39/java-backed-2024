package edu.java.bot.service.processor.command;

import java.util.Set;
import edu.java.bot.model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ListTest {
    @Test
    void listPrintsSpecialMessageIfUserHasNoLinks() {
        List list = new List();
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);
        user.setLinks(Set.of());

        String response = list.process(user);

        assertThat(response).isEqualTo("Вы не отслеживаете никакие сайты");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }

    @Test
    void listPrintsLinksIfUserHasLinks() {
        List list = new List();
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);
        user.setLinks(Set.of("google.com", "youtube.com"));

        String response = list.process(user);

        assertThat(response).isIn("google.com\nyoutube.com", "youtube.com\ngoogle.com");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }
}
