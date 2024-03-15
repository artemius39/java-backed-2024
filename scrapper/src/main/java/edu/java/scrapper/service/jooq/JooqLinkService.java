package edu.java.scrapper.service.jooq;

import edu.java.scrapper.dto.bot.LinkResponse;
import edu.java.scrapper.dto.bot.ListLinksResponse;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository linkRepository;

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
        return linkRepository.findByUserId(chatId)
            .stream()
            .map(this::toDto)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new ListLinksResponse(list, list.size())
            ));
    }

    private LinkResponse toDto(Link link) {
        return new LinkResponse(link.getId(), link.getUrl());
    }
}
