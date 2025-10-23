package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Domicilio;

public interface DomicilioRepository extends JpaRepository<Domicilio, Integer> {
}

