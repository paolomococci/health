package local.health.research.resolvers;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import local.health.research.exceptions.EntityNotFoundException;
import local.health.research.inputs.ArticleInput;
import local.health.research.inputs.ArticlePatch;
import local.health.research.inputs.ResearcherInput;
import local.health.research.inputs.ResearcherPatch;
import local.health.research.inputs.ReviewInput;
import local.health.research.inputs.ReviewPatch;
import local.health.research.inputs.ReviewerInput;
import local.health.research.models.Article;
import local.health.research.models.Researcher;
import local.health.research.models.Review;
import local.health.research.models.Reviewer;
import local.health.research.services.ArticleService;
import local.health.research.services.ResearcherService;
import local.health.research.services.ReviewService;
import local.health.research.services.ReviewerService;

/**
 * GraphQL resolver for the public API of the health-research application.
 * This class exposes all queries and mutations that can be executed from
 * the GraphQL interface. Each method delegates to the corresponding
 * service and guarantees that the result is either returned or an
 * IllegalArgumentException is thrown if the requested entity is not found.
 */
@Controller
public class ResearchHealthResolver {

    private final ArticleService articleService;
    private final ResearcherService researcherService;
    private final ReviewerService reviewerService;
    private final ReviewService reviewService;

    /**
     * Creates a new instance of the resolver and injects all required
     * service dependencies.
     *
     * @param articleService    the service that manages Article entities
     * @param researcherService the service that manages Researcher entities
     * @param reviewerService   the service that manages Reviewer entities
     * @param reviewService     the service that manages Review entities
     */
    public ResearchHealthResolver(
            ArticleService articleService,
            ResearcherService researcherService,
            ReviewerService reviewerService,
            ReviewService reviewService) {
        this.articleService = articleService;
        this.researcherService = researcherService;
        this.reviewerService = reviewerService;
        this.reviewService = reviewService;
    }

    /* ---------------------------------------------------------------------- */
    /* --------------------------- Queries ---------------------------------- */
    /* ----- read-only operations that return lists or single entities ------ */
    /* ---------------------------------------------------------------------- */

    /**
     * Retrieves all articles stored in the system.
     *
     * @return a list containing every Article in the database
     */
    @QueryMapping
    public List<Article> articles() {
        return articleService.findAll();
    }

    /**
     * Retrieves a single article identified by its id.
     *
     * @param id the unique identifier of the article to fetch
     * @return the Article that matches the supplied id
     */
    @QueryMapping
    public Article article(@Argument Long id) {
        return articleService.findById(id)
                .orElseGet(() -> handleNotFound("Article"));
    }

    /**
     * Retrieves all researchers stored in the system.
     *
     * @return a list containing every Researcher in the database
     */
    @QueryMapping
    public List<Researcher> researchers() {
        return researcherService.findAll();
    }

    /**
     * Retrieves a single researcher identified by its id.
     *
     * @param id the unique identifier of the researcher to fetch
     * @return the Researcher that matches the supplied id
     */
    @QueryMapping
    public Researcher researcher(@Argument Long id) {
        return researcherService.findById(id)
                .orElseGet(() -> handleNotFound("Researcher"));
    }

    /**
     * Retrieves all reviewers stored in the system.
     *
     * @return a list containing every Reviewer in the database
     */
    @QueryMapping
    public List<Reviewer> reviewers() {
        return reviewerService.findAll();
    }

    /**
     * Retrieves a single reviewer identified by its id.
     *
     * @param id the unique identifier of the reviewer to fetch
     * @return the Reviewer that matches the supplied id
     */
    @QueryMapping
    public Reviewer reviewer(@Argument Long id) {
        return reviewerService.findById(id)
                .orElseGet(() -> handleNotFound("Reviewer"));
    }

    /**
     * Retrieves all reviews stored in the system.
     *
     * @return a list containing every Review in the database
     */
    @QueryMapping
    public List<Review> reviews() {
        return reviewService.findAll();
    }

    /**
     * Retrieves a single review identified by its id.
     *
     * @param id the unique identifier of the review to fetch
     * @return the Review that matches the supplied id
     */
    @QueryMapping
    public Review review(@Argument Long id) {
        return reviewService.findById(id)
                .orElseGet(() -> handleNotFound("Review"));
    }

    /* ---------------------------------------------------------------------- */
    /* --------------------------- Mutations -------------------------------- */
    /* -------------- create, update and delete domain entities ------------- */
    /* ---------------------------------------------------------------------- */

    /* -- Article -- */

    /**
     * Creates a new article in the system.
     *
     * @param input the data required to construct the new article
     * @return the newly created Article
     */
    @MutationMapping
    public Article createArticle(@Argument ArticleInput input) {
        setHttpStatus(201);
        return articleService.createArticle(input);
    }

    /**
     * Updates an existing article identified by its id.
     *
     * @param id    the unique identifier of the article to update
     * @param input the new values to apply to the article
     * @return the updated Article
     * @throws IllegalArgumentException if no article with the given id exists
     */
    @MutationMapping
    public Article updateArticle(@Argument Long id, @Argument ArticlePatch input) {
        return articleService.updateArticle(id, input)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
    }

    /**
     * Deletes an article identified by its id.
     *
     * @param id the unique identifier of the article to delete
     * @return true if the article was successfully deleted; false otherwise
     */
    @MutationMapping
    public boolean deleteArticle(@Argument Long id) {
        return handleDelete(
                () -> articleService.existsById(id),
                articleService::deleteArticle,
                id,
                "Article");
    }

    /* -- Researcher -- */

    /**
     * Creates a new researcher in the system.
     *
     * @param input the data required to construct the new researcher
     * @return the newly created Researcher
     */
    @MutationMapping
    public Researcher createResearcher(@Argument ResearcherInput input) {
        setHttpStatus(201);
        return researcherService.createResearcher(input);
    }

    /**
     * Updates an existing researcher identified by its id.
     *
     * @param id    the unique identifier of the researcher to update
     * @param input the new values to apply to the researcher
     * @return the updated Researcher
     * @throws IllegalArgumentException if no researcher with the given id exists
     */
    @MutationMapping
    public Researcher updateResearcher(@Argument Long id, @Argument ResearcherPatch patch) {
        return researcherService.updateResearcher(id, patch)
                .orElseThrow(() -> new IllegalArgumentException("Researcher not found"));
    }

    /**
     * Deletes a researcher identified by its id.
     *
     * @param id the unique identifier of the researcher to delete
     * @return true if the researcher was successfully deleted; false otherwise
     */
    @MutationMapping
    public boolean deleteResearcher(@Argument Long id) {
        return handleDelete(
                () -> researcherService.existsById(id),
                researcherService::deleteResearcher,
                id,
                "Researcher");
    }

    /* -- Reviewer -- */

    /**
     * Creates a new reviewer in the system.
     *
     * @param input the data required to construct the new reviewer
     * @return the newly created Reviewer
     */
    @MutationMapping
    public Reviewer createReviewer(@Argument ReviewerInput input) {
        setHttpStatus(201);
        return reviewerService.createReviewer(input);
    }

    /**
     * Updates an existing reviewer identified by its id.
     *
     * @param id    the unique identifier of the reviewer to update
     * @param input the new values to apply to the reviewer
     * @return the updated Reviewer
     * @throws IllegalArgumentException if no reviewer with the given id exists
     */
    @MutationMapping
    public Reviewer updateReviewer(@Argument Long id, @Argument ReviewerInput input) {
        return reviewerService.updateReviewer(id, input)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));
    }

    /**
     * Deletes a reviewer identified by its id.
     *
     * @param id the unique identifier of the reviewer to delete
     * @return true if the reviewer was successfully deleted; false otherwise
     */
    @MutationMapping
    public boolean deleteReviewer(@Argument Long id) {
        return handleDelete(
                () -> reviewerService.existsById(id),
                reviewerService::deleteReviewer,
                id,
                "Reviewer");
    }

    /* -- Review -- */

    /**
     * Creates a new review in the system.
     *
     * @param input the data required to construct the new review
     * @return the newly created Review
     */
    @MutationMapping
    public Review createReview(@Argument ReviewInput input) {
        setHttpStatus(201);
        return reviewService.createReview(input);
    }

    /**
     * Updates an existing review identified by its id.
     *
     * @param id    the unique identifier of the review to update
     * @param patch the changes to apply to the review
     * @return the updated Review
     * @throws IllegalArgumentException if no review with the given id exists
     */
    @MutationMapping
    public Review updateReview(@Argument Long id, @Argument ReviewPatch patch) {
        return reviewService.updateReview(id, patch)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }

    /**
     * Deletes a review identified by its id.
     *
     * @param id the unique identifier of the review to delete
     * @return true if the review was successfully deleted; false otherwise
     */
    @MutationMapping
    public boolean deleteReview(@Argument Long id) {
        return handleDelete(
                () -> reviewService.existsById(id),
                reviewService::deleteReview,
                id,
                "Review");
    }

    /* -- Relationship Mutations -- */

    /**
     * Assigns a article to a researcher, establishing a professional
     * relationship between the two entities. After this operation,
     * the article is considered to be a peer of the researcher.
     *
     * @param researcherId the unique identifier of the researcher
     * @param articleId    the unique identifier of the article
     * @return true if the assignment was successful; false otherwise
     * @throws IllegalArgumentException if either the researcher or the article does
     *                                  not exist
     */
    @MutationMapping
    public Researcher addArticleToResearcher(@Argument Long researcherId, @Argument Long articleId) {
        return researcherService.addArticle(researcherId, articleId)
                .orElseThrow(() -> new IllegalArgumentException("Researcher or Article not found"));
    }

    /**
     * Removes a article from a researcher. After this operation, the
     * article is no longer considered a peer of the researcher.
     *
     * @param researcherId the unique identifier of the researcher
     * @param articleId    the unique identifier of the article
     * @return true if the removal was successful; false otherwise
     * @throws IllegalArgumentException if either the researcher or the article does
     *                                  not exist
     */
    @MutationMapping
    public Researcher removeArticleFromResearcher(@Argument Long researcherId, @Argument Long articleId) {
        return researcherService.removeArticle(researcherId, articleId)
                .orElseThrow(() -> new IllegalArgumentException("Researcher or Article not found"));
    }

    /**
     * Associates a reviewer with a specific article. After this
     * operation, the reviewer is considered as having reviewed the article.
     *
     * @param reviewerId the unique identifier of the reviewer
     * @param articleId  the unique identifier of the article to review
     * @return true if the association was created; false otherwise
     * @throws IllegalArgumentException if either the reviewer or the article does
     *                                  not exist
     */
    @MutationMapping
    public Reviewer addArticleToReviewer(@Argument Long reviewerId, @Argument Long articleId) {
        return reviewerService.addArticle(reviewerId, articleId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer or Article not found"));
    }

    /**
     * Removes the association between a reviewer and an article, effectively
     * deleting the review that the reviewer made on the article.
     *
     * @param reviewerId the unique identifier of the reviewer
     * @param articleId  the unique identifier of the article
     * @return true if the association was removed; false otherwise
     * @throws IllegalArgumentException if either the reviewer or the article does
     *                                  not exist
     */
    @MutationMapping
    public Reviewer removeArticleFromReviewer(@Argument Long reviewerId, @Argument Long articleId) {
        return reviewerService.removeArticle(reviewerId, articleId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer or Article not found"));
    }

    /* --------------------------------------------------------------------- */
    /* ------------------------------ Helpers ------------------------------ */
    /* ---------- internal logic for setting HTTP status codes and --------- */
    /* -------------- handling not-found and delete scenarios -------------- */
    /* --------------------------------------------------------------------- */

    /**
     * Stores the desired HTTP status code in the GraphQL request context.
     *
     * @param status the HTTP status code to associate with the current request
     */
    private void setHttpStatus(int status) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            attrs.setAttribute("graphql.httpStatus", status, RequestAttributes.SCOPE_REQUEST);
        }
    }

    /**
     * Handles the case where an entity is not found.
     *
     * @param entityName the name of the entity that was requested
     * @return always returns null so that the calling method can
     *         propagate a 404 response
     */
    private <T> T handleNotFound(String entityName) {

        // Mark the request as not found.
        setHttpStatus(404);
        // Return null so that the GraphQL framework can produce a 404.
        return null;
    }

    /**
     * Performs a deletion while correctly setting HTTP status codes for the
     * various outcomes:
     * 
     * 204 - if the deletion succeeds.
     * 404 - if the entity does not exist.
     * 500 - for any unexpected internal error.
     * 
     *
     * @param existsSupplier supplies true if the entity exists
     * @param deleteConsumer consumes the entity id to delete it
     * @param id             the id of the entity to delete
     * @param entityName     the human-readable name of the entity (used in logs)
     * @return true if the entity was deleted, false otherwise
     */
    private boolean handleDelete(
            Supplier<Boolean> existsSupplier,
            Consumer<Long> deleteConsumer,
            Long id,
            String entityName) {
        // Verify that the entity exists before attempting deletion.
        if (!existsSupplier.get()) {
            // Not Found
            setHttpStatus(404);
            return false;
        }
        try {
            // Attempt deletion.
            deleteConsumer.accept(id);
            // Successful deletion - no content.
            setHttpStatus(204);
            return true;
        } catch (EntityNotFoundException e) {
            // Entity was missing between the existence check and deletion.
            setHttpStatus(404);
            return false;
        } catch (Exception e) {
            // A generic unexpected error occurred - Internal Server Error.
            setHttpStatus(500);
            return false;
        }
    }
}
