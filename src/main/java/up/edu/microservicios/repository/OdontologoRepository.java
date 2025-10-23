package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Odontologo;

import java.util.Optional;

// Jpa = Java Persistent APi
public interface OdontologoRepository extends JpaRepository<Odontologo, Integer> {
    Optional<Odontologo> findByMatricula(String matricula);
}
