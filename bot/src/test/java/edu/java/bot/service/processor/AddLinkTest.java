package edu.java.bot.service.processor;

import edu.java.bot.model.User;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AddLinkTest {
    @Test
    void operationGetsCancelledOnCancelMessage() {
        AddLink addLink = new AddLink();
        User user = new User();
        user.setState(User.State.WAITING_FOR_LINK_TO_ADD);

        String response = addLink.process("Отмена", user);

        assertThat(response).isEqualTo("Операция отменена");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }

    @Test
    void linkGetsAddedIfUserEntersLink() {
        AddLink addLink = new AddLink();
        User user = new User();
        user.setState(User.State.WAITING_FOR_LINK_TO_ADD);
        user.setLinks(new HashSet<>());

        String response = addLink.process("google.com", user);

        assertThat(user.getLinks()).contains("google.com");
        assertThat(response).isEqualTo("Ссылка добавлена в отслеживание");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }

    @Test
    void specialMessageIsPrintedIfUserAlreadyTracksLinkEntered() {
        AddLink addLink = new AddLink();
        User user = new User();
        user.setState(User.State.WAITING_FOR_LINK_TO_ADD);
        user.setLinks(new HashSet<>());
        user.addLink("google.com");

        String response = addLink.process("google.com", user);

        assertThat(response).isEqualTo("Вы уже отслеживаете эту ссылку");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
        assertThat(user.getLinks()).contains("google.com");
    }
}
