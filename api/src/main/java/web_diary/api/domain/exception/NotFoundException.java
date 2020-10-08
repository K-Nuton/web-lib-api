package web_diary.api.domain.exception;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException{
  public NotFoundException(String message) {
    super(message);
  }
}
