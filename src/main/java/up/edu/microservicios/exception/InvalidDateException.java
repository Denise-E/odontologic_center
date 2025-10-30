package up.edu.microservicios.exception;

/**
 * Excepción lanzada cuando una fecha no es válida para un turno
 * Ejemplos: fecha pasada, día no laborable, fecha null
 */
public class InvalidDateException extends RuntimeException {
    
    public InvalidDateException() {
        super();
    }
    
    public InvalidDateException(String message) {
        super(message);
    }
    
    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidDateException(Throwable cause) {
        super(cause);
    }
}

