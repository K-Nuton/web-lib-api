package web_diary.api.application.resource;

import web_diary.api.domain.model.Diary;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DiaryBody {
  @NotNull
  private Integer inner_user_id;
  
  @NotNull
  private String date;
  
  @NotNull
  private Integer wheather;
  
  @NotNull
  private Integer feeling;
  
  @NotNull
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
