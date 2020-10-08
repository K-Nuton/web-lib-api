package web_diary.api.domain.repository;

import web_diary.api.domain.model.Diaries;
import web_diary.api.domain.model.Diary;

public interface DiaryRepository {
  Diaries find_by_inner_user_id(Integer inner_user_id);

  Diary find_by_diary_id(Integer diary_id);

  Diaries find_by_multi_condition(Diary diary, Diary end);

  Diary insert(Diary diary);

  Diary update(Diary diary);

  void delete_diary_by_diary_id(Integer diary_id);
}
