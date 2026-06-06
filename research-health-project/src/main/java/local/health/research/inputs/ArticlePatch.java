package local.health.research.inputs;

import java.time.LocalDate;
import java.util.List;

/**
 * ArticlePatch is a DTO (Data Transfer Object) that represents a *partial*
 * update of an Article.
 * Unlike ArticleInput, the fields here are allowed to be null - a null value
 * signals that the corresponding property should be left unchanged.
 *
 * Java 16+ records give us a concise immutable class with:
 * - A constructor that accepts all fields,
 * - accessor methods (title(), subject(), …),
 * - equals(), hashCode() and toString() automatically generated.
 *
 * Each property matches the same semantic meaning used in ArticleInput:
 * - title, subject, content, publishedDate,
 * - researcherIds - list of researcher IDs to associate,
 * - reviewerIds - list of reviewer IDs to associate.
 *
 * Example usage:
 * ArticlePatch patch = new ArticlePatch(null, "New subject", null,
 * null, List.of(3L), null);
 * Only the subject and reviewer associations would be modified.
 */
public record ArticlePatch(
                String title,
                String subject,
                String content,
                LocalDate publishedDate,
                List<Long> researcherIds,
                List<Long> reviewerIds) {

}
