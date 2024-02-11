package edu.java.bot.service.processor;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class AddLink extends BaseMessageProcessor {
    @Override
    protected User.State supportedState() {
        return User.State.WAITING_FOR_LINK_TO_ADD;
    }

    @Override
    protected String processImpl(String link, User user) {
        user.setState(User.State.WAITING_FOR_COMMAND);
        if (link.equals("Отмена")) {
            return "Операция отменена";
        }
        if (user.addLink(link)) {
            return "Ссылка добавлена в отслеживание";
        } else {
            return "Вы уже отслеживаете эту ссылку";
        }
    }
}
