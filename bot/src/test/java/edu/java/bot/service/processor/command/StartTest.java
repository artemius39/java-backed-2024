package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class StartTest {
    @Test
    void startPrintsSuccessMessage() {
        Start start = new Start();
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        String response = start.process(user);

        assertThat(response).isEqualTo("Вы зарегистрированы");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }
}
