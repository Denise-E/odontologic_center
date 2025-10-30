package up.edu.microservicios.exception;

public class ResourceNotFoundException extends RuntimeException {
    // Lógica del manejo de la excepción
    
    public ResourceNotFoundException() {
        super();
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
