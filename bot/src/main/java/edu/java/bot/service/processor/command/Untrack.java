package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Untrack extends BaseCommandProcessor {
    private final List list;

    public Untrack(List list) {
        this.list = list;
    }

    @Override
    protected String processImpl(User user) {
        if (user.getLinks().isEmpty()) {
            return "Вы не отслеживаете никакие сайты";
        }
        user.setState(User.State.WAITING_FOR_LINK_TO_REMOVE);
        return "Введите ссылку, которую вы хотите прекратить отслеживать или \"Отмена\", чтобы отменить операцию."
               + " Список отслеживаемых сайтов:\n" + list.process(user);
    }
}
