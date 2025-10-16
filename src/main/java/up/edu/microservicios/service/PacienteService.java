package up.edu.microservicios.service;

import up.edu.microservicios.dao.iDao;
import up.edu.microservicios.model.Paciente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService implements iService<Paciente> {
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
}

