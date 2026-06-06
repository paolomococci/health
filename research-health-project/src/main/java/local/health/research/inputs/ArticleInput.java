package local.health.research.inputs;

import java.time.LocalDate;
import java.util.List;

/**
 * ArticleInput is a lightweight, immutable DTO used when
 * a client submits data to create or update an Article.
 * Java 16+ records automatically provide:
 * - A canonical constructor with all fields,
 * - accessor methods (title(), subject(), …),
 * - equals(), hashCode(), and toString() implementations.
 *
 * Each field represents a property of the article that
 * is relevant to the caller, along with the IDs of
 * associated Researchers and Reviewers.
 */
public record ArticleInput(
        String title,
        String subject,
        String content,
        LocalDate publishedDate,
        List<Long> researcherIds,
        List<Long> reviewerIds) {

}
