package up.edu.microservicios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.edu.microservicios.entity.Odontologo;

// Jpa = Java Persistent APi
public interface OdontologoRepository extends JpaRepository<Odontologo, Integer> {

}
