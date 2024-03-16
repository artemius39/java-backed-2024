package edu.java.scrapper.model;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Link {
    private Long id;
    private URI url;
    private OffsetDateTime lastUpdated;
}
