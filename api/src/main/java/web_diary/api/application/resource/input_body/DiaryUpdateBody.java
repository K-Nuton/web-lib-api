package web_diary.api.application.resource.input_body;

import web_diary.api.domain.exception.InvalidJsonValueException;
import web_diary.api.domain.model.Diary;

import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class DiaryUpdateBody {
  @NotNull
  private Integer diary_id;

  @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[0-1])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]).([0-9]{1,3})$")
  private String date;

  @Min(0)
  @Max(6)
  private Integer wheather;

  @Min(0)
  @Max(9)
  private Integer feeling;

  @Size(min=10)
  private String text;

  public Diary to_domain_diary() {
    if (this.is_empty_instance())
      throw new InvalidJsonValueException("When updating diary, at least one of the date, weather, feeling, and text must be included in the JSON.");

    Diary result = Diary.builder()
    .diary_id(this.diary_id)
    .build();
    
    if (this.date != null)
      result.setDate(LocalDateTime.parse(this.date));
    
    if (this.wheather != null)
      result.setWheather(this.wheather);
    
    if (this.feeling != null) 
      result.setFeeling(this.feeling);
    
    if (this.text != null) 
      result.setText(this.text);

    return result;
  }

  private boolean is_empty_instance() {
    return (
      this.date == null &&
      this.wheather == null &&
      this.feeling == null &&
      this.text == null
    );
  }
}
