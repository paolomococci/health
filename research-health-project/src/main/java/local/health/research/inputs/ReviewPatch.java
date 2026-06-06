package local.health.research.inputs;

import local.health.research.models.Decision;

/**
 * ReviewPatch is an immutable DTO (Data Transfer Object) that represents a
 * partial update of an existing Review. Unlike ReviewInput, each
 * field may be null - a null value signals that the
 * corresponding property should remain unchanged.
 *
 * As a record, Java automatically supplies:
 * - a constructor that takes content and rating,
 * - accessor methods content() and rating(),
 * - equals(), hashCode() and toString() implementations.
 *
 * Fields:
 * - content : new review text (null = no change)
 * - rating : new Integer score (null = no change)
 *
 * Example usage:
 * ReviewPatch patch = new ReviewPatch("Updated text", null);
 * Only the content will be modified, rating stays as before.
 *
 * The service layer interprets non-null values to modify the
 * Review entity before delegating persistence to the DAO.
 */
public record ReviewPatch(
                String title,
                String content,
                Integer rating,
                Decision decision,
                Long reviewerId,
                Long articleId) {

}
