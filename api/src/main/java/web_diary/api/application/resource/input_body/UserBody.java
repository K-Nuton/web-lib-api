package web_diary.api.application.resource.input_body;

import web_diary.api.domain.model.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserBody {
  @NotBlank
  @Size(min=4, max=24)
  @Pattern(regexp="^[a-z|A-Z|0-9]{4,24}$")
  private String user_id;
  
  @NotBlank
  @Size(min=2, max=24)
  private String user_name;

  public User to_domain_user() {
    return User.builder()
    .user_id(this.user_id)
    .user_name(this.user_name)
    .build();
  }
}
