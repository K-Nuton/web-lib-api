package web_diary.api.application.resource;

import web_diary.api.domain.model.Diary;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DiaryUpdateBody {

  @NotBlank
  private Integer diary_id = 0;

  private String date;

  private Integer wheather;

  private Integer feeling;

  private String text;

  public Diary to_domain_diary() throws RuntimeException {
    Diary result = Diary.builder()
    .diary_id(this.diary_id)
    .build();
    
    boolean err = true;
    if (this.date != null) {
      err = false;
      result.setDate(LocalDateTime.parse(this.date));
    }
    if (this.wheather != null) {
      err = false;
      result.setWheather(this.wheather);
    }
    if (this.feeling != null) {
      err = false;
      result.setFeeling(this.feeling);
    }
    if (this.text != null) {
      err = false;
      result.setText(this.text);
    }

    if (err) {
      throw new RuntimeException("日付, 天気, 気持ち, 本文 のいずれも更新対象になっていません。");
    }

    return result;
  }
}
