package web_diary.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diary {
  private Integer inner_user_id;
  private Integer diary_id;
  private LocalDateTime date;
  private LocalDateTime update_date;
  private Integer wheather;
  private Integer feeling;
  private String text;
}
