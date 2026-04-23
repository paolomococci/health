package local.health.care.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "observations")
public class Observation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // e.g., "blood-pressure"
  private String code;
  private String kind;
  private String unit;
  private Instant recordedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "episode_id")
  private Episode episode;

  public Observation() {
  }

  public Observation(String code, String kind, String unit, Instant recordedAt) {
    this.code = code;
    this.kind = kind;
    this.unit = unit;
    this.recordedAt = recordedAt;
  }

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Instant getRecordedAt() {
    return recordedAt;
  }

  public void setRecordedAt(Instant recordedAt) {
    this.recordedAt = recordedAt;
  }

  public Episode getEpisode() {
    return episode;
  }

  public void setEpisode(Episode episode) {
    this.episode = episode;
  }
}
