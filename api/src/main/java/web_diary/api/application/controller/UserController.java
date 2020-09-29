package web_diary.api.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import web_diary.api.application.resource.UserBody;
import web_diary.api.domain.model.User;
import web_diary.api.domain.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "web_diary/user")
public class UserController {
  
  @NonNull
  private final UserService userService;

  @GetMapping("{user_id}")
  @ResponseStatus(HttpStatus.OK)
  public User find_by_user_id(@PathVariable("user_id") String user_id) {
    return this.userService.find_by_user_id(user_id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User insert(@Validated @RequestBody UserBody userBody, BindingResult result) {
    if (result.hasErrors()) {
      throw new RuntimeException("入力値が不足しています。");
    }

    return this.userService.insert(userBody.to_domain_user());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public User update(@RequestBody @Validated UserBody userBody, BindingResult result) {
    if (result.hasErrors()) {
      throw new RuntimeException("入力値が不足しています。");
    }

    return this.userService.update(userBody.to_domain_user());
  }

  @DeleteMapping("{user_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete_by_user_id(@PathVariable("user_id") String user_id) {
    this.userService.delete_by_user_id(user_id);
  }
}
