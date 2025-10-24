package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Turno;

import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {

    
    // Métodos personalizados para buscar turnos
    List<Turno> findByPacienteId(Integer pacienteId);
    List<Turno> findByOdontologoId(Integer odontologoId);
}
