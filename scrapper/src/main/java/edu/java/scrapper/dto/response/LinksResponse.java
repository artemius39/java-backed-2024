package edu.java.scrapper.dto.response;

import java.util.List;

public record LinksResponse(List<LinkResponse> links, int size) {
    public LinksResponse(List<LinkResponse> links) {
        this(links, links.size());
    }
}
