package web_diary.api.application.controller;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import web_diary.api.application.resource.input_body.SearchBody;
import web_diary.api.domain.exception.InvalidJsonValueException;
import web_diary.api.domain.model.Diary;
import web_diary.api.domain.service.DiaryService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "web_diary/search")
public class SearchController {
  
  @NonNull
  private final DiaryService diaryService;

  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<Diary> search(@RequestBody @Validated SearchBody searchBody, BindingResult result) {
    if (result.hasErrors())
      throw InvalidJsonValueException.getInstance(result);
    
    return this.diaryService.find_by_multi_condition(
      searchBody.to_domain_diary(),
      searchBody.get_end_priod()
    );
  }
}
