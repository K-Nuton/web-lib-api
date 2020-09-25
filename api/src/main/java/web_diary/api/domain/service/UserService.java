package web_diary.api.domain.service;

import web_diary.api.domain.model.User;
import web_diary.api.domain.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  @NonNull
  private final UserRepository userRepository;

  public User find_by_user_id(String user_id) {
    return this.userRepository.find_by_user_id(user_id);
  }

  public User update(User user) {
    return this.userRepository.update(user);
  }

  public User insert(User user) {
    return this.userRepository.insert(user);
  }

  public void delete_by_user_id(String user_id) {
    this.userRepository.delete_by_user_id(user_id);
  }
}
