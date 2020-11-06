package web_diary.api.infrastructure;

import web_diary.api.domain.exception.DataDuplicationException;
import web_diary.api.domain.exception.NotFoundException;
import web_diary.api.domain.model.User;
import web_diary.api.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final BeanPropertyRowMapper<User> MAPPER = new BeanPropertyRowMapper<>(User.class);

  private final String SELECT = "select user_link.inner_user_id, user.user_id, user.user_name " + 
                                "from user inner join user_link on user.user_id = user_link.user_id " +
                                "where user.user_id = ?";
  
  private final String SELECT_BY_INNER_USER_ID = "select * from user_link where inner_user_id = ?";

  private final String INSERT_USER = "insert into user (user_id, user_name) values (?, ?)";
  private final String INSERT_USER_LINK = "insert into user_link (user_id) values (?)";

  private final String UPDATE = "update user set user_name = ? where user_id = ?";

  private final String DELETE = "delete user, user_link " + 
                                "from user inner join user_link on user.user_id = user_link.user_id " +
                                "where user.user_id = ?";
  
  private final String REPLACE = "%%";
  private final String DUPLICATED = "user_id: "+REPLACE+" already exists";
  private final String NOT_FOUND = "There is no user related to user_id: "+REPLACE+".";
  
  @Override
  public User insert(User user) {
    try {
      jdbcTemplate.update(INSERT_USER, user.getUser_id(), user.getUser_name());
      jdbcTemplate.update(INSERT_USER_LINK, user.getUser_id() );

    } catch (DuplicateKeyException e) {
      throw new DataDuplicationException(DUPLICATED.replace(REPLACE, user.getUser_id()));
    }

    return find_by_user_id(user.getUser_id());
  }

  @Override
  public User update(User user) {
    try {
      jdbcTemplate.update(UPDATE, user.getUser_name(), user.getUser_id());

    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException(NOT_FOUND.replace(REPLACE, user.getUser_id()));
    }

    return find_by_user_id(user.getUser_id());
  }

  @Override
  public User find_by_user_id(String user_id) {
    try {
      return jdbcTemplate.queryForObject(SELECT, MAPPER, user_id);

    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException(NOT_FOUND.replace(REPLACE, user_id));
    }
  }

  @Override
  public boolean user_exists(Integer inner_user_id) {
    try {
      jdbcTemplate.queryForMap(SELECT_BY_INNER_USER_ID, inner_user_id);
      return true;

    } catch (EmptyResultDataAccessException e) {
      return false;
    }
  }

  @Override
  public void delete_by_user_id(String user_id) {
    if (jdbcTemplate.update(DELETE, user_id) == 0) 
      throw new NotFoundException(NOT_FOUND.replace(REPLACE, user_id));
  }
}
