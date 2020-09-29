package web_diary.api.domain.exception;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class InvalidJsonValueException extends RuntimeException{

  public InvalidJsonValueException(String message) {
    super(message);
  }

  public static InvalidJsonValueException getInstance(BindingResult result) {
    List<FieldError> errors = result.getFieldErrors();
    StringBuilder error_detail = new StringBuilder();

    errors.forEach(
      error -> error_detail
        .append(error.getField())
        .append(": ")
        .append(error.getDefaultMessage())
        .append(";")
    );

    return new InvalidJsonValueException(error_detail.toString());
  }
}
