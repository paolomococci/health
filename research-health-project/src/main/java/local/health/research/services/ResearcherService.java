package local.health.research.services;

// For mutable sets.
import java.util.HashSet;
// Import collection and optional utilities.
import java.util.List; // For lists of entities.
import java.util.Optional; // For optional values.
import java.util.Set; // For sets.

// Spring stereotypes and transaction support.
import org.springframework.stereotype.Service; // Marks this class as a Spring service.
import jakarta.transaction.Transactional; // Enables transaction support.
import local.health.research.dao.ArticleDao;
import local.health.research.dao.ResearcherDao;
// Input DTO for creating a researcher.
import local.health.research.inputs.ResearcherInput;
// Input DTO for updating a researcher.
import local.health.research.inputs.ResearcherPatch;
// Domain models.
import local.health.research.models.Article;
import local.health.research.models.Researcher;

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
public class ResearcherService {

    // DAO to perform CRUD operations on Researcher.
    private final ResearcherDao researcherDao;
    // DAO to load Article entities when needed.
    private final ArticleDao articleDao;

    /**
     * Constructor injection of the two DAOs.
     *
     * @param researcherDao DAO for researchers
     * @param articleDao    DAO for articles
     */
    public ResearcherService(
            ResearcherDao researcherDao,
            ArticleDao articleDao) {
        this.researcherDao = researcherDao;
        this.articleDao = articleDao;
    }

    /* ---------- CRUD ---------- */

    /**
     * Create a new researcher using data from a ResearcherInput DTO.
     *
     * @param input the data used to build the new researcher
     * @return the persisted researcher entity
     */
    public Researcher createResearcher(ResearcherInput input) {
        var researcher = new Researcher();

        // 1. Populate the simple (non-relational) fields.
        researcher.setName(input.name());
        researcher.setTitle(input.title());
        researcher.setAffiliation(input.affiliation());

        if (input.emails() != null) {
            researcher.setEmails(new HashSet<>(input.emails()));
        }

        // 2. Handle the many-to-many relation with Article, if any ids were supplied.
        if (input.articleIds() != null && !input.articleIds().isEmpty()) {
            Set<Article> articleSet = new HashSet<>();

            for (Long articleId : input.articleIds()) {
                articleDao.findById(articleId).ifPresent(article -> {
                    // Add the article to the researcher's collection.
                    articleSet.add(article);
                    // Maintain bidirectional consistency (optional but recommended).
                    article.getResearchers().add(researcher);
                });
            }

            researcher.setArticles(articleSet);
        } else {
            // We guarantee that the attribute is never null.
            researcher.setArticles(new HashSet<>());
        }

        // 3. Persist the new researcher; the transaction ensures atomicity.
        return researcherDao.save(researcher);
    }

    /**
     * Retrieve all researchers from the database.
     *
     * @return list of all researchers
     */
    public List<Researcher> findAll() {
        return researcherDao.findAll();
    }

    /**
     * Find a researcher by its primary key.
     *
     * @param id the researcher's id
     * @return an Optional containing the researcher if found, otherwise empty
     */
    public Optional<Researcher> findById(Long id) {
        return researcherDao.findById(id);
    }

    /**
     * Update an existing researcher. Only non-null fields from the patch are
     * applied.
     *
     * @param id    the id of the researcher to update
     * @param patch the new values for the researcher
     * @return an Optional containing the updated researcher, or empty if not found
     */
    public Optional<Researcher> updateResearcher(Long id, ResearcherPatch patch) {
        return researcherDao.findById(id).map(researcher -> {
            // 1. Update simple (primitive) fields only if new values are supplied.
            if (patch.name() != null)
                researcher.setName(patch.name());
            if (patch.affiliation() != null)
                researcher.setAffiliation(patch.affiliation());
            if (patch.emails() != null)
                researcher.setEmails(new HashSet<>(patch.emails()));
            if (patch.title() != null)
                researcher.setTitle(patch.title());

            // 2. Replace the current article set with the new set specified in the patch.
            if (patch.articleIds() != null) {
                Set<Article> newArticles = new HashSet<>();
                for (Long articleId : patch.articleIds()) {
                    Article a = articleDao.findById(articleId)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Article with id " + articleId + " not found"));
                    newArticles.add(a);
                }
                researcher.setArticles(newArticles);
            }

            // 3. Persist the updated researcher.
            return researcherDao.save(researcher);
        });
    }

    /**
     * Delete a researcher by id.
     *
     * @param id the researcher's id
     * @return true if deletion succeeded, false if no such researcher exists
     */
    public boolean deleteResearcher(Long id) {
        if (!researcherDao.existsById(id)) {
            // No record to delete.
            return false;
        }
        researcherDao.deleteById(id);
        // Deletion succeeded.
        return true;
    }

    /* ---------- Relationship management ---------- */

    /**
     * Associate an existing article with a researcher.
     *
     * @param researcherId id of the researcher
     * @param articleId    id of the article
     * @return Optional containing the updated researcher, or empty if either entity
     *         is missing
     */
    public Optional<Researcher> addArticle(Long researcherId, Long articleId) {
        // 1. Fetch both entities.
        Optional<Researcher> optResearcher = researcherDao.findById(researcherId);
        Optional<Article> optArticle = articleDao.findById(articleId);

        // 2. If both are present, update the many-to-many association.
        return optResearcher.flatMap(researcher -> optArticle.map(article -> {
            researcher.getArticles().add(article);

            // 3. Keep bidirectional consistency (optional).
            article.getResearchers().add(researcher);

            return researcherDao.save(researcher);
        }));
    }

    /**
     * Remove the association between a researcher and an article.
     *
     * @param researcherId id of the researcher
     * @param articleId    id of the article
     * @return Optional containing the updated researcher, or empty if either
     *         entity does not exist
     */
    public Optional<Researcher> removeArticle(Long researcherId, Long articleId) {
        Optional<Researcher> optResearcher = researcherDao.findById(researcherId);
        Optional<Article> optArticle = articleDao.findById(articleId);

        return optResearcher.flatMap(researcher -> optArticle.map(article -> {
            researcher.getArticles().remove(article);
            // Keep bidirectional consistency (optional).
            article.getResearchers().remove(researcher);
            return researcherDao.save(researcher);
        }));
    }

    /**
     * Determine whether a researcher with the given identifier exists.
     *
     * @param id the identifier to check
     * @return true if a researcher exists, false otherwise
     */
    public boolean existsById(Long id) {
        return researcherDao.existsById(id);
    }
}
