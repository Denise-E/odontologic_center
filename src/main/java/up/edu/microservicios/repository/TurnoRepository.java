package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Turno;

// Jpa = Java Persistent APi
public interface TurnoRepository extends JpaRepository<Turno, Integer> {

}
