package br.com.paulojof.controller;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.paulojof.configuration.MessageConfig;
import br.com.paulojof.exception.http.RestException;
import br.com.paulojof.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private static final Integer JVM_MAX_STRING_LEN = 2147483647;

    private final MessageConfig messageConfig;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(List.of(buildErrorResponse("400.001", exception.getName())));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<List<ErrorResponse>> assertionException(HttpMessageNotReadableException exception) {
        if (exception.getCause() instanceof JsonMappingException cause) {
            String field = cause.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("."));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(List.of(buildErrorResponse("400.001", field)));
        }
        return defaultBadRequestError();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ErrorResponse> messageErrors = Optional.ofNullable(exception)
                .map(MethodArgumentNotValidException::getBindingResult)
                .filter(ObjectUtils::isNotEmpty)
                .map(BindingResult::getAllErrors)
                .filter(ObjectUtils::isNotEmpty).stream()
                .flatMap(Collection::stream)
                .filter(ObjectUtils::isNotEmpty)
                .map(this::createError)
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messageErrors);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Object> handleRestException(RestException restException) {
        Object body = null;
        if (restException.getResponseBodyCode() != null) {
            body = buildErrorResponse(restException.getResponseBodyCode());
        }

        if (restException.getResponseBody() != null) {
            ErrorResponse errorResponse = restException.getResponseBody();

            body = StringUtils.isBlank(errorResponse.getMessage())
                    ? buildErrorResponse(errorResponse.getCode())
                    : errorResponse;
        }

        return ResponseEntity
                .status(restException.getStatus())
                .body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException exception) {
        List<ErrorResponse> errors = exception.getConstraintViolations().stream()
                .map(constraint -> buildErrorResponse(constraint.getMessageTemplate(), ((PathImpl) constraint.getPropertyPath()).getLeafNode()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    private ErrorResponse buildErrorResponse(String code, Object... arguments) {
        return ErrorResponse.builder()
                .code(code)
                .message(getMessage(code, arguments))
                .build();
    }

    private ResponseEntity<List<ErrorResponse>> defaultBadRequestError() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(List.of(buildErrorResponse("400.000")));
    }

    private ErrorResponse createError(ObjectError error) {
        String code = error.getDefaultMessage();
        String field = "";
        if (error instanceof FieldError fieldError) {
            field = fieldError.getField();
        }

        if ("Size".equals(error.getCode())) {
            Integer min = null;
            Integer max = null;
            if (error.getArguments() != null && error.getArguments().length > 2) {
                Integer rawMax = (Integer) error.getArguments()[1];
                max = Objects.equals(rawMax, JVM_MAX_STRING_LEN) ? null : rawMax;

                Integer rawMin = (Integer) error.getArguments()[1];
                min = Objects.equals(rawMin, JVM_MAX_STRING_LEN) ? null : rawMin;

            }

            String message = "";
            if (ObjectUtils.allNotNull(min, max)) {
                message = getMessage(code, field, min, max);
            } else if (min != null) {
                message = getMessage(code, field, min);
            } else if (max != null) {
                message = getMessage(code, field, max);
            }

            return ErrorResponse.builder()
                    .code(code)
                    .message(message)
                    .build();
        }

        return buildErrorResponse(code, field);
    }

    private String getMessage(String code, Object... args) {
        return messageConfig.getMessage(code, args);
    }

}
