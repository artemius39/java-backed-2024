package edu.java.scrapper.model;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    private Long id;
    private URI url;
    private OffsetDateTime lastUpdated;

    public Link(URI url) {
        this.url = url;
    }

}
