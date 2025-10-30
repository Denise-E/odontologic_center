package up.edu.microservicios.exception;

/**
 * Excepción lanzada cuando hay un conflicto de turnos
 * Ejemplos: odontólogo ocupado, paciente ya tiene turno en ese horario
 */
public class AppointmentConflictException extends RuntimeException {
    
    public AppointmentConflictException() {
        super();
    }
    
    public AppointmentConflictException(String message) {
        super(message);
    }
    
    public AppointmentConflictException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AppointmentConflictException(Throwable cause) {
        super(cause);
    }
}

