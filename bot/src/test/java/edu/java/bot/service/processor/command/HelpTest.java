package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HelpTest {
    @Test
    void helpPrintCompleteListOfCommands() {
        Help help = new Help();
        User user = new User();
        user.setState(User.State.WAITING_FOR_COMMAND);

        String response = help.process(user);

        assertThat(response).isEqualTo("""
            Доступные команды:
            /start -- зарегистрироваться в системе
            /help -- список команд
            /track -- начать отслеживать сайт
            /untrack -- прекратить отслеживать сайт
            /list -- список отслеживаемых сайтов""");
        assertThat(user.getState()).isEqualTo(User.State.WAITING_FOR_COMMAND);
    }
}
