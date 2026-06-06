package local.health.research.inputs;

import java.util.List;

/**
 * ResearcherInput is a tiny immutable DTO (Data Transfer Object) used when a
 * client wants to create or update a Researcher entity.
 *
 * The record automatically supplies:
 * - a constructor that takes the name,
 * - an accessor method name(),
 * - equals(), hashCode() and toString() implementations.
 *
 * Only the name field is required - any other attributes of a
 * Researcher (e.g., id, associated articles, etc.) are managed
 * by the service layer and the persistence layer.
 */
public record ResearcherInput(
                String name,
                List<String> emails,
                String title,
                String affiliation,
                List<Long> articleIds) {

}
