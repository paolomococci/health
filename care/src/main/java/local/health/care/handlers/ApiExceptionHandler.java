package local.health.care.handlers;

import local.health.care.exceptions.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @RestControllerAdvice registers this class as a global exception handler for
 *                       all controllers in the application context.
 *                       Each method annotated with @ExceptionHandler will
 *                       intercept the corresponding exception type and produce
 *                       an appropriate HTTP response.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

  /**
   * Handles ResourceNotFoundException (e.g., when a patient ID cannot be found).
   * Returns a 404 NOT FOUND status with a simple ErrorResponse body containing
   * the exception message.
   * 
   * @param ex the thrown ResourceNotFoundException
   * @return ResponseEntity with HTTP 404 and an error payload
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> notFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Handles validation errors thrown by Spring MVC when a @Valid annotated DTO
   * fails its constraints.
   * It collects all field-level errors, formats them as "field: message",
   * concatenates them with commas, and returns a 400 BAD REQUEST with the
   * aggregated message.
   * 
   * @param ex the MethodArgumentNotValidException containing binding errors
   * @return ResponseEntity with HTTP 400 and an error payload
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .collect(Collectors.joining(", "));
    return ResponseEntity.badRequest().body(new ErrorResponse(msg));
  }

  /**
   * Catches any other Exception that was not handled by a more specific method.
   * The stack trace is printed to the console (useful for debugging in a
   * development environment) and a generic 500 INTERNAL SERVER ERROR response is
   * sent to the client without exposing internal details.
   * 
   * @param ex the unhandled Exception
   * @return ResponseEntity with HTTP 500 and a generic error payload
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> generic(Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal server error"));
  }

  /**
   * A simple static inner class that represents the JSON body returned for all
   * error responses.
   */
  static class ErrorResponse {
    public ErrorResponse(String message) {
    }
  }
}
