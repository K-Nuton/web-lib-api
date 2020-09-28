package web_diary.api.application.resource;

import web_diary.api.DateTimeLite;
import web_diary.api.domain.model.Diary;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchBody {

  @NotNull
  private Integer inner_user_id;

  private String date;

  private String end_date;

  private Integer wheather;

  private Integer feeling;

  private String text;

  public Diary to_domain_diary() throws RuntimeException {
    Diary result = Diary.builder()
    .inner_user_id(this.inner_user_id)
    .build();
    
    boolean err = true;
    if (this.date != null) {
      err = false;
      result.setDate(DateTimeLite.getInstance().parse(this.date).toString());
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
      throw new RuntimeException("日付, 天気, 気持ち, 本文 のいずれも検索対象になっていません。");
    }

    return result;
  }

  public Diary get_end_priod() throws RuntimeException {
    Diary result = Diary.builder().build();
    if (this.end_date == null && this.date != null) {
      throw new RuntimeException("範囲検索をする場合は、検索終了位置: \"end_date\"を設定してください。");
    }

    if (this.end_date != null) {
      result.setDate(DateTimeLite.getInstance().parse(this.end_date).toString());
    }
    return result;
  }
}
