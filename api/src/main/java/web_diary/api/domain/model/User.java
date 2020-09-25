package web_diary.api.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
  private String user_id;
  private int inner_user_id;
  private String user_name;
}
