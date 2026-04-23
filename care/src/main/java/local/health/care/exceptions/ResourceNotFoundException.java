package local.health.care.exceptions;

/**
 * Custom unchecked exception that signals a requested resource could not be
 * found. Extending RuntimeException allows it to be thrown without mandatory
 * catch or declare clauses, which is common for REST error handling.
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Constructor that accepts a descriptive error message.
   * 
   * @param message the detail message explaining why the resource was not found.
   */
  public ResourceNotFoundException(String message) {
    // Pass the message to the superclass constructor.
    super(message);
  }
}
