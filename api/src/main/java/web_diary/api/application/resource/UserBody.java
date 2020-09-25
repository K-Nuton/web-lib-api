package web_diary.api.application.resource;

import web_diary.api.domain.model.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserBody {
  @NotBlank
  @Size(max=24)
  private String user_id;
  
  @NotBlank
  @Size(max=24)
  private String user_name;

  public User to_domain_user() {
    return User.builder()
    .user_id(this.user_id)
    .user_name(this.user_name)
    .build();
  }
}
