package edu.java.bot.model;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private State state;
    private Set<String> links;

    public boolean removeLink(String link) {
        return links.remove(link);
    }

    public boolean addLink(String link) {
        return links.add(link);
    }

    public enum State {
        WAITING_FOR_COMMAND, WAITING_FOR_LINK_TO_ADD, WAITING_FOR_LINK_TO_REMOVE
    }
}
