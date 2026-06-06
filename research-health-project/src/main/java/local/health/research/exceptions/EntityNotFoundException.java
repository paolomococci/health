package local.health.research.exceptions;

/**
 * EntityNotFoundException is a custom runtime exception used by the DAO layer
 * to signal that a requested entity could not be located in the database.
 * It stores the name of the entity type and the id that was searched for,
 * which can be useful for debugging and for generating client-facing error
 * messages in a GraphQL API.
 */
public class EntityNotFoundException extends RuntimeException {

    // The simple name of the entity type that was not found.
    private final String entityName;
    // The primary key value that was used in the lookup.
    private final Long id;

    /**
     * Constructor.
     * 
     * @param entityName the class name or alias of the missing entity
     * @param id         the id that was searched for
     * 
     *                   The exception message is built to read like:
     *                   User with id 42 not found
     */
    public EntityNotFoundException(String entityName, Long id) {
        super(entityName + " with id " + id + " not found");
        this.entityName = entityName;
        this.id = id;
    }

    /**
     * Retrieve the name of the entity type that was not found.
     * 
     * @return the entity name passed to the constructor
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Retrieve the id that was searched for.
     * 
     * @return he id value passed to the constructor
     */
    public Long getId() {
        return id;
    }
}
