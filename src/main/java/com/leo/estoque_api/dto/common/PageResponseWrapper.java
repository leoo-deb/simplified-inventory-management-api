package com.leo.estoque_api.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Wrapper padrão para respostas paginadas")
public record PageResponseWrapper<T>(

        @Schema(description = "Lista de itens da página atual")
        List<T> content,

        @Schema(description = "Número da página atual",
                example = "5")
        Integer page,

        @Schema(description = "Número total de elementos",
                example = "50")
        Long totalElement,

        @Schema(description = "Número total de páginas",
                example = "10")
        Integer totalPages,

        @Schema(description = "Indica se é a primeira página",
                example = "true")
        Boolean first,

        @Schema(description = "Indica se é a última página",
                example = "false")
        Boolean last
) {
    public PageResponseWrapper(Page<T> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}