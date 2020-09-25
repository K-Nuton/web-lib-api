package web_diary.api.application.controller;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import web_diary.api.application.resource.SearchBody;
import web_diary.api.domain.model.Diary;
import web_diary.api.domain.service.DiaryService;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "web_diary/search")
public class SearchController {
  
  @NonNull
  private final DiaryService searchService;

  @GetMapping("{inner_user_id}")
  @ResponseStatus(HttpStatus.OK)
  public List<Diary> get_all_diary(@PathVariable Integer inner_user_id) {
    return this.searchService.find_by_inner_user_id(inner_user_id);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<Diary> get_multi_diary(@RequestBody @Validated SearchBody searchBody, BindingResult result) {
    if (result.hasErrors()) {
      throw new RuntimeException("入力値が不足しています。");
    }
    
    return this.searchService.find_by_multi_condition(searchBody.to_domain_diary(), searchBody.get_end_priod());
  }
}
