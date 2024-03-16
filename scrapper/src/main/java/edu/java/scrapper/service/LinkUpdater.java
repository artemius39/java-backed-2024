package edu.java.scrapper.service;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface LinkUpdater {
    // return update message if update occurred otherwise return empty optional
    Optional<String> update(URI url, OffsetDateTime lastUpdatedAt);

    boolean supports(URI url);
}
