package edu.java.bot.service.processor;

import java.util.HashSet;
import edu.java.bot.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RemoveLinkTest {
    @Test
    void linkDoesNotGetRemovedIfUserDoesNotTrackTheLink() {
        RemoveLink removeLink = new RemoveLink();
        User user = new User();
        user.setState(User.State.WAITING_FOR_LINK_TO_REMOVE);
        user.setLinks(new HashSet<>());
        user.addLink("google.com");

        String response = removeLink.process("youtube.com", user);

        assertThat(response).isEqualTo("Вы не отслеживаете эту ссылку. "
                                       + "Попробуйте ещё раз или введите \"Отмена\", чтобы ничего не удалять");
        assertThat(user.getLinks()).containsExactly("google.com");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_LINK_TO_REMOVE);
    }

    @Test
    void operationGetsCancelledOnCancelMessage() {
        RemoveLink removeLink = new RemoveLink();
        User user = new User();
        user.setState(User.State.WAITING_FOR_LINK_TO_REMOVE);

        String response = removeLink.process("Отмена", user);

        assertThat(response).isEqualTo("Операция отменена");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }

    @Test
    void linkGetsRemovedIfUserTracksIt() {
        RemoveLink removeLink = new RemoveLink();
        User user = new User();
        user.setState(User.State.WAITING_FOR_LINK_TO_REMOVE);
        user.setLinks(new HashSet<>());
        user.addLink("google.com");

        String response = removeLink.process("google.com", user);

        assertThat(user.getLinks()).isEmpty();
        assertThat(response).isEqualTo("Ссылка удалена из отслеживания");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }
}
