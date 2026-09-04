package com.leo.estoque_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_photos_variants")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Data
public class PhotoVariant {

    @Id
    private UUID id;

    @MapsId
    @OneToOne
    private ProductVariant productVariant;

    private String name;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Long size;

}
