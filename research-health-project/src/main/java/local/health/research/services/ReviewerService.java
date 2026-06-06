package local.health.research.services;

// For mutable sets.
import java.util.HashSet;
// Import collection and optional utilities.
import java.util.List; // For lists of entities.
import java.util.Optional; // For optional values.
import java.util.Set; // For sets.
import java.util.stream.Collectors; // For collecting streams

// Spring stereotypes and transaction support.
import org.springframework.stereotype.Service; // Marks this class as a Spring service.
import jakarta.transaction.Transactional; // Enables transaction support.
import local.health.research.dao.ArticleDao;
import local.health.research.dao.ReviewDao;
import local.health.research.dao.ReviewerDao;
// Input DTO for creating or updating a researcher.
import local.health.research.inputs.ReviewerInput;
import local.health.research.models.Article;
import local.health.research.models.Review;
// Domain entities.
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
public class ReviewerService {

    // CRUD operations for Reviewer.
    private final ReviewerDao reviewerDao;
    // Needed to look up Articles when linking them to a Reviewer.
    private final ArticleDao articleDao;
    // Needed to look up Reviews when linking them to a Reviewer.
    private final ReviewDao reviewDao;

    /**
     * Constructor injection of the DAOs.
     *
     * @param reviewerDao DAO for reviewers
     * @param articleDao  DAO for articles
     * @param reviewDao   DAO for reviews
     */
    public ReviewerService(
            ReviewerDao reviewerDao,
            ArticleDao articleDao,
            ReviewDao reviewDao) {
        this.reviewerDao = reviewerDao;
        this.articleDao = articleDao;
        this.reviewDao = reviewDao;
    }

    /* ---------- CRUD operations ---------- */

    /**
     * Create a new Reviewer from input data.
     *
     * @param input contains the name of the reviewer
     * @return the persisted Reviewer entity
     */
    public Reviewer createReviewer(ReviewerInput input) {
        // 1. Build a new Reviewer entity.
        Reviewer reviewer = new Reviewer();
        reviewer.setTitle(input.title());
        reviewer.setName(input.name());
        reviewer.setAffiliation(input.affiliation());

        // 1.1 Handle the emails set (may be null).
        reviewer.setEmails(
                input.emails() == null
                        ? new HashSet<>()
                        : new HashSet<>(input.emails()));

        // 2. Resolve attended articles.
        Set<Article> articles = input.attendedArticleIds() == null
                ? new HashSet<>()
                : input.attendedArticleIds()
                        .stream()
                        // Fetch each Article by id.
                        .map(articleDao::findById)
                        // Ignore missing ones.
                        .filter(Optional::isPresent)
                        // Unwrap Optional.
                        .map(Optional::get)
                        .collect(Collectors.toSet());
        reviewer.setAttendedArticles(articles);

        // 3. Resolve reviews.
        Set<Review> reviews = new HashSet<>();
        if (input.reviewIds() != null) {
            List<Review> fetched = reviewDao.findAllById(input.reviewIds());
            for (Review review : fetched) {
                // Set the back-reference.
                review.setReviewer(reviewer);
                reviews.add(review);
            }
        }
        reviewer.setReviews(reviews);

        // 4. Persist the new Reviewer (and cascade to related entities).
        return reviewerDao.save(reviewer);
    }

    /**
     * Retrieve all reviewers.
     *
     * @return list of all Reviewer entities
     */
    public List<Reviewer> findAll() {
        return reviewerDao.findAll();
    }

    /**
     * Find a reviewer by its primary key.
     *
     * @param id the identifier of the reviewer
     * @return Optional containing the Reviewer if found, otherwise empty
     */
    public Optional<Reviewer> findById(Long id) {
        return reviewerDao.findById(id);
    }

    /**
     * Update an existing reviewer. Only non-null fields from the input are applied.
     *
     * @param id    id of the reviewer to update
     * @param input new values for the reviewer
     * @return Optional containing the updated Reviewer, or empty if not found
     */
    public Optional<Reviewer> updateReviewer(Long id, ReviewerInput input) {

        // 1. Find the existing Reviewer (or return empty if not found).
        return reviewerDao.findById(id)
                .map(existing -> {

                    // 2. Update simple scalar fields only if provided.
                    if (input.title() != null)
                        existing.setTitle(input.title());
                    if (input.name() != null)
                        existing.setName(input.name());
                    if (input.affiliation() != null)
                        existing.setAffiliation(input.affiliation());

                    // 3. Update the email set if present in the input.
                    if (input.emails() != null) {
                        existing.setEmails(new HashSet<>(input.emails()));
                    }

                    // 4. Update the attended-articles relationship.
                    if (input.attendedArticleIds() != null) {
                        Set<Article> articles = input.attendedArticleIds()
                                .stream()
                                .map(articleDao::findById)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(Collectors.toSet());
                        existing.setAttendedArticles(articles);
                    }

                    // 5. Update the reviews relationship.
                    if (input.reviewIds() != null) {
                        /**
                         * 5.1 If the caller passed an empty list, we want to remove
                         * all reviews from this reviewer. The original code
                         * only handled the "non-null" case but did not treat
                         * an empty list specially.
                         */
                        if (input.reviewIds().isEmpty()) {
                            existing.getReviews()
                                    // Disassociate.
                                    .forEach(review -> review.setReviewer(null));
                            // (orphanRemoval = true).
                            existing.getReviews().clear();
                        } else {
                            /**
                             * 5.2 Detach old reviews - back-reference becomes null
                             * and the orphanRemoval flag will delete them
                             * from the database.
                             */
                            existing.getReviews()
                                    .forEach(review -> review.setReviewer(null));

                            /**
                             * 5.3 Clear the collection so that the new set can be
                             * applied cleanly.
                             */
                            existing.getReviews().clear();

                            /**
                             * 5.4 Fetch the requested reviews by ID and
                             * re-associate them with this reviewer.
                             */
                            List<Review> fetched = reviewDao.findAllById(input.reviewIds());
                            fetched.forEach(review -> review.setReviewer(existing));
                            existing.getReviews().addAll(fetched);
                        }
                    }

                    // 6. Persist the updated Reviewer and return it.
                    return reviewerDao.save(existing);
                });
    }

    /**
     * Delete a reviewer by id.
     *
     * @param id the identifier of the reviewer
     * @return true if deletion succeeded, false if the reviewer does not exist
     */
    public boolean deleteReviewer(Long id) {
        if (!reviewerDao.existsById(id)) {
            // Nothing to delete.
            return false;
        }
        reviewerDao.deleteById(id);
        // Deletion succeeded.
        return true;
    }

    /* ---------- Relationship management ---------- */

    /**
     * Associate an existing article with a reviewer.
     *
     * @param reviewerId id of the reviewer
     * @param articleId  id of the article
     * @return Optional containing the updated Reviewer, or empty if either entity
     *         is missing
     */
    public Optional<Reviewer> addArticle(Long reviewerId, Long articleId) {
        return reviewerDao.findById(reviewerId).flatMap(reviewer -> articleDao.findById(articleId).map(article -> {
            /* Add the article to the reviewer's set */
            reviewer.getAttendedArticles().add(article);

            /* (Optional) keep the relationship bidirectional */
            article.getReviewers().add(reviewer);

            /**
             * Persist the owning side (Reviewer) - the join table
             * entry is updated automatically.
             */
            return reviewerDao.save(reviewer);
        }));
    }

    /**
     * Remove the association between a reviewer and an article.
     *
     * @param reviewerId id of the reviewer
     * @param articleId  id of the article
     * @return Optional containing the updated Reviewer, or empty if either entity
     *         is missing
     */
    public Optional<Reviewer> removeArticle(Long reviewerId, Long articleId) {
        return reviewerDao.findById(reviewerId).flatMap(reviewer -> articleDao.findById(articleId).map(article -> {
            // Remove the article from the reviewer's set.
            reviewer.getAttendedArticles().remove(article);

            // Optional keep the relationship bidirectional.
            article.getReviewers().remove(reviewer);

            return reviewerDao.save(reviewer);
        }));
    }

    /**
     * Check if a reviewer exists by its id.
     *
     * @param id the identifier to check
     * @return true if a reviewer with the given id exists
     */
    public boolean existsById(Long id) {
        return reviewerDao.existsById(id);
    }
}
