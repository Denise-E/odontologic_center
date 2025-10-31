package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Turno;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {

    
    // Métodos personalizados para buscar turnos
    List<Turno> findByPacienteId(Integer pacienteId);
    List<Turno> findByOdontologoId(Integer odontologoId);
    
    // Métodos para detectar conflictos de turnos
    Optional<Turno> findByOdontologoIdAndFecha(Integer odontologoId, LocalDate fecha);
    Optional<Turno> findByPacienteIdAndFecha(Integer pacienteId, LocalDate fecha);
}
