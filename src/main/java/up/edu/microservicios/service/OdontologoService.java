package up.edu.microservicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import up.edu.microservicios.entity.Odontologo;
import org.springframework.stereotype.Service;
import up.edu.microservicios.repository.OdontologoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologoService {
    @Autowired
    private OdontologoRepository odontologoRepository;

    public Odontologo guardar(Odontologo odontologo) {
        return odontologoRepository.save(odontologo);
    }

    public Optional<Odontologo> buscarPorId(Integer id) {
        return odontologoRepository.findById(id);
    }

    public List<Odontologo> buscarTodos() {
        return odontologoRepository.findAll();
    }

    public Odontologo actualizar(Odontologo odontologo) {
        return odontologoRepository.save(odontologo);
    }

    public void eliminar(Integer id) {
        odontologoRepository.deleteById(id);
    }

    public Optional<Odontologo> buscarPorMatricula(String matricula) {
        return odontologoRepository.findByMatricula(matricula);
    }
}
