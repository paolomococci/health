package local.health.care.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PatientDto {
  @NotBlank
  private String name;
  @NotNull
  private LocalDate birthdate;
  @NotBlank
  private String gender;

  public PatientDto() {
  }

  public PatientDto(String name, LocalDate birthdate, String gender) {
    this.name = name;
    this.birthdate = birthdate;
    this.gender = gender;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }
}
