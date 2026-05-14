package local.health.research.models;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
// Table name is explicit: "reviewers".
@Table(name = "reviewers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reviewer extends PeerEntity {

  /**
   * Article - Reviewer (many-to-many)
   *
   * - Each reviewer can be  "attended " by many articles and each
   * article can have many reviewers.
   * - The owning side (this side) defines the join table:
   * name = "reviewer_article"
   * FK to reviewers -> "reviewer_id"
   * FK to articles -> "article_id"
   * - No cascade is applied - the relationship is *purely
   * referential*: adding a Reviewer does not create or delete
   * Article records.
   */
  @ManyToMany
  // Name of the join table.
  @JoinTable(name = "reviewer_article",
      // FK in the join table that points to "reviewers.id".
      joinColumns = @JoinColumn(name = "reviewer_id"),
      // FK that points to articles.id
      inverseJoinColumns = @JoinColumn(name = "article_id"))
  // Set of articles this reviewer has processed.
  private Set<Article> attendedArticles;

  /**
   * Reviewer - Review (one-to-many)
   *
   * - One reviewer can author many Review objects.
   * - mappedBy = "reviewer" tells JPA that the
   * Review entity owns the foreign key (article_id &
   * reviewer_id) and that we should look at that field to find
   * the collection.
   * - cascade = CascadeType.ALL means every persistence
   * operation performed on a Reviewer (persist, merge,
   * remove, refresh, detach) is forwarded to the contained
   * Review instances.
   * - orphanRemoval = true guarantees that when a
   * Review is removed from the reviews set it will
   * be deleted from the database automatically - a real
   *  "orphan " cleanup.
   * - The set is a Set<Review> because the number of
   * reviews is usually small enough to fit comfortably in memory,
   * and because we need the ordering capability that a Set
   * provides for cascading.
   */
  @OneToMany(
      // Review.reviewer owns the FK.
      mappedBy = "reviewer",
      // Forward all JPA ops.
      cascade = CascadeType.ALL,
      // Delete Review when removed from the set.
      orphanRemoval = true)
  // Reviews authored by this reviewer.
  private Set<Review> reviews;
}
