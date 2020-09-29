package web_diary.api.domain.service;

import web_diary.api.domain.model.Diary;
import web_diary.api.domain.repository.DiaryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
  @NonNull
  private DiaryRepository diaryRepository;

  public List<Diary> find_by_inner_user_id(Integer inner_user_id) {
    return this.diaryRepository.find_by_inner_user_id(inner_user_id);
  }

  public Diary find_by_diary_id(Integer diary_id) {
    return this.diaryRepository.find_by_diary_id(diary_id);
  }

  public List<Diary> find_by_multi_condition(Diary diary, Diary end) {
    return this.diaryRepository.find_by_multi_condition(diary, end);
  }

  @Transactional
  public Diary insert(Diary diary) {
    return this.diaryRepository.insert(diary);
  }

  @Transactional
  public Diary update(Diary diary) {
    return this.diaryRepository.update(diary);
  }

  @Transactional
  public void delete_diary_by_diary_id(Integer diary_id) {
    this.diaryRepository.delete_diary_by_diary_id(diary_id);
  }
}
