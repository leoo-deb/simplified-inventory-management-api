package com.leo.estoque_api.exceptions.handle;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.EntityNotFoundException;
import com.leo.estoque_api.exceptions.StorageContentTypeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    private static final String MSG_GENERIC_ERROR = "An unexpected system error occurred. Please try again, " +
            "and if the problem persists, contact a system administrator.";

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        TypeError type = TypeError.INVALID_BODY;
        String requestPath = request.getDescription(false).replace("uri=", "");

        ErrorResponse errorResponse = createErrorResponse(
                (HttpStatus) status,
                type,
                requestPath,
                ex.getMessage()
        ).build();

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ex.getCause();
        String requestPath = request.getDescription(false).replace("uri=", "");

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return  handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        TypeError type = TypeError.INVALID_BODY;
        String message = "The request body is invalid. Check the syntax.";

        ErrorResponse errorResponse = createErrorResponse((HttpStatus) status, type, requestPath, message).build();
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String cause = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
        String requestPath = request.getDescription(false).replace("uri=", "");

        TypeError type = TypeError.INVALID_BODY;
        String message = String.format("The property '%s' does not exist. Correct or remove " +
                "the property and try again.", cause);

        ErrorResponse errorResponse = createErrorResponse((HttpStatus) status, type, requestPath, message).build();
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                HttpStatusCode status, WebRequest request) {
        String cause = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
        String requestPath = request.getDescription(false).replace("uri=", "");

        TypeError type = TypeError.INVALID_BODY;
        String message = String.format(
                "The property '%s' received the value '%s', which is invalid. Please correct it and provide a " +
                        "value compatible with the type %s.",
                cause,
                ex.getValue(),
                ex.getTargetType().getSimpleName()
        );

        ErrorResponse errorResponse = createErrorResponse((HttpStatus) status, type, requestPath, message).build();
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String messageError = "One or more fields are invalid. Please fill them out correctly and try again.";
        TypeError type = TypeError.INVALID_DATA;
        String requestPath = request.getDescription(false).replace("uri=", "");

        List<ErrorResponse.Field> fields = ex.getFieldErrors().stream()
                .map(fieldError -> {
                    String message = fieldError.getDefaultMessage();

                    return ErrorResponse.Field.builder()
                            .field(fieldError.getField())
                            .message(message)
                            .build();
                })
                .toList();

        ErrorResponse errorResponse = createErrorResponse((HttpStatus) status, type, requestPath, messageError)
                .fields(fields).build();

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleBusinessRuleException(Exception ex, WebRequest request) {
        TypeError type = TypeError.SYSTEM_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String requestPath = request.getDescription(false).replace("uri=", "");

        ErrorResponse errorResponse = createErrorResponse(status, type, requestPath, MSG_GENERIC_ERROR).build();
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(StorageContentTypeException.class)
    public ResponseEntity<Object> handleStorageContentTypeException(StorageContentTypeException ex, WebRequest request) {
        TypeError type = TypeError.INVALID_BODY;
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        String requestPath = request.getDescription(false).replace("uri=", "");

        ErrorResponse errorResponse = createErrorResponse(
                status,
                type,
                requestPath,
                ex.getMessage()
        ).build();

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handleBusinessRuleException(BusinessRuleException ex, WebRequest request) {
        TypeError type = TypeError.BUSINESS_ROLE_VIOLATION;
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String requestPath = request.getDescription(false).replace("uri=", "");

        ErrorResponse errorResponse = createErrorResponse(status, type, requestPath, message).build();

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        TypeError type = TypeError.ENTITY_NOT_FOUND;
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        String requestPath = request.getDescription(false).replace("uri=", "");

        ErrorResponse errorResponse = createErrorResponse(status, type, requestPath, message).build();

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        String requestPath = request.getDescription(false).replace("uri=", "");

        if (body == null) {
            body = createErrorResponse(
                    (HttpStatus) statusCode,
                    TypeError.SYSTEM_ERROR,
                    requestPath,
                    HttpStatus.valueOf(statusCode.value()).getReasonPhrase()
            ).build();
        } else if (body instanceof String) {
            body = createErrorResponse(
                    (HttpStatus) statusCode,
                    TypeError.SYSTEM_ERROR,
                    requestPath,
                    (String) body
            ).build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    public ErrorResponse.ErrorResponseBuilder createErrorResponse(HttpStatus status, TypeError type, String path, String message) {
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .path(path)
                .status(status.value())
                .error(type.getType())
                .message(message);
    }

}
