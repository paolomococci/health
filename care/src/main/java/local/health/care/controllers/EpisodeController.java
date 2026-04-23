package local.health.care.controllers;

import local.health.care.dto.EpisodeDto;
import local.health.care.models.Episode;
import local.health.care.services.EpisodeService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/episodes")
public class EpisodeController {
  private final EpisodeService episodeService;

  public EpisodeController(EpisodeService episodeService) {
    this.episodeService = episodeService;
  }

  @PostMapping
  public ResponseEntity<Episode> create(@PathVariable Long patientId, @Valid @RequestBody EpisodeDto episodeDto) {
    Episode created = episodeService.create(patientId, episodeDto);
    return ResponseEntity.created(URI.create("/api/episodes/" + created.getId())).body(created);
  }

  @GetMapping
  public ResponseEntity<List<Episode>> list(@PathVariable Long patientId) {
    return ResponseEntity.ok(episodeService.findByPatient(patientId));
  }
}
