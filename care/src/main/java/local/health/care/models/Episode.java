package local.health.care.models;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "episodes")
public class Episode {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Instant startedAt;
  private String kind;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Observation> observations = new ArrayList<>();

  public Episode() {}

  public Episode(Instant startedAt, String kind) {
        this.startedAt = startedAt;
        this.kind = kind;
    }

  public Long getId() {
    return id;
  }

  public Instant getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(Instant startedAt) {
    this.startedAt = startedAt;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public List<Observation> getObservations() {
    return observations;
  }

  public void addObservation(Observation observation) {
    observations.add(observation);
    observation.setEpisode(this);
  }
}
