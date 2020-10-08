package web_diary.api.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import web_diary.api.application.resource.input_body.DiaryBody;
import web_diary.api.application.resource.input_body.DiaryUpdateBody;
import web_diary.api.domain.exception.InvalidJsonValueException;
import web_diary.api.domain.model.Diaries;
import web_diary.api.domain.model.Diary;
import web_diary.api.domain.service.DiaryService;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
  public Diaries get_all_diary(@PathVariable("inner_user_id") Integer inner_user_id) {
    return this.diaryService.find_by_inner_user_id(inner_user_id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Diary insert(@RequestBody @Validated DiaryBody diaryBody, BindingResult result) {
    if (result.hasErrors())
      throw InvalidJsonValueException.getInstance(result);

    return this.diaryService.insert(diaryBody.to_domain_diary());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public Diary update(@RequestBody @Validated DiaryUpdateBody diarybody, BindingResult result) {
    if (result.hasErrors())
      throw InvalidJsonValueException.getInstance(result);

    return this.diaryService.update(diarybody.to_domain_diary());
  }

  @DeleteMapping("{diary_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete_diary_by_diary_id(@PathVariable("diary_id") Integer diary_id) {
    this.diaryService.delete_diary_by_diary_id(diary_id);
  }
}
