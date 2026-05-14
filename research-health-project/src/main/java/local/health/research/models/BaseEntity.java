package local.health.research.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Entity classes that extend this class inherit its mapping.
@MappedSuperclass
// Generates a public getter for each field.
@Getter
// Generates a public setter for each field.
@Setter
// Generates a public no-arg constructor (needed by JPA).
@NoArgsConstructor
// Generates a constructor with all fields as parameters.
@AllArgsConstructor
// Abstract base class for all entity types.
public abstract class BaseEntity {

  // Primary key of the entity.
  @Id
  // Auto-increment using the DB's identity column.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // Unique identifier for each entity instance.
  private Long id;

  // Common field that all concrete entities can reuse.
  private String title;
}
