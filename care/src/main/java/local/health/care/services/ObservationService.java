package local.health.care.services;

import local.health.care.dto.ObservationDto;
import local.health.care.models.Episode;
import local.health.care.models.Observation;
import local.health.care.repositories.EpisodeRepository;
import local.health.care.repositories.ObservationRepository;
import local.health.care.exceptions.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ObservationService {
  private final ObservationRepository observationRepository;
  private final EpisodeRepository episodeRepository;

  public ObservationService(ObservationRepository observationRepository, EpisodeRepository episodeRepository) {
    this.observationRepository = observationRepository;
    this.episodeRepository = episodeRepository;
  }

  public Observation create(Long episodeId, ObservationDto observationDto) {
    Episode episode = episodeRepository.findById(episodeId)
        .orElseThrow(() -> new ResourceNotFoundException("Episode not found"));
    Observation observation = new Observation(
        observationDto.getCode(),
        observationDto.getKind(),
        observationDto.getUnit(),
        observationDto.getRecordedAt());
    observation.setEpisode(episode);
    return observationRepository.save(observation);
  }

  public List<Observation> findByEpisode(Long episodeId) {
    return observationRepository.findByEpisodeId(episodeId);
  }

  public List<Observation> findByCode(String code) {
    return observationRepository.findByCode(code);
  }
}
