package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Turno;

// Jpa = Java Persistent APi
public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    // JpaRepository ya provee:
    // - findById(Integer id): Optional<Turno>
    // - findAll(): List<Turno>
    // - save(Turno entity): Turno
    // - deleteById(Integer id): void
}
