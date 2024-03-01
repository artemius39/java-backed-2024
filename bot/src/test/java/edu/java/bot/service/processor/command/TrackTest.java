package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TrackTest {
    @Test
    void trackSetsUserStateToWaitForLinkToAdd() {
        Track track = new Track();
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        String response = track.process(user);

        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_LINK_TO_ADD);
        assertThat(response).isEqualTo("Введите ссылку на сайт, который вы хотите отслеживать "
                                       + "или \"Отмена\" для того, чтобы ничего не добавлять");
    }
}
