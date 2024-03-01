package edu.java.bot.service.processor;

import java.util.List;
import edu.java.bot.model.User;
import edu.java.bot.service.processor.command.Command;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProcessCommandTest {
    @Test
    void specialMessageIsPrintedOnUnrecognizedCommand() {
        Command command1 = mock(Command.class);
        when(command1.name()).thenReturn("/cmd1");
        Command command2 = mock(Command.class);
        when(command2.name()).thenReturn("/cmd2");
        Command command3 = mock(Command.class);
        when(command3.name()).thenReturn("/cmd3");
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);
        ProcessCommand processCommand = new ProcessCommand(List.of(command1, command2, command3));

        String response = processCommand.process("unsupported", user);

        assertThat(response).isEqualTo("Неизвестная команда. "
                                       + "Введите /help, чтобы увидеть список доступных команд");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
        verify(command1, times(0)).process(user);
        verify(command2, times(0)).process(user);
        verify(command3, times(0)).process(user);
    }

    @Test
    void commandGetsForwardedToCorrespondingProcessor() {
        Command command1 = mock(Command.class);
        when(command1.name()).thenReturn("/cmd1");
        Command command2 = mock(Command.class);
        when(command2.name()).thenReturn("/cmd2");
        Command command3 = mock(Command.class);
        when(command3.name()).thenReturn("/cmd3");
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);
        ProcessCommand processCommand = new ProcessCommand(List.of(command1, command2, command3));

        processCommand.process("/cmd1", user);

        verify(command1, times(1)).process(user);
        verify(command2, times(0)).process(user);
        verify(command3, times(0)).process(user);
    }
}
