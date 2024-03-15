package edu.java.scrapper.model;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Link {
    private Long id;
    private URI url;
    private OffsetDateTime lastUpdated;
}
