package up.edu.microservicios.exception;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe
 * Ejemplos: email duplicado, matrícula duplicada, número de contacto duplicado
 */
public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException() {
        super();
    }
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DuplicateResourceException(Throwable cause) {
        super(cause);
    }
}

