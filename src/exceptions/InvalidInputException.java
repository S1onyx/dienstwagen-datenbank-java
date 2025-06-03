package exceptions;

// Thrown when input format or value is invalid
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}