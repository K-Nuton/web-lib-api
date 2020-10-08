package web_diary.api.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Diaries {
  private List<Diary> diaries;
}
