package web_diary.api.infrastructure;

import web_diary.api.domain.exception.DataDuplicationException;
import web_diary.api.domain.exception.NotFoundException;
import web_diary.api.domain.model.Diaries;
import web_diary.api.domain.model.Diary;
import web_diary.api.domain.repository.DiaryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigInteger;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepository {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final BeanPropertyRowMapper<Diary> MAPPER = new BeanPropertyRowMapper<>(Diary.class);

  private final String INNER_JOIN = "select inner_user_id, diary.diary_id, date, update_date, " + 
                                    "wheather, feeling, text from diary inner join diary_link " + 
                                    "on diary.diary_id = diary_link.diary_id ";

  private final String FIND_BY_INNER_USER_ID = INNER_JOIN + "where inner_user_id = ?";
  private final String FIND_BY_DIARY_ID = INNER_JOIN + "where diary.diary_id = ?";
  
  private final String UPDATE_SET = "update diary set ";
  private final String WHERE_DIARY_ID = " where diary_id";
                                          
  private final String INSERT_DIARY_LINK = "insert into diary_link (inner_user_id) values (?)";                                          
  private final String INSERT_DIARY = "insert into diary (diary_id, date, wheather, feeling, text) " + 
                                      "values (?, ?, ?, ?, ?)";

  private final String SELECT_LAST_INSERT_ID = "select last_insert_id()";
  private final String LAST_INSERT_ID = "last_insert_id()";

  private final String DELETE = "delete diary, diary_link " +
                                "from diary inner join diary_link on diary.diary_id = diary_link.diary_id " + 
                                "where diary.diary_id = ?";  
  
                                
  private final String AND = " and ";
  private final String BETWEEN = " between ? and ?";
  private final String DELIMITER = ",";
  private final String EQUAL = " = ?";
  private final String LIKE = " like ?";

  private final String VALUES = "values";
  private final String QUERY = "query";

  private final String DATE = "date";
  private final String WHEATHER = "wheather";
  private final String FEELING = "feeling";
  private final String TEXT = "text"; 

  private final String REPLACE = "%%";
  private final String NO_DIARY = "user: %% has no diary";
  private final String NOT_FOUND = "There is no diary related to diary_id: %%.";
  private final String NOT_FOUND_BY_CONDITION = "There are no diaries with requested conditions.";

  @Override
  public Diaries find_by_inner_user_id(Integer inner_user_id) {
    List<Diary> result = jdbcTemplate.query(
      FIND_BY_INNER_USER_ID,
      MAPPER,
      inner_user_id
    );

    if (result.size() == 0) 
      throw new NotFoundException(NO_DIARY.replace(REPLACE, inner_user_id.toString()));
    
    return Diaries.builder().diaries(result).build();
  }

  @Override
  public Diary find_by_diary_id(Integer diary_id) {
    try {
      return jdbcTemplate.queryForObject(
        FIND_BY_DIARY_ID,
        MAPPER,
        diary_id
      );
    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException(NOT_FOUND.replace(REPLACE, diary_id.toString()));
    }
  }

  @Override
  public Diaries find_by_multi_condition(Diary diary, Diary end) {
    Map<String, String[]> sqls = get_select_sql(diary, end);

    List<Diary> result = jdbcTemplate.query(
      String.join(AND, sqls.get(QUERY)),
      MAPPER,
      (Object[])sqls.get(VALUES)
    );

    if (result.size() == 0)
      throw new NotFoundException(NOT_FOUND_BY_CONDITION);

    return Diaries.builder().diaries(result).build();
  }

  @Override
  public Diary insert(Diary diary) {
    int diary_id;
    try {
      jdbcTemplate.update(
        INSERT_DIARY_LINK, 
        diary.getInner_user_id()
      );

      diary_id = (
        (BigInteger)jdbcTemplate.queryForMap(SELECT_LAST_INSERT_ID).get(LAST_INSERT_ID)
      ).intValue();

      jdbcTemplate.update(
        INSERT_DIARY,
        diary_id,
        diary.getDate(),
        diary.getWheather(),
        diary.getFeeling(),
        diary.getText()
      );

    } catch (DuplicateKeyException e) {
      throw new DataDuplicationException(e.getMessage());
    }

    return find_by_diary_id(diary_id);
  }

  @Override
  public Diary update(Diary diary) {
    Map<String, String[]> sqls = get_update_sql(diary);
    String query = UPDATE_SET + String.join(DELIMITER, sqls.get(QUERY)) + WHERE_DIARY_ID + EQUAL;

    try {
      jdbcTemplate.update(query, (Object[]) sqls.get(VALUES));

    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException(NOT_FOUND.replace(REPLACE, diary.getDiary_id().toString()));
    }

    return find_by_diary_id(diary.getDiary_id());
  }

  @Override
  public void delete_diary_by_diary_id(Integer diary_id) {
    if (jdbcTemplate.update(DELETE, diary_id) == 0)
      throw new NotFoundException(NOT_FOUND.replace(REPLACE, diary_id.toString()));
  }

  private Map<String, String[]> get_update_sql(Diary diary) {
    Map<String, String[]> result = new HashMap<>();

    List<String> query = new ArrayList<>();
    List<String> values = new ArrayList<>();

    if (diary.getDate() != null) {
      query.add(DATE + EQUAL);
      values.add(diary.getDate().toString());
    }
    if (diary.getWheather() != null) {
      query.add(WHEATHER + EQUAL);
      values.add(diary.getWheather().toString());
    }
    if (diary.getFeeling() != null) {
      query.add(FEELING + EQUAL);
      values.add(diary.getFeeling().toString());
    }
    if (diary.getText() != null) {
      query.add(TEXT + EQUAL);
      values.add(diary.getText());
    }

    values.add(diary.getDiary_id().toString());
    result.put(QUERY, query.toArray(new String[0]));
    result.put(VALUES, values.toArray(new String[0]));

    return result;
  }

  private Map<String, String[]> get_select_sql(Diary diary, Diary end) {
    Map<String, String[]> result = new HashMap<>();

    List<String> query = new ArrayList<>(); 
    List<String> values = new ArrayList<>();

    query.add(FIND_BY_INNER_USER_ID);
    values.add(diary.getInner_user_id().toString());

    if (diary.getDate() != null) {
      values.add(diary.getDate().toString());
      values.add(end.getDate().toString());
      query.add(DATE + BETWEEN);
    }
    if (diary.getWheather() != null) {
      values.add(diary.getWheather().toString());
      query.add(WHEATHER + EQUAL);
    }
    if (diary.getFeeling() != null) {
      values.add(diary.getFeeling().toString());
      query.add(FEELING + EQUAL);
    }
    if (diary.getText() != null) {
      values.add("%" + escape(diary.getText()) + "%");
      query.add(TEXT + LIKE);
    }

    result.put(VALUES, values.toArray(new String[0]));
    result.put(QUERY, query.toArray(new String[0]));

    return result;
  }

  private String escape(String text) {
    return text.replace("\\", "\\\\")
    .replace("_", "\\_")
    .replace("%", "\\%");
  }
}
