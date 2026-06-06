package local.health.research.services;

// Mutable set implementation.
import java.util.HashSet;
// Import collection and optional utilities.
import java.util.List; // List of entities.
import java.util.Optional; // Optional wrapper for nullable returns.

// Spring stereotypes and transaction support.
import org.springframework.stereotype.Service; // Marks this class as a Spring service.
import jakarta.transaction.Transactional; // Enables declarative transaction handling.
import local.health.research.dao.ArticleDao;
import local.health.research.dao.ResearcherDao;
import local.health.research.dao.ReviewerDao;
// Input DTO for creating or updating a researcher.
import local.health.research.inputs.ArticleInput; // DTO used to create an Article.
import local.health.research.inputs.ArticlePatch; // DTO used to partially update an Article.

// Domain models.
import local.health.research.models.Article; // Article domain entity.
import local.health.research.models.Researcher; // Researcher domain entity.
import local.health.research.models.Reviewer; // Reviewer domain entity.

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
public class ArticleService {

    // DAO for Article entities.
    private final ArticleDao articleDao;
    // DAO for Researcher entities.
    private final ResearcherDao researcherDao;
    // DAO for Reviewer entities.
    private final ReviewerDao reviewerDao;

    /**
     * Create a new ArticleService.
     *
     * @param articleDao    DAO used to read and write Article objects.
     * @param researcherDao DAO used to load Researcher objects.
     * @param reviewerDao   DAO used to load Reviewer objects.
     */
    public ArticleService(ArticleDao articleDao,
            ResearcherDao researcherDao,
            ReviewerDao reviewerDao) {
        this.articleDao = articleDao;
        this.researcherDao = researcherDao;
        this.reviewerDao = reviewerDao;
    }

    /* ---------- CRUD operations ---------- */

    /**
     * Persist a new Article using the supplied input data.
     *
     * @param input an ArticleInput that holds the values for the new article.
     * @return the persisted Article instance.
     */
    public Article createArticle(ArticleInput input) {

        // 1. Create a new Article instance and populate its scalar fields.
        Article article = new Article();
        article.setTitle(input.title());
        article.setSubject(input.subject());
        article.setContent(input.content());
        article.setPublishedDate(input.publishedDate());

        // 2. Persist the article first so it gets an ID and becomes managed.
        articleDao.save(article);

        // 3. Attach Researchers to the article.
        if (input.researcherIds() != null && !input.researcherIds().isEmpty()) {
            // Load all Researchers referenced by the input IDs.
            List<Researcher> researchers = researcherDao.findAllById(input.researcherIds());

            // Verify that every requested ID exists.
            if (researchers.size() != input.researcherIds().size()) {
                throw new IllegalArgumentException(
                        "One or more Researcher IDs do not exist: " + input.researcherIds());
            }

            // Add the article to each Researcher's collection.
            researchers.forEach(researcher -> researcher.getArticles().add(article));

            // Keep the owning side Article in sync.
            article.setResearchers(new HashSet<>(researchers));
        }

        // 4. Attach Reviewers to the article.
        if (input.reviewerIds() != null && !input.reviewerIds().isEmpty()) {
            // Load all Reviewers referenced by the input IDs.
            List<Reviewer> reviewers = reviewerDao.findAllById(input.reviewerIds());

            // Verify that every requested ID exists.
            if (reviewers.size() != input.reviewerIds().size()) {
                throw new IllegalArgumentException(
                        "One or more Reviewer IDs do not exist: " + input.reviewerIds());
            }

            // Add the article to each Reviewer's collection.
            reviewers.forEach(reviewer -> reviewer.getAttendedArticles().add(article));

            // Keep the owning side Article in sync.
            article.setReviewers(new HashSet<>(reviewers));
        }

        // 5. Return the article, all changes will be flushed when the transaction
        // commits.
        return article;
    }

    /**
     * Return every Article stored in the database.
     *
     * @return a list of all Article objects.
     */
    public List<Article> findAll() {
        return articleDao.findAll();
    }

    /**
     * Find a single Article by its primary key.
     *
     * @param id the Article id to search for.
     * @return an Optional containing the Article if found, or empty if not.
     */
    public Optional<Article> findById(Long id) {
        return articleDao.findById(id);
    }

    /**
     * Update an existing Article.
     * Only the fields that are not null in the patch are changed.
     *
     * @param id    the id of the Article to update.
     * @param patch an ArticlePatch containing the new values.
     * @return an Optional with the updated Article, or throws if not found.
     */
    public Optional<Article> updateArticle(Long id, ArticlePatch patch) {
        // Delegate the whole update logic to the DAO.
        return articleDao.update(id, patch);
    }

    /**
     * Delete an Article by its id.
     *
     * @param id the Article id to delete.
     * @return true if the Article existed and was removed, false otherwise.
     */
    public boolean deleteArticle(Long id) {
        return articleDao.findById(id).map(article -> {
            // 1. Remove many-to-many relationships.
            if (article.getResearchers() != null) {
                article.getResearchers().clear();
            }
            if (article.getReviewers() != null) {
                article.getReviewers().clear();
            }

            // 2. Remove dependent entities.
            if (article.getReviews() != null) {
                article.getReviews().clear();
            }

            // 3. Delete the Article entity.
            articleDao.delete(article);
            return true;
        }).orElse(false);
    }

    /* ---------- Relationship management ---------- */

    /**
     * Attach a Researcher to an Article.
     *
     * @param articleId    the id of the Article.
     * @param researcherId the id of the Researcher to add.
     * @return the updated Article.
     */
    public Article addResearcher(Long articleId, Long researcherId) {
        // Load the Article, throw if it does not exist.
        Article article = articleDao.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        // Load the Researcher, throw if it does not exist.
        Researcher researcher = researcherDao.findById(researcherId)
                .orElseThrow(() -> new IllegalArgumentException("Researcher not found"));

        // Add the association on the owning side.
        article.getResearchers().add(researcher);
        return articleDao.save(article);
    }

    /**
     * Detach a Researcher from an Article.
     *
     * @param articleId    the id of the Article.
     * @param researcherId the id of the Researcher to remove.
     * @return the updated Article.
     */
    public Article removeResearcher(Long articleId, Long researcherId) {
        Article article = articleDao.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        Researcher researcher = researcherDao.findById(researcherId)
                .orElseThrow(() -> new IllegalArgumentException("Researcher not found"));

        // Remove the association.
        article.getResearchers().remove(researcher);
        return articleDao.save(article);
    }

    /**
     * Attach a Reviewer to an Article.
     *
     * @param articleId  the id of the Article.
     * @param reviewerId the id of the Reviewer to add.
     * @return the updated Article.
     */
    public Article addReviewer(Long articleId, Long reviewerId) {
        Article article = articleDao.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        Reviewer reviewer = reviewerDao.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));

        article.getReviewers().add(reviewer);
        return articleDao.save(article);
    }

    /**
     * Detach a Reviewer from an Article.
     *
     * @param articleId  the id of the Article.
     * @param reviewerId the id of the Reviewer to remove.
     * @return the updated Article.
     */
    public Article removeReviewer(Long articleId, Long reviewerId) {
        Article article = articleDao.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        Reviewer reviewer = reviewerDao.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));

        article.getReviewers().remove(reviewer);
        return articleDao.save(article);
    }

    /**
     * Check if an Article exists by its id.
     *
     * @param id the Article id to check.
     * @return true if a Article with the given id exists.
     */
    public boolean existsById(Long id) {
        return articleDao.existsById(id);
    }
}
