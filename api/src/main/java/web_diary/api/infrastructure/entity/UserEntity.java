package web_diary.api.infrastructure.entity;

import web_diary.api.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
  private String user_id;
  private int inner_user_id;
  private String user_name;

  private final String USER_ID = "user_id";
  private final String INNER_USER_ID = "inner_user_id";
  private final String USER_NAME = "user_name";

  public UserEntity set_result(Map<String, Object> result) {
    this.setInner_user_id((int) result.get(INNER_USER_ID));
    this.setUser_id((String) result.get(USER_ID));
    this.setUser_name((String) result.get(USER_NAME));
    return this;
  }

  public static UserEntity build(User user) {
    return UserEntity.builder()
    .user_id(user.getUser_id())
    .inner_user_id(user.getInner_user_id())
    .user_name(user.getUser_name())
    .build();
  }

  public User get_domain_instance() {
    return User.builder()
      .user_id(this.user_id)
      .inner_user_id(this.inner_user_id)
      .user_name(this.user_name)
      .build();
  }
}
