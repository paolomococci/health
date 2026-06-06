package local.health.research.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
// Explicit table name: "reviews".
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

  // Conventionally 1 = one star (poor) … 5 = five stars (excellent).
  private Integer rating;
  // The body of the review - free-text commentary and recommendations.
  private String content;

  /**
   * The outcome of the review process. The Decision
   * enum (ACCEPT, REVISION, REJECT) is persisted as a readable
   * string because we use EnumType.STRING.
   */
  @Enumerated(EnumType.STRING)
  private Decision decision;

  /**
   * Review <-> Reviewer (many-to-one)
   *
   * - Many reviews belong to the same reviewer - this is the
   * inverse side of a one-to-many relationship.
   * - fetch = FetchType.LAZY means the Reviewer
   * instance is not immediately loaded; it will be fetched
   * only when getReviewer() is called (or via a join
   * fetch).
   * - @JoinColumn(name = "reviewer_id") defines the foreign
   * key column that points to reviewers.id. No cascade
   * or orphan removal is defined - deleting a Review will only
   * remove the row from the reviews table, the Reviewer
   * itself remains untouched.
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  // FK column in the reviews table.
  @JoinColumn(name = "reviewer_id", nullable = false)
  // Reviewer that authored this review.
  private Reviewer reviewer;

  // It is related to a specific article.
  /**
   * Review <-> Article (many-to-one)
   *
   * - Many reviews are associated with a single article - the owning
   * side of a one-to-many relationship (the Article owns the
   * collection of Review objects).
   * - Lazy fetching keeps the Article object from loading
   * unless explicitly requested.
   * - @JoinColumn(name = "article_id") creates the FK column
   * that points to articles.id. Again, no cascade is
   * defined, so persisting or deleting a Review does not affect
   * the Article entity itself.
   */
  // Many reviews -> one article.
  @ManyToOne(fetch = FetchType.LAZY)
  // FK column in the reviews table.
  @JoinColumn(name = "article_id")
  // Article being evaluated.
  private Article article;
}
