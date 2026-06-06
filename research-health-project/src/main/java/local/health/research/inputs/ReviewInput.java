package local.health.research.inputs;

import local.health.research.models.Decision;

/**
 * ReviewInput is an immutable DTO that represents the data a
 * client must supply when creating or updating a Review.
 *
 * As a record, Java automatically generates:
 * - a constructor accepting content and rating,
 * - accessor methods content() and rating(),
 * - equals(), hashCode() and toString() implementations.
 *
 * Fields:
 * - content : the textual body of the review
 * - rating : an Integer score (e.g., 1-5) that quantifies the
 * review's evaluation of the article.
 *
 * The service layer can use this record to construct a Review
 * entity (assigning an ID, associating it with an Article and
 * a Reviewer, etc.) and then persist it via the DAO layer.
 */
public record ReviewInput(
        String title,
        String content,
        Integer rating,
        Decision decision,
        Long  reviewerId,
        Long  articleId) {

}
