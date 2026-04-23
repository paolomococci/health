package local.health.care.controllers;

import local.health.care.dto.ObservationDto;
import local.health.care.models.Observation;
import local.health.care.services.ObservationService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ObservationController {
  private final ObservationService observationService;

  public ObservationController(ObservationService observationService) {
    this.observationService = observationService;
  }

  @PostMapping("/episodes/{episodeId}/observations")
  public ResponseEntity<Observation> create(@PathVariable Long episodeId,
      @Valid @RequestBody ObservationDto observationDto) {
    Observation created = observationService.create(episodeId, observationDto);
    return ResponseEntity.created(URI.create("/api/observations/" + created.getId())).body(created);
  }

  @GetMapping("/episodes/{episodeId}/observations")
  public ResponseEntity<List<Observation>> forEpisode(@PathVariable Long episodeId) {
    return ResponseEntity.ok(observationService.findByEpisode(episodeId));
  }

  @GetMapping("/observations")
  public ResponseEntity<List<Observation>> byCode(@RequestParam(value = "code", required = false) String code) {
    if (code == null)
      return ResponseEntity.ok(List.of());
    return ResponseEntity.ok(observationService.findByCode(code));
  }
}
