package local.health.research.inputs;

import java.util.List;

/**
 * ReviewerInput is an immutable DTO that represents the minimal
 * data required to create or update a Reviewer entity.
 *
 * Because it's a record, Java automatically generates:
 * - a constructor that accepts the name,
 * - a getter named name(),
 * - equals(), hashCode() and toString() implementations.
 *
 * The service layer can use this record to build a Reviewer
 * entity (assigning an ID, linking to articles, etc.) and then
 * persist it via the DAO.
 */
public record ReviewerInput(
        String title,
        String name,
        String affiliation,
        List<String> emails,
        List<Long> attendedArticleIds,
        List<Long> reviewIds) {

}
