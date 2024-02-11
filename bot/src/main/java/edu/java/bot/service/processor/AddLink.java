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
        String response;
        if (link.equals("Отмена")) {
            response = "Операция отменена";
        } else if (user.addLink(link)) {
            response = "Ссылка добавлена в отслеживание";
        } else {
            response = "Вы уже отслеживаете эту ссылку";
        }
        user.setState(User.State.WAITING_FOR_COMMAND);
        return response;
    }
}
