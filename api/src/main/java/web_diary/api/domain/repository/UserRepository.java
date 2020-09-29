package web_diary.api.domain.repository;

import web_diary.api.domain.model.User;

public interface UserRepository {
  User find_by_user_id(String user_id);

  boolean user_exists(Integer inner_user_id);

  User update(User user);

  User insert(User user);

  void delete_by_user_id(String user_id);
}
