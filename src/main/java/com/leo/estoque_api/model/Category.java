package com.leo.estoque_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_categories")
@Data
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    @Column(nullable = false, length = 30)
    private String name;

    private Boolean active;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    public boolean isActive() {
        return active;
    }

}
