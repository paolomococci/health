package local.health.research.models;

import java.util.Set;

import jakarta.persistence.ElementCollection;
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
// Extends BaseEntity, adding peer-specific fields.
public abstract class PeerEntity extends BaseEntity {

  // The person's name.
  private String name;

  // Organization (University, Hospital, etc.).
  private String affiliation;

  // Maps the collection to a separate table of simple values.
  @ElementCollection
  // A set of email addresses associated with the peer.
  private Set<String> emails;
}
