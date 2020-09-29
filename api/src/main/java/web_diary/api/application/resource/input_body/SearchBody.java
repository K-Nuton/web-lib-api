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
public class SearchBody {

  @NotNull
  private Integer inner_user_id;

  @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[0-1])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]).([0-9]{1,3})$")
  private String date;

  @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[0-1])T([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]).([0-9]{1,3})$")
  private String end_date;

  @Min(0)
  @Max(6)
  private Integer wheather;

  @Min(0)
  @Max(9)
  private Integer feeling;

  @Size(min=2)
  private String text;

  public Diary to_domain_diary() throws RuntimeException {
    if (this.is_empty_instance())
      throw new InvalidJsonValueException("For a diary search, you must include at least one of the date( and end_date), weather, feelings, and text in the JSON.");

    Diary result = Diary.builder()
    .inner_user_id(this.inner_user_id)
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

  public Diary get_end_priod() throws RuntimeException {
    if (this.end_date == null && this.date != null)
      throw new InvalidJsonValueException("If you use date range search, set the end date to [end_date].");

    Diary result = Diary.builder().build();
    
    if (this.end_date != null)
      result.setDate(LocalDateTime.parse(this.end_date));

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
