package web_diary.api.domain.exception;

public class DataDuplicationException extends RuntimeException {
  public DataDuplicationException(String message) {
    super(message);
  }
}