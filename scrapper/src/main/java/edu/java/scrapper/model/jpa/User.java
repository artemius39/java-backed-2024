package edu.java.scrapper.model.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.Generated;

@Entity
@Table(name = "`user`")
@Data
public class User {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", nullable = false)
    @Generated(sql = "now()")
    private OffsetDateTime createdAt;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
        name = "user_link",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private Set<Link> links = new HashSet<>();
}
