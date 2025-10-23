package up.edu.microservicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import up.edu.microservicios.entity.Domicilio;
import org.springframework.stereotype.Service;
import up.edu.microservicios.repository.DomicilioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DomicilioService {
    @Autowired
    private DomicilioRepository domicilioRepository;

    public Domicilio guardar(Domicilio domicilio) {
        return domicilioRepository.save(domicilio);
    }

    public Optional<Domicilio> buscarPorId(Integer id) {
        return domicilioRepository.findById(id);
    }

    public List<Domicilio> buscarTodos() {
        return domicilioRepository.findAll();
    }

    public Domicilio actualizar(Domicilio domicilio) {
        return domicilioRepository.save(domicilio);
    }

    public void eliminar(Integer id) {
        domicilioRepository.deleteById(id);
    }
}

