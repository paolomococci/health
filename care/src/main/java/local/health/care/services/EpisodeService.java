package local.health.care.services;

import local.health.care.dto.EpisodeDto;
import local.health.care.models.Episode;
import local.health.care.models.Patient;
import local.health.care.repositories.EpisodeRepository;
import local.health.care.repositories.PatientRepository;
import local.health.care.exceptions.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EpisodeService {
  private final EpisodeRepository episodeRepository;
  private final PatientRepository patientRepository;

  public EpisodeService(EpisodeRepository episodeRepository, PatientRepository patientRepository) {
    this.episodeRepository = episodeRepository;
    this.patientRepository = patientRepository;
  }

  public Episode create(Long patientId, EpisodeDto episodeDto) {
    Patient patient = patientRepository.findById(patientId)
        .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    Episode episode = new Episode(
        episodeDto.getStartedAt(),
        episodeDto.getKind());
    episode.setPatient(patient);
    return episodeRepository.save(episode);
  }

  public List<Episode> findByPatient(Long patientId) {
    return episodeRepository.findByPatientId(patientId);
  }
}
