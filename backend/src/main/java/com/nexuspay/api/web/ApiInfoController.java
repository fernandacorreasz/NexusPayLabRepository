package com.nexuspay.api.web;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class ApiInfoController {
  private final JdbcTemplate jdbc;
  public ApiInfoController(JdbcTemplate jdbc) { this.jdbc = jdbc; }
  @GetMapping({"/","/api"})
  public Map<String,Object> info() { return Map.of("name","NexusPay","version","1.0.0","docs","/swagger-ui/index.html"); }
  @GetMapping("/api/debug/statement")
  public Map<String,String> debug(@RequestParam String sql) {
    try { return Map.of("result", String.valueOf(jdbc.queryForList(sql))); }
    catch (Exception ex) { return Map.of("error", ex.toString(), "cause", String.valueOf(ex.getCause())); }
  }
}
