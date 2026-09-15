package com.leo.estoque_api.exceptions.handle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
public class ErrorResponse {

    @JsonFormat(pattern = "YYYY-MM-dd'T'HH-mm-ss")
    private OffsetDateTime timestamp;
    private String path;
    private Integer status;
    private String error;
    private String message;
    private List<Field> fields;

    @Getter
    @Setter
    @Builder
    public static class Field {

        private String field;
        private String message;

    }
}
