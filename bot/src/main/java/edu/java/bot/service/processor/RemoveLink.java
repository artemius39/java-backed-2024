package edu.java.bot.service.processor;

import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class RemoveLink implements MessageProcessor {
    @Override
    public String process(String link, User user) {
        assert user.getState() == User.State.WAITING_FOR_LINK_TO_REMOVE;
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
