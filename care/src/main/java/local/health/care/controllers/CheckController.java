package local.health.care.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {

  @GetMapping("/check")
  public Map<String, String> check() {
    return Map.of("status", "ok");
  }
}
