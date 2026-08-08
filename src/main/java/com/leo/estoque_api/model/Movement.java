package com.leo.estoque_api.model;

import com.leo.estoque_api.model.enums.TypeMovements;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tb_movements")
@Data
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "variant_id")
    private ProductVariant productVariant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMovements type;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long oldStock;

    @Column(nullable = false)
    private Long newStock;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(columnDefinition = "datetime", nullable = false)
    private OffsetDateTime dateTime;

    @Column(columnDefinition = "TEXT")
    private String description;

}
