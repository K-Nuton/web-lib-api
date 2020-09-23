package web_diary.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/jdbc/sample")
public class Test {
  
  @Autowired
  JdbcTemplate jdbcTemplate;

  @RequestMapping(path="/users", method=RequestMethod.GET)
  public String index() {
    List<Map<String, Object>> list;
    list = jdbcTemplate.queryForList("select * from user");
    return list.toString();
  }

  @RequestMapping(path="/users/{id}", method=RequestMethod.GET)
  public String read(@PathVariable String id) {
    List<Map<String, Object>> list;
    list = jdbcTemplate.queryForList("select * from user where user_id = ?", id);
    return list.toString();
  }
}
