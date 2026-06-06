package local.health.research.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import local.health.research.models.Reviewer;

/**
 * ReviewerRepository is a Spring Data interface that provides
 * CRUD and pagination support for the Reviewer entity.
 *
 * Extending JpaRepository<Reviewer, Long> automatically
 * gives the repository methods such as:
 * - save, findById, findAll, delete, etc.
 *
 * The generic parameters specify that the entity type is
 * Reviewer and its primary key type is Long.
 *
 * The @Repository annotation marks this interface as a Spring
 * bean and enables translation of persistence exceptions
 * into Spring's DataAccessException hierarchy.
 *
 * No custom query methods are defined here, but new ones
 * can be added using Spring Data's query derivation
 * conventions if needed.
 */
@Repository
public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {

}
