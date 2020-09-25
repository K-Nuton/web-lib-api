package web_diary.api.infrastructure.entity;

import web_diary.api.DateTimeLite;
import web_diary.api.domain.model.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryEntity {
  private Integer inner_user_id;
  private Integer diary_id;
  private String date;
  private String update_date;
  private Integer wheather;
  private Integer feeling;
  private String text;

  private static final String INNER_USER_ID = "inner_user_id";
  private static final String DIARY_ID = "diary_id";
  private static final String DATE = "date";
  private static final String UPDATE_DATE = "update_date";
  private static final String WHEATHER = "wheather";
  private static final String FEELING = "feeling";
  private static final String TEXT = "text";

  public static DiaryEntity set_result(Map<String, Object> result) {
    return DiaryEntity.builder()
      .inner_user_id((Integer) result.get(INNER_USER_ID))
      .diary_id((Integer) result.get(DIARY_ID))
      .date(DateTimeLite.getInstance().setTimestamp((Timestamp) result.get(DATE)).toString())
      .update_date(DateTimeLite.getInstance().setTimestamp((Timestamp) result.get(UPDATE_DATE)).toString())
      .wheather((Integer) result.get(WHEATHER))
      .feeling((Integer) result.get(FEELING))
      .text((String) result.get(TEXT))
      .build();
  }

  public static DiaryEntity build(Diary diary) {
    return DiaryEntity.builder()
      .inner_user_id(diary.getInner_user_id())
      .diary_id(diary.getDiary_id())
      .date(diary.getDate())
      .update_date(diary.getUpdate_date())
      .wheather(diary.getWheather())
      .feeling(diary.getFeeling())
      .text(diary.getText())
      .build();
  }

  public Diary get_domain_instance() {
    return Diary.builder()
      .inner_user_id(this.inner_user_id)
      .diary_id(this.diary_id)
      .date(this.date)
      .update_date(this.update_date)
      .wheather(this.wheather)
      .feeling(this.feeling)
      .text(this.text)
      .build();
  }
}
