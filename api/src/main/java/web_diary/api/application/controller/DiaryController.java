package web_diary.api.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import web_diary.api.application.resource.DiaryBody;
import web_diary.api.application.resource.DiaryUpdateBody;
import web_diary.api.application.resource.SearchBody;
import web_diary.api.domain.model.Diary;
import web_diary.api.domain.service.DiaryService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "web_diary/diary")
public class DiaryController {
  
  @NonNull
  private final DiaryService diaryService;

  @GetMapping("{diary_id}")
  @ResponseStatus(HttpStatus.OK)
  public Diary find_by_diary_id(@PathVariable("diary_id") Integer diary_id) {
    return this.diaryService.find_by_diary_id(diary_id);
  }

  @PostMapping("{inner_user_id}") 
  @ResponseStatus(HttpStatus.OK)
  public List<Diary> get_all_diary(@PathVariable("inner_user_id") Integer inner_user_id) {
    return this.diaryService.find_by_inner_user_id(inner_user_id);
  }

  @PostMapping("{inner_user_id}/{limit_date}") 
  @ResponseStatus(HttpStatus.OK)
  public List<Diary> search(@RequestBody @Validated SearchBody searchBody, BindingResult result, @PathVariable("limit_date") String limit_date) {
    if (result.hasErrors()) {
      throw new RuntimeException("入力値が不足しています。");
    }
    return this.diaryService.find_by_multi_condition(
      searchBody.to_domain_diary(), 
      Diary.builder()
      .date(LocalDateTime.parse(limit_date))
      .build()
    );
  }

  @Transactional
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Diary insert(@RequestBody @Validated DiaryBody diaryBody, BindingResult result) {
    if (result.hasErrors()) {
      throw new RuntimeException("入力値が不足しています。");
    }
    return this.diaryService.insert(diaryBody.to_domain_diary());
  }

  @Transactional
  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public Diary update(@RequestBody DiaryUpdateBody diarybody, BindingResult result) {
    if (result.hasErrors()) {
      throw new RuntimeException("入力値が不足しています。");
    }
    return this.diaryService.update(diarybody.to_domain_diary());
  }

  @Transactional
  @DeleteMapping("{diary_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete_diary_by_diary_id(@PathVariable("diary_id") Integer diary_id) {
    this.diaryService.delete_diary_by_diary_id(diary_id);
  }
}
