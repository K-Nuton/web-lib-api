package web_diary.api.application.resource.input_body;

import web_diary.api.domain.model.Diary;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class DiaryBody {
  @NotNull
  private Integer inner_user_id;
  
  @NotNull
  @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[0-1])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]).([0-9]{1,3})$")
  private String date;
  
  @NotNull
  @Min(0)
  @Max(6)
  private Integer wheather;
  
  @NotNull
  @Min(0)
  @Max(9)
  private Integer feeling;
  
  @NotNull
  @Size(min=10)
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
