package local.health.research.services;

// Import collection and optional utilities.
import java.util.List;
import java.util.Optional;

// Spring stereotypes and transaction support.
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import local.health.research.dao.ArticleDao;
import local.health.research.dao.ReviewDao;
import local.health.research.dao.ReviewerDao;
import local.health.research.inputs.ReviewInput;
import local.health.research.inputs.ReviewPatch;
import local.health.research.models.Article;
// Domain models.
import local.health.research.models.Review;
import local.health.research.models.Reviewer;

/**
 * Service layer that orchestrates business logic for Researcher entities.
 * All public methods run inside a transaction thanks to @Transactional.
 * It uses Spring Data repositories (DAOs) for persistence.
 * Data Access Object is a design pattern to provide an abstract layer to some
 * type of persistence implementations.
 * Each public method is transactional because of @Transactional.
 */
@Service
@Transactional
public class ReviewService {

    // Performs CRUD for Review.
    private final ReviewDao reviewDao;
    // DAO for the Reviewer entity, used to set or update the reviewer reference.
    private final ReviewerDao reviewerDao;
    // DAO for the Article entity, used to set or update the article reference.
    private final ArticleDao articleDao;

    /**
     * Constructor injection of DAO.
     * 
     * @param reviewDao   DAO that handles CRUD for Review
     * @param reviewerDao DAO that handles CRUD for Reviewer
     * @param articleDao  DAO that handles CRUD for Article
     */
    public ReviewService(
            ReviewDao reviewDao,
            ReviewerDao reviewerDao,
            ArticleDao articleDao) {
        this.reviewDao = reviewDao;
        this.reviewerDao = reviewerDao;
        this.articleDao = articleDao;
    }

    /* ---------- CRUD ---------- */

    /**
     * Create a new Review from the supplied input.
     *
     * @param input contains the textual content and numeric rating
     * @return the persisted Review entity
     */
    public Review createReview(ReviewInput input) {
        // 1. Build a fresh Review instance.
        Review review = new Review();
        // 2. Copy the title, content, rating and decision from the input.
        review.setTitle(input.title());
        review.setContent(input.content());
        review.setRating(input.rating());
        review.setDecision(input.decision());

        // 3. Resolve foreign keys
        Reviewer reviewer = reviewerDao.findById(Long.valueOf(input.reviewerId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid reviewer id"));
        Article article = articleDao.findById(Long.valueOf(input.articleId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid article id"));

        // 4. Set the relationships on the Review entity.
        review.setReviewer(reviewer);
        review.setArticle(article);

        // 5. Persist the entity via the DAO and return the stored instance.
        return reviewDao.save(review);
    }

    /**
     * Retrieve all reviews stored in the system.
     *
     * @return a List containing every Review entity
     */
    public List<Review> findAll() {
        return reviewDao.findAll();
    }

    /**
     * Find a single review by its primary key.
     *
     * @param id the identifier of the review
     * @return Optional containing the Review if it exists, otherwise empty
     */
    public Optional<Review> findById(Long id) {
        return reviewDao.findById(id);
    }

    /**
     * Apply a partial update (patch) to an existing review.
     * Only the fields present in the patch are modified.
     *
     * @param id    the identifier of the review to update
     * @param patch DTO holding the fields to change
     * @return Optional containing the updated Review, or empty if the review does
     *         not exist
     */
    public Optional<Review> updateReview(Long id, ReviewPatch patch) {
        return reviewDao.findById(id).map(review -> {

            // 1. Update scalar fields if present.
            if (patch.title() != null)
                review.setTitle(patch.title());
            if (patch.content() != null)
                review.setContent(patch.content());
            if (patch.rating() != null)
                review.setRating(patch.rating());

            // 2. Update enum field if present.
            if (patch.decision() != null)
                review.setDecision(patch.decision());

            // 3. Update foreign key relationships if new IDs are supplied.
            if (patch.reviewerId() != null) {
                Reviewer newReviewer = reviewerDao.findById(patch.reviewerId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid reviewer id"));
                review.setReviewer(newReviewer);
            }

            if (patch.articleId() != null) {
                Article newArticle = articleDao.findById(patch.articleId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid article id"));
                review.setArticle(newArticle);
            }

            // 4. Persist the modified Review entity.
            return reviewDao.save(review);
        });
    }

    /**
     * Delete a review identified by its id.
     *
     * @param id the identifier of the review to delete
     * @return true if the review was found and deleted, false otherwise
     */
    public boolean deleteReview(Long id) {
        if (!reviewDao.existsById(id)) {
            // No entity to delete.
            return false;
        }
        reviewDao.deleteById(id);
        // Deletion succeeded.
        return true;
    }

    /**
     * Check whether a Review with the specified ID exists.
     *
     * @param id the primary key to test
     * @return true if a Review with id exists, false otherwise
     */
    public boolean existsById(Long id) {
        return reviewDao.existsById(id);
    }
}
