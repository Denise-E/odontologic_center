package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Paciente;

import java.util.Optional;

// Jpa = Java Persistent APi
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Optional<Paciente> findByEmail(String email);
    Optional<Paciente> findByNumeroContacto(String numeroContacto);
}
