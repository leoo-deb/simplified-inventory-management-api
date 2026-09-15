package com.leo.estoque_api.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Wrapper padrão para respostas da API")
public record ApiResponseWrapper<T>(
        @Schema(description = "Indica se a operação foi bem-sucedida",
                example = "true")
        Boolean success,

        @Schema(description = "Dados da resposta")
        T data,

        @Schema(description = "Mensagem descritiva",
                example = "Operação realizada com sucesso")
        String message,

        @Schema(description = "Timestamp da resposta",
                example = "2026-09-10T12:20:00")
        @JsonFormat(pattern = "YYYY-MM-dd'T'HH:mm:ss")
        OffsetDateTime timestamp
) {

    public static <T> ApiResponseWrapper<T> success(T data, String message) {
        return new ApiResponseWrapper<>(true, data, message, OffsetDateTime.now());
    }

    public static <T> ApiResponseWrapper<T> failed(String message) {
        return new ApiResponseWrapper<>(false, null, message, OffsetDateTime.now());
    }
}
