package local.health.research.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import local.health.research.models.Review;

/**
 * ReviewRepository is a Spring Data interface that manages
 * persistence operations for the Review entity.
 *
 * By extending JpaRepository<Review, Long> the interface
 * inherits standard CRUD and pagination methods:
 * - save, findById, findAll, delete, etc.
 *
 * The generic parameters declare that the entity type is
 * Review and its primary key type is Long.
 *
 * The @Repository annotation registers the interface
 * as a Spring bean and activates exception translation,
 * converting low-level persistence exceptions into
 * Spring's DataAccessException hierarchy.
 *
 * No custom query methods are defined at this point,
 * but additional methods can be added using Spring Data
 * query derivation conventions if needed.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Retrieves all Review entities that are linked to a specific article.
     *
     * Spring Data interprets the method name:
     * findByArticleId
     * and generates a query that selects reviews where
     * review.article.id = :articleId.
     *
     * @param articleId the primary key of the article
     * @return a list of Review objects belonging to the article
     */
    List<Review> findByArticleId(Long articleId);

    /**
     * Retrieves all Review entities authored by a particular reviewer.
     *
     * The method name findByReviewerId tells Spring Data to
     * generate a query that selects reviews where
     * review.reviewer.id = :reviewerId.
     *
     * @param reviewerId the primary key of the reviewer
     * @return a list of Review objects written by the reviewer
     */
    List<Review> findByReviewerId(Long reviewerId);
}
