package exceptions;

// Thrown when a requested entity cannot be found
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}