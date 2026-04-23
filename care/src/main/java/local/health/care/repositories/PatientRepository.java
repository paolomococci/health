package local.health.care.repositories;

import local.health.care.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Repository tells Spring to create a bean that implements this interface and
 *             to handle translation of persistence exceptions into Spring’s
 *             data‑exception hierarchy.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

  // Custom finder method - Spring Data JPA will generate the implementation
  // automatically.
  // It returns all patients whose `name` field contains the supplied string,
  // ignoring case.
  // Useful for “search‑by‑name” scenarios.
  List<Patient> findByNameContainingIgnoreCase(String name);
}
