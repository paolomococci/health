package local.health.care.services;

import local.health.care.dto.PatientDto;
import local.health.care.dto.PatientPatchDto;
import local.health.care.models.Patient;
import local.health.care.repositories.PatientRepository;
import local.health.care.exceptions.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Service Register as a Spring bean; automatically detected by component
 *          scanning.
 * @Transactional Default transaction for all public methods (read-only by
 *                default).
 */
@Service
@Transactional
public class PatientService {

  // Repository dependency injected via constructor.
  private final PatientRepository patientRepository;

  /**
   * Assign the injected repository.
   * 
   * @param patientRepository
   */
  public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  /**
   * Explicitly mark as read-write (needed for INSERT).
   * 
   * @param patientDto
   * @return
   */
  @Transactional
  public Patient create(PatientDto patientDto) {
    // Build a new Patient entity from the DTO.
    Patient patient = new Patient(
        patientDto.getName(),
        patientDto.getBirthdate(),
        patientDto.getGender());

    return patientRepository.save(patient);
  }

  /**
   * Attempt to load the patient by ID or throw a ResourceNotFoundException if
   * absent.
   * 
   * @param id
   * @return
   */
  public Patient get(Long id) {
    return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found."));
  }

  /**
   * Delegate to a custom query that performs a case-insensitive search.
   * 
   * @param name
   * @return
   */
  public List<Patient> searchByName(String name) {
    return patientRepository.findByNameContainingIgnoreCase(name);
  }

  /**
   * Mark this method as write-enabled (defaults to read-write).
   * 
   * @param id
   * @param dto
   * @return
   */
  @Transactional
  public Patient update(Long id, PatientDto dto) {
    // Retrieve the existing patient (will throw if missing).
    Patient patient = get(id);

    // Replace all mutable fields with the values from the DTO.
    patient.setName(dto.getName());
    patient.setBirthdate(dto.getBirthdate());
    patient.setGender(dto.getGender());

    return patient;
  }

  /**
   * Patch operation also needs a write transaction.
   * 
   * @param id
   * @param dto
   * @return
   */
  @Transactional
  public Patient patch(Long id, PatientPatchDto dto) {
    // Retrieve the target patient.
    Patient patient = get(id);

    // Apply partial updates only if a value is present in the PatchDTO.
    dto.name().ifPresent(patient::setName);
    dto.birthdate().ifPresent(patient::setBirthdate);
    dto.gender().ifPresent(patient::setGender);

    // Return the modified entity; changes are automatically persisted when the
    // transaction commits.
    return patient;
  }

  /**
   * Delegate to the repository’s delete operation.
   * This method is not marked @Transactional so it inherits the class-level
   * transaction (read-only).
   * Spring will automatically start a write transaction for deleteById() because
   * the repository method is annotated with @Transactional on the repository
   * interface.
   * 
   * @param id
   */
  public void delete(Long id) {
    patientRepository.deleteById(id);
  }
}
