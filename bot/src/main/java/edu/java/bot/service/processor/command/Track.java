package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Track extends BaseCommand {
    @Override
    public String name() {
        return "/track";
    }

    @Override
    public String description() {
        return "добавить сайт в отслеживание";
    }

    @Override
    protected String processImpl(User user) {
        user.setState(User.State.WAITING_FOR_LINK_TO_ADD);
        return "Введите ссылку на сайт, который вы хотите отслеживать "
               + "или \"Отмена\" для того, чтобы ничего не добавлять";
    }
}
