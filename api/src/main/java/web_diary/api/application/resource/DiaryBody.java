package web_diary.api.application.resource;

import web_diary.api.domain.model.Diary;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DiaryBody {
  @NotBlank
  private Integer inner_user_id;
  
  @NotBlank
  private String date;
  
  @NotBlank
  private Integer wheather;
  
  @NotBlank
  private Integer feeling;
  
  @NotBlank
  private String text;

  public Diary to_domain_diary() {
    return Diary.builder()
    .inner_user_id(this.inner_user_id)
    .date(LocalDateTime.parse(this.date))
    .wheather(this.wheather)
    .feeling(this.feeling)
    .text(this.text)
    .build();
  }
}
