package web_diary.api.infrastructure;

import web_diary.api.domain.model.User;
import web_diary.api.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final String SELECT = "select user_link.inner_user_id, user.user_id, user.user_name " + 
                                "from user inner join user_link on user.user_id = user_link.user_id " +
                                "where user.user_id = ?";

  private final String INSERT1 = "insert into user (user_id, user_name) values (?, ?)";

  private final String INSERT2 = "insert into user_link (user_id) values (?)";

  private final String UPDATE = "update user set user_name = ? where user_id = ?";

  private final String DELETE = "delete user, user_link " + 
                                "from user inner join user_link on user.user_id = user_link.user_id " +
                                "where user.user_id = ?";
  
  @Override
  public User insert(User user) {
    jdbcTemplate.update(
      INSERT1, user.getUser_id(), user.getUser_name()
    );

    jdbcTemplate.update(
      INSERT2, user.getUser_id()
    );

    return find_by_user_id(user.getUser_id());
  }

  @Override
  public User update(User user) {
    jdbcTemplate.update(
      UPDATE, user.getUser_name(), user.getUser_id()
    );

    return find_by_user_id(user.getUser_id());
  }

  @Override
  public User find_by_user_id(String user_id) {
    return jdbcTemplate.queryForObject(
      SELECT,
      new BeanPropertyRowMapper<User>(User.class),
      user_id
    );
  }

  @Override
  public void delete_by_user_id(String user_id) {
    jdbcTemplate.update(DELETE, user_id);
  }
}