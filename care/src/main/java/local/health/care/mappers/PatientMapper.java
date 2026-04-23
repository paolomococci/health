package local.health.care.mappers;

import org.springframework.stereotype.Component;
import local.health.care.models.Patient;
import local.health.care.dto.PatientDto;

/**
 * @Component marks this class as a Spring bean. Spring will automatically
 *            instantiate it and make it available for injection wherever a
 *            PatientMapper is needed.
 *            Mapper class - converts between domain objects and DTOs.
 */
@Component
public class PatientMapper {

  /**
   * Parameter: the entity to be converted.
   * Construct a new DTO instance.
   * Copy the patient's name, birthdate and gender.
   * 
   * @param patient
   * @return
   */
  public PatientDto toDto(Patient patient) {
    return new PatientDto(
        patient.getName(),
        patient.getBirthdate(),
        patient.getGender());
  }
}
