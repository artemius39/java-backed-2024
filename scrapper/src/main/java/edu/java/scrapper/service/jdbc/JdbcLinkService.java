package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.dto.bot.LinkResponse;
import edu.java.scrapper.dto.bot.ListLinksResponse;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;

    @Override
    public LinkResponse add(long chatId, URI url) {
        return toDto(linkRepository.add(new Link(url), new User(chatId)));
    }

    @Override
    public LinkResponse remove(long chatId, URI url) {
        return toDto(linkRepository.remove(new Link(url), new User(chatId)));
    }

    @Override
    public ListLinksResponse listAll(long chatId) {
        return new ListLinksResponse(linkRepository.findByUserId(chatId)
            .stream()
            .map(this::toDto)
            .toList());
    }

    private LinkResponse toDto(Link link) {
        return new LinkResponse(link.getId(), link.getUrl());
    }
}
