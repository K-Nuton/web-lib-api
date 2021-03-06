package web_diary.api.domain.service;

import web_diary.api.domain.exception.NotFoundException;
import web_diary.api.domain.model.Diaries;
import web_diary.api.domain.model.Diary;
import web_diary.api.domain.repository.DiaryRepository;
import web_diary.api.domain.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {
  @NonNull
  private DiaryRepository diaryRepository;

  @NonNull 
  private UserRepository userRepository;

  private final String REPLACE = "%%";
  private final String USER_NOT_EXISTS = "user: %% does not exists.";

  public Diaries find_by_inner_user_id(Integer inner_user_id) {
    if (!userRepository.user_exists(inner_user_id))
      throw new NotFoundException(USER_NOT_EXISTS.replace(REPLACE, inner_user_id.toString()));

    return this.diaryRepository.find_by_inner_user_id(inner_user_id);
  }

  public Diary find_by_diary_id(Integer diary_id) {
    return this.diaryRepository.find_by_diary_id(diary_id);
  }

  public Diaries find_by_multi_condition(Diary diary, Diary end) {
    return this.diaryRepository.find_by_multi_condition(diary, end);
  }

  @Transactional
  public Diary insert(Diary diary) {
    if (!userRepository.user_exists(diary.getInner_user_id()))
      throw new NotFoundException(USER_NOT_EXISTS.replace(REPLACE, diary.getInner_user_id().toString()));

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
