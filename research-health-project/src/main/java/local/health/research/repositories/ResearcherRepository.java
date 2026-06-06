package local.health.research.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import local.health.research.models.Researcher;

/**
 * ResearcherRepository is a Spring Data interface that manages
 * persistence operations for the Researcher entity.
 * By extending JpaRepository<Researcher, Long> it inherits
 * standard CRUD and pagination methods, for example:
 * - save, findById, findAll, delete, etc.
 *
 * The generic parameters declare that the entity type is
 * Researcher and that its primary key is a Long.
 *
 * The @Repository annotation registers the interface as a
 * Spring bean and activates exception translation, turning
 * low-level persistence exceptions into Spring's unified
 * DataAccessException hierarchy.
 *
 * No custom query methods are currently defined, but new
 * methods can be added following Spring Data's naming
 * conventions (e.g., findByLastName).
 */
@Repository
public interface ResearcherRepository extends JpaRepository<Researcher, Long> {

}
