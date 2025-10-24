package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Turno;

import java.util.List;

// Jpa = Java Persistent APi
public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    // JpaRepository ya provee:
    // - findById(Integer id): Optional<Turno>
    // - findAll(): List<Turno>
    // - save(Turno entity): Turno
    // - deleteById(Integer id): void
    
    // Métodos personalizados para buscar turnos
    List<Turno> findByPacienteId(Integer pacienteId);
    List<Turno> findByOdontologoId(Integer odontologoId);
}
