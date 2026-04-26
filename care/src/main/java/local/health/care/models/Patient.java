package local.health.care.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Entity tells JPA that this class should be mapped to a database table.
 * @Table specifies the exact table name - useful when it differs from the
 *        default (class name converted to snake_case).
 */
@Entity
@Table(name = "patients")
public class Patient {

  /**
   * @Id marks the primary key field.
   * @GeneratedValue(strategy = GenerationType.IDENTITY) lets the database
   *                          generate the key using an auto-increment column.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Patient’s name.
  private String name;

  // Date of birth.
  private LocalDate birthdate;

  // Gender, (could be "MALE"/"FEMALE"/"OTHER").
  private String gender;

  /**
   * @OneToMany defines a one-to-many relationship to Episode, (to be understood
   *            as a clinical episode).
   *            mappedBy = "patient" tells JPA that the foreign key lives on the
   *            Episode side (the owning side).
   *            cascade = CascadeType.ALL propagates all entity operations
   *            (persist, merge, remove, etc.) from Patient to its Episodes.
   *            orphanRemoval = true automatically deletes an Episode if it is
   *            removed from the collection.
   */
  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Episode> episodes = new ArrayList<>();

  // Constructors, getters, setters.

  public Patient() {
  }

  public Patient(String name, LocalDate birthdate, String gender) {
    this.name = name;
    this.birthdate = birthdate;
    this.gender = gender;
  }

  public Long getId() {
    return id;
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

  public List<Episode> getEpisodes() {
    return episodes;
  }

  /**
   * Helper method to add an Episode to the patient's collection.
   * It also sets the back-reference on the Episode side, ensuring both sides of
   * the relationship stay in sync.
   * 
   * @param episode
   */
  public void addEpisode(Episode episode) {
    episodes.add(episode);
    episode.setPatient(this);
  }
}
