package web_diary.api.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Diary {
  private Integer inner_user_id;
  private Integer diary_id;
  private String date;
  private String update_date;
  private Integer wheather;
  private Integer feeling;
  private String text;
}
