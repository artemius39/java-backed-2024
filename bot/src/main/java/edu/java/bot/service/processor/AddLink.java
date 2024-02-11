package edu.java.bot.service.processor;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class AddLink implements MessageProcessor {
    @Override
    public String process(String link, User user) {
        assert user.getState() == User.State.WAITING_FOR_LINK_TO_ADD;
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
