package edu.java.bot.service.processor;

import edu.java.bot.model.User;
import edu.java.bot.service.processor.command.Help;
import edu.java.bot.service.processor.command.List;
import edu.java.bot.service.processor.command.Start;
import edu.java.bot.service.processor.command.Track;
import edu.java.bot.service.processor.command.Untrack;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ProcessCommandTest {
    @Test
    void specialMessageIsPrintedOnUnrecognizedCommand() {
        Start start = mock(Start.class);
        Help help = mock(Help.class);
        List list = mock(List.class);
        Track track = mock(Track.class);
        Untrack untrack = mock(Untrack.class);
        ProcessCommand processCommand = new ProcessCommand(start, help, list, track, untrack);
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        String response = processCommand.process("unsupported", user);

        assertThat(response).isEqualTo("Неизвестная команда. "
                                       + "Введите /help, чтобы увидеть список доступных команд");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }

    @Test
    void startCommandGetsForwardedToStartProcessor() {
        Start start = mock(Start.class);
        Help help = mock(Help.class);
        List list = mock(List.class);
        Track track = mock(Track.class);
        Untrack untrack = mock(Untrack.class);
        ProcessCommand processCommand = new ProcessCommand(start, help, list, track, untrack);
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        processCommand.process("/start", user);

        verify(start, times(1)).process(user);
    }

    @Test
    void helpCommandGetsForwardedToHelpProcessor() {
        Start start = mock(Start.class);
        Help help = mock(Help.class);
        List list = mock(List.class);
        Track track = mock(Track.class);
        Untrack untrack = mock(Untrack.class);
        ProcessCommand processCommand = new ProcessCommand(start, help, list, track, untrack);
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        processCommand.process("/help", user);

        verify(help, times(1)).process(user);
    }

    @Test
    void listCommandGetsForwardedToListProcessor() {
        Start start = mock(Start.class);
        Help help = mock(Help.class);
        List list = mock(List.class);
        Track track = mock(Track.class);
        Untrack untrack = mock(Untrack.class);
        ProcessCommand processCommand = new ProcessCommand(start, help, list, track, untrack);
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        processCommand.process("/list", user);

        verify(list, times(1)).process(user);
    }

    @Test
    void trackCommandGetsForwardedToTrackProcessor() {
        Start start = mock(Start.class);
        Help help = mock(Help.class);
        List list = mock(List.class);
        Track track = mock(Track.class);
        Untrack untrack = mock(Untrack.class);
        ProcessCommand processCommand = new ProcessCommand(start, help, list, track, untrack);
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        processCommand.process("/track", user);

        verify(track, times(1)).process(user);
    }

    @Test
    void untrackCommandGetsForwardedToUntrackProcessor() {
        Start start = mock(Start.class);
        Help help = mock(Help.class);
        List list = mock(List.class);
        Track track = mock(Track.class);
        Untrack untrack = mock(Untrack.class);
        ProcessCommand processCommand = new ProcessCommand(start, help, list, track, untrack);
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        processCommand.process("/untrack", user);

        verify(untrack, times(1)).process(user);
    }
}
