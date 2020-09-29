package web_diary.api.application.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import web_diary.api.application.resource.response_body.ErrorResponse;
import web_diary.api.domain.exception.DataDuplicationException;
import web_diary.api.domain.exception.InvalidJsonValueException;
import web_diary.api.domain.exception.NotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    if (!(body instanceof ErrorResponse))
      body = new ErrorResponse(status.getReasonPhrase(), "");

    return new ResponseEntity<>(body, headers, status);
  }

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<Object> handle404(NotFoundException ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    ErrorResponse body = new ErrorResponse("Not Found", ex.getMessage());
    HttpStatus status = HttpStatus.NOT_FOUND;

    return this.handleExceptionInternal(ex, body, headers, status, request);
  }

  @ExceptionHandler({InvalidJsonValueException.class})
  public ResponseEntity<Object> handleInvalidJson(InvalidJsonValueException ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    ErrorResponse body = new ErrorResponse("Invalid Json", ex.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;

    return this.handleExceptionInternal(ex, body, headers, status, request);
  }

  @ExceptionHandler({DataDuplicationException.class})
  public ResponseEntity<Object> handleDublication(DataDuplicationException ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    ErrorResponse body = new ErrorResponse("Requested user_id or diary_id already exists.", ex.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;

    return this.handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request
  ) {
    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    StringBuilder error_detail = new StringBuilder();
    errors.forEach(
      error -> error_detail
        .append(error.getField())
        .append(": ")
        .append(error.getDefaultMessage())
        .append("; ")
    );

    ErrorResponse body = new ErrorResponse("Bad Request", error_detail.toString());

    return this.handleExceptionInternal(ex, body, headers, status, request);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handle500(Exception ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    ErrorResponse body = new ErrorResponse("Internal Server Error", "");
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    return this.handleExceptionInternal(ex, body, headers, status, request);
  }
}
