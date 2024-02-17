package edu.java.bot.service.processor;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class RemoveLink extends BaseMessageProcessor {
    @Override
    public User.State supportedState() {
        return User.State.WAITING_FOR_LINK_TO_REMOVE;
    }

    @Override
    protected String processImpl(String link, User user) {
        if (link.equals("Отмена")) {
            user.setState(User.State.WAITING_FOR_COMMAND);
            return "Операция отменена";
        }
        if (user.removeLink(link)) {
            user.setState(User.State.WAITING_FOR_COMMAND);
            return "Ссылка удалена из отслеживания";
        } else {
            return "Вы не отслеживаете эту ссылку. Попробуйте ещё раз или введите \"Отмена\", чтобы ничего не удалять";
        }
    }
}
