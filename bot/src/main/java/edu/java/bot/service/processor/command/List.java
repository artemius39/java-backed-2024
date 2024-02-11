package edu.java.bot.service.processor.command;

import java.util.Set;
import edu.java.bot.model.User;
import org.springframework.stereotype.Service;

@Service
public class List implements CommandProcessor {
    @Override
    public String process(User user) {
        assert user.getState() == User.State.WAITING_FOR_COMMAND;
        Set<String> links = user.getLinks();
        return links.isEmpty() ? "Вы не отслеживаете никакие сайты" : String.join("\n", links);
    }
}
