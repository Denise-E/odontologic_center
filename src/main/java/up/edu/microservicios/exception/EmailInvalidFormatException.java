package up.edu.microservicios.exception;

public class EmailInvalidFormatException extends RuntimeException {
    
    public EmailInvalidFormatException() {
        super();
    }
    
    public EmailInvalidFormatException(String message) {
        super(message);
    }
    
    public EmailInvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EmailInvalidFormatException(Throwable cause) {
        super(cause);
    }
}
