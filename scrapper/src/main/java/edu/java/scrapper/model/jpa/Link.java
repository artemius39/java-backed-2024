package edu.java.scrapper.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Entity
@Table(name = "link")
@Data
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "url", unique = true, updatable = false)
    private String url;

    @Column(name = "last_updated", unique = true, nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToMany(mappedBy = "links")
    private Set<User> users = new HashSet<>();
}
