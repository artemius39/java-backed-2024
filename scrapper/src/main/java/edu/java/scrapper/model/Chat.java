package edu.java.scrapper.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Chat {
    private Long id;
    private OffsetDateTime createdAt;
}
