package local.health.care.controllers;

import local.health.care.dto.PatientDto;
import local.health.care.dto.PatientPatchDTO;
import local.health.care.mappers.PatientMapper;
import local.health.care.models.Patient;
import local.health.care.services.PatientService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @RestController Marks this class as a REST controller.
 * @RequestMapping Base path for all endpoints in this controller.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

  // Dependencies injected via constructor.
  private final PatientService patientService;
  private final PatientMapper mapper;

  public PatientController(PatientService patientService, PatientMapper mapper) {
    this.patientService = patientService;
    this.mapper = mapper;
  }

  /**
   * POST '/api/patients' - create a new patient.
   * 
   * @param patientDto
   * @return
   */
  @PostMapping
  public ResponseEntity<Patient> create(@Valid @RequestBody PatientDto patientDto) {
    // Delegate creation to the service layer.
    Patient created = patientService.create(patientDto);
    // Build a 201 Created response with the location of the new resource.
    return ResponseEntity
        .created(URI.create("/api/patients/" + created.getId()))
        .body(created);
  }

  /**
   * GET '/api/patients/{id}' - retrieve a patient by ID.
   * 
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<Patient> get(@PathVariable Long id) {
    return ResponseEntity.ok(patientService.get(id));
  }

  /**
   * GET '/api/patients?name=' - search patients by name.
   * 
   * @param name
   * @return
   */
  @GetMapping
  public ResponseEntity<List<Patient>> search(@RequestParam(value = "name", required = false) String name) {
    // If no name was provided, search with an empty string (returns all).
    if (name == null || name.isBlank()) {
      return ResponseEntity.ok(patientService.searchByName(""));
    }
    // Perform a case‑insensitive search.
    return ResponseEntity.ok(patientService.searchByName(name));
  }

  /**
   * PUT '/api/patients/{id}' - full update of a patient.
   * @param id
   * @param patientDto
   * @return
   */
  @PutMapping("/{id}")
  public ResponseEntity<Patient> update(
      @PathVariable Long id,
      @Valid @RequestBody PatientDto patientDto) {

    Patient updated = patientService.update(id, patientDto);
    return ResponseEntity.ok(updated);
  }

  /**
   * PATCH '/api/patients/{id}' - partial update of a patient.
   * @param id
   * @param patchDTO
   * @return
   */
  @PatchMapping("/{id}")
  public ResponseEntity<PatientDto> patch(
      @PathVariable Long id,
      @Valid @RequestBody PatientPatchDTO patchDTO) {

    // Apply the patch and get the updated entity.
    Patient updated = patientService.patch(id, patchDTO);
    // Convert the entity back to a DTO for the response.
    PatientDto responseDto = mapper.toDto(updated);

    return ResponseEntity.ok(responseDto);
  }

  /**
   * DELETE '/api/patients/{id}' - remove a patient.
   * @param id
   * @return
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    patientService.delete(id);
    // Return 204 No Content to indicate successful deletion.
    return ResponseEntity.noContent().build();
  }
}
