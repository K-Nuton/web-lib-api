package web_diary.api.infrastructure;

import web_diary.api.domain.model.Diary;
import web_diary.api.domain.repository.DiaryRepository;
import web_diary.api.infrastructure.entity.DiaryEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.math.BigInteger;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepository {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private final String INNER_JOIN = "select inner_user_id, diary.diary_id, date, update_date, " + 
                                    "wheather, feeling, text from diary inner join diary_link " + 
                                    "on diary.diary_id = diary_link.diary_id ";

  private final String FIND_ALL_BY_INNER_USER_ID = INNER_JOIN + "where inner_user_id = ?";
  
  private final String FIND_BY_DIARY_ID = INNER_JOIN + "where diary.diary_id = ?";      
                                          
  private final String INSERT1 = "insert into diary_link (inner_user_id) values (?)";                                          

  private final String INSERT2 = "insert into diary (diary_id, date, wheather, feeling, text) " + 
                                 "values (?, ?, ?, ?, ?)";

  private final String DELETE = "delete diary, diary_link " +
                                "from diary inner join diary_link on diary.diary_id = diary_link.diary_id " + 
                                "where diary.diary_id = ?";                          

  private final String DATE = "date";
  private final String WHEATHER = "wheather";
  private final String FEELING = "feeling";
  private final String TEXT = "text"; 

  @Override
  public List<Diary> find_by_inner_user_id(Integer inner_user_id) {
    List<Map<String, Object>> list = jdbcTemplate.queryForList(
      FIND_ALL_BY_INNER_USER_ID, inner_user_id
    );

    return list.stream()
    .map(DiaryEntity::set_result)
    .map(j -> j.get_domain_instance())
    .collect(Collectors.toList());
  }

  @Override
  public Diary find_by_diary_id(Integer diary_id) {
    Map<String, Object> result = jdbcTemplate.queryForMap(
      FIND_BY_DIARY_ID, diary_id
    );

    return DiaryEntity.set_result(result)
    .get_domain_instance();
  }

  @Override
  public List<Diary> find_by_multi_condition(Diary diary, Diary end) {
    String update_part = create_search(diary, end);

    String query = INNER_JOIN + " where inner_user_id = " +  diary.getInner_user_id() + update_part;

    List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
    
    return result.stream().map(DiaryEntity::set_result)
    .map(r -> r.get_domain_instance())
    .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public Diary insert(Diary diary) {
    jdbcTemplate.update(
      INSERT1, diary.getInner_user_id()
    );

    int diary_id = (
      (BigInteger) jdbcTemplate.queryForMap("select last_insert_id()").get("last_insert_id()")
    ).intValue();

    jdbcTemplate.update(
      INSERT2,
      diary_id,
      diary.getDate(),
      diary.getWheather(),
      diary.getFeeling(),
      diary.getText()
    );

    Map<String, Object> result2 = jdbcTemplate.queryForMap(
      FIND_BY_DIARY_ID, diary_id
    );

    return DiaryEntity.set_result(result2)
    .get_domain_instance();
  }

  @Override
  public Diary update(Diary diary) {
    String update_part = create_update(diary);

    String query = "update diary set " + update_part + " where diary_id = " + diary.getDiary_id();

    jdbcTemplate.update(query);
    Map<String, Object> result = jdbcTemplate.queryForMap(
      FIND_BY_DIARY_ID, diary.getDiary_id()
    );
    
    Diary temp = DiaryEntity.set_result(result).get_domain_instance();

    return temp;
  }

  @Override
  public void delete_diary_by_diary_id(Integer diary_id) {
    jdbcTemplate.update(DELETE, diary_id);
  }

  private String create_update(Diary diary) {
    String result = "";

    if (diary.getDate() != null) {
      result += DATE + "='" + diary.getDate() + "'";
    }

    if (diary.getWheather() != null) {
      result += (result.length() != 0 ? ", " : "") +  WHEATHER + "=" + diary.getWheather();
    }

    if (diary.getFeeling() != null) {
      result += (result.length() != 0 ? ", " : "") + FEELING + "=" + diary.getFeeling();
    }

    if (diary.getText() != null) {
      result += (result.length() != 0 ? ", " : "") + TEXT + "='" + diary.getText() + "'";
    }

    return result;
  }

  private String create_search(Diary diary, Diary end) {
    String result = "";

    if (diary.getDate() != null) {
      result += " and " +  DATE + " between '" + diary.getDate() + "' and '" + end.getDate() + "'";
    }
    if (diary.getWheather() != null) {
      result += " and " + WHEATHER + "=" + diary.getWheather();
    }
    if (diary.getFeeling() != null) {
      result += " and " + FEELING + "=" + diary.getFeeling();
    }
    if (diary.getText() != null) {
      result += " and " + TEXT +  " like '%" + diary.getText() + "%'"; 
    }
    return result;
  }
}
