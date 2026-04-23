package local.health.care.repositories;

import local.health.care.models.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
  List<Episode> findByPatientId(Long patientId);
}
