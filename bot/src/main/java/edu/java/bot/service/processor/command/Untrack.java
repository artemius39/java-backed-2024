package edu.java.bot.service.processor.command;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class Untrack implements CommandProcessor {
    private final List list;

    public Untrack(List list) {
        this.list = list;
    }

    @Override
    public String process(User user) {
        assert user.getState() == User.State.WAITING_FOR_COMMAND;
        if (user.getLinks().isEmpty()) {
            return "Вы не отслеживаете никакие сайты";
        }
        user.setState(User.State.WAITING_FOR_LINK_TO_REMOVE);
        return "Введите ссылку, которую вы хотите прекратить отслеживать или \"Отмена\", чтобы отменить операцию."
               + " Список отслеживаемых сайтов:\n" + list.process(user);
    }
}
