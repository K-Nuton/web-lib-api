package web_diary.api.domain.exception;

@SuppressWarnings("serial")
public class DataDuplicationException extends RuntimeException {
  public DataDuplicationException(String message) {
    super(message);
  }
}