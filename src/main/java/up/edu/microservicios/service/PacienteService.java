package up.edu.microservicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import up.edu.microservicios.dao_DELETE.iDao;
import up.edu.microservicios.entity.Paciente;
import org.springframework.stereotype.Service;
import up.edu.microservicios.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;
    public Paciente guardar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    public List<Paciente> buscarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente actualizar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void eliminar(Integer id) {
        pacienteRepository.deleteById(id);
    }

    public Optional<Paciente> buscarPorEmail(String email) {
        return pacienteRepository.findByEmail(email);
    }

    public Optional<Paciente> buscarPorNumeroContacto(String numeroContacto) {
        return pacienteRepository.findByNumeroContacto(numeroContacto);
    }
    /*
    implements iService<Paciente>
    private iDao<Paciente> pacienteiDao;

    public PacienteService(iDao<Paciente> pacienteiDao) {
        this.pacienteiDao = pacienteiDao;
    }

    @Override
    public Paciente guardar(Paciente paciente) {
        return pacienteiDao.guardar(paciente);
    }

    @Override
    public Paciente buscarPorId(Integer id) {
        return pacienteiDao.buscar(id);
    }

    @Override
    public List<Paciente> buscarTodos() {
        return pacienteiDao.buscarTodos();
    }

    @Override
    public void actualizar(Paciente paciente) {
        pacienteiDao.actualizar(paciente);
    }


    @Override
    public void eliminar(Integer id) {
        pacienteiDao.eliminar(id);
    }
     */
}

