package exceptions;

// Thrown when an entity with the same ID already exists
public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}