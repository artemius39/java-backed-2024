package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Track implements CommandProcessor {
    @Override
    public String process(User user) {
        assert user.getState() == User.State.WAITING_FOR_COMMAND;
        user.setState(User.State.WAITING_FOR_LINK_TO_ADD);
        return "Введите ссылку на сайт, который вы хотите отслеживать "
               + "или \"Отмена\" для того, чтобы ничего не добавлять";
    }
}
