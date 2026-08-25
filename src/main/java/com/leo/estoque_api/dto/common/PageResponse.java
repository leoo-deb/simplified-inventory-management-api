package com.leo.estoque_api.dto.common;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        Integer page,
        Long totalElement,
        Integer totalPages
) {
    public PageResponse(Page<T> page) {
        this(page.getContent(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
    }
}