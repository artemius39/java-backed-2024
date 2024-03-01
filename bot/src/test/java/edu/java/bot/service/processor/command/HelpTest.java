package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;

class HelpTest {
    @Test
    void helpPrintsCompleteListOfCommands() {
        Command command1 = mock(Command.class);
        when(command1.name()).thenReturn("/cmd1");
        when(command1.description()).thenReturn("desc1");
        Command command2 = mock(Command.class);
        when(command2.name()).thenReturn("/cmd2");
        when(command2.description()).thenReturn("desc2");
        Help help = new Help(List.of(command1, command2));
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        String response = help.process(user);

        assertThat(response).isEqualTo("""
                Доступные команды:
                /help -- список всех команд
                /cmd1 -- desc1
                /cmd2 -- desc2""");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }
}
