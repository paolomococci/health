package local.health.research.models;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
// Table name is "researchers"
@Table(name = "researchers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Inherits common attributes (id, name, affiliation, email) from PeerEntity
public class Researcher extends PeerEntity {

  /**
   * Researcher <-> Article (many-to-many)
   *
   * - This side is **the owning side** of the relationship because
   * it defines the join table via @JoinTable.
   *
   * - The join table is named researcher_article and
   * contains two foreign-key columns:
   *
   * * researcher_id - references researchers.id
   * * article_id - references articles.id
   *
   * - The Set<Article> collection holds all articles written
   * by this researcher. Using a Set prevents duplicate
   * entries for the same article.
   *
   * - No cascade or orphanRemoval is specified, so standard
   * persistence behaviour applies:
   * - Persisting a {@code Researcher} will not automatically
   * persist the Article it references.
   * - Removing a Researcher will not delete the articles,
   * only the join table entries will be cleaned up.
   */
  @ManyToMany
  @JoinTable(
    // Name of the join table.
    name = "researcher_article", 
    // FK to this entity (Researcher).
    joinColumns = @JoinColumn(name = "researcher_id"), 
    // FK to the target entity (Article).
    inverseJoinColumns = @JoinColumn(name = "article_id")
  )
  // Collection of authored articles.
  private Set<Article> articles;
}
