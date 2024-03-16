package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.JdbcLinkRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;

    @Override
    public Link add(long chatId, URI url) {
        return linkRepository.add(new Link(url), new User(chatId));
    }

    @Override
    public Link remove(long chatId, URI url) {
        return linkRepository.remove(new Link(url), new User(chatId));
    }

    @Override
    public Collection<Link> listAll(long chatId) {
        return linkRepository.findByUserId(chatId);
    }
}
