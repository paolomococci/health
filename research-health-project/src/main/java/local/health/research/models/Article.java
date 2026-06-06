package local.health.research.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Tells JPA that this is a persistent entity.
@Entity
// The table name in the database will be "articles".
@Table(name = "articles")
// Lombok generates getters for all fields.
@Getter
// Lombok generates setters for all fields.
@Setter
// Lombok generates a no-argument constructor.
@NoArgsConstructor
// Lombok generates a constructor with every field.
@AllArgsConstructor
// Inherits from BaseEntity (likely contains an @Id field).
public class Article extends BaseEntity {

  // The title or subject of the article.
  private String subject;
  // The main body/content of the article.
  private String content;
  // Publication date.
  private LocalDate publishedDate;

  /**
   * Researchers <-> Article (many-to-many)
   *
   * Each article can be written by multiple researchers.
   * Each researcher can author multiple articles.
   *
   * In the owning side of the relationship (i.e. the side that owns the join
   * table), we defined the `@ManyToMany` on the `Researcher` entity with
   * `@JoinTable`. Here, we use `mappedBy = "articles"` to tell Hibernate
   * that this side is the inverse and that the mapping is already defined
   * on `Researcher#articles`. The actual join table is created by
   * the owning side.
   */
  @ManyToMany(mappedBy = "articles")
  @BatchSize(size = 20)
  private Set<Researcher> researchers;

  /**
   * Reviewers <-> Article (many-to-many)
   *
   * An article can be attended by many reviewers, and a reviewer can attend
   * many articles. Similar to the previous relationship, the owning side
   * is on `Reviewer#attendedArticles`. This side is the inverse side
   * (mappedBy) and does not own the join table.
   */
  @ManyToMany(mappedBy = "attendedArticles")
  @BatchSize(size = 20)
  private Set<Reviewer> reviewers;

  /**
   * Article <-> Review (one-to-many)
   *
   * One article can have multiple reviews, but each review belongs to a
   * single article. Therefore we use `@OneToMany`. The `mappedBy`
   * attribute points to the field on the `Review` entity that owns the
   * relationship (`Review#article`). The cascade and orphanRemoval
   * options are important:
   *
   * - `cascade = CascadeType.ALL` - any persistence operation
   * (persist, merge, remove, refresh, detach) performed on Article
   * will automatically propagate to its reviews. This is convenient
   * when you create an article with a set of reviews that you want
   * persisted together.
   *
   * - `orphanRemoval = true` - if a review is removed from the
   * `reviews` set (or its `article` reference is set to null), JPA
   * will automatically delete that review from the database. This
   * guarantees that the database never contains orphaned reviews.
   */
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
  @BatchSize(size = 20)
  private Set<Review> reviews = new HashSet<>();
}
