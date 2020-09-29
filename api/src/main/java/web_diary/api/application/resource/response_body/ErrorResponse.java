package web_diary.api.application.resource.response_body;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ErrorResponse {
  @JsonProperty("error")
  private Error error;

  public ErrorResponse(String message, String detail) {
    this.error = new Error(message, detail);
  }

  @Value
  private class Error {
    @JsonProperty("message")
    private final String message;

    @JsonProperty("detail")
    private final String detail;
  }
}
