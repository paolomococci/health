package local.health.research.models;

/**
 * Represents the possible outcomes that a Reviewer can
 * give when evaluating a Review. The enum is deliberately
 * small - it mirrors the three standard decisions used in many
 * peer-review workflows:
 * - ACCEPT, the article is accepted as-is (or with minor corrections).
 * - REVISION, the article needs substantive changes before it can be considered
 * for publication.
 * - REJECT, the article is not suitable for publication in its current form.
 * 
 * The enum is stored in the database as a simple string value
 * (e.g., "ACCEPT") because it is annotated with
 * 
 * @Enumerated(EnumType.STRING) in the owning entity
 *                              (likely local.health.research.models.Review).
 *                              This makes
 *                              the data easy to read and maintain - adding or
 *                              renaming a value
 *                              does not shift the column order or cause type
 *                              mismatches.
 */
public enum Decision {
  // The manuscript meets the journal's standards and can be published without
  // further changes.
  ACCEPT,
  // The manuscript requires additional work or clarification before it can be
  // accepted. Reviewers typically provide detailed comments to guide the authors.
  REVISION,
  // The manuscript is not suitable for publication in its current form. Reasons
  // may include lack of novelty, methodological flaws, or insufficient data.
  REJECT
}
