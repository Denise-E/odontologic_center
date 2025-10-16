package up.edu.microservicios.service;

import up.edu.microservicios.dao.iDao;
import up.edu.microservicios.model.Odontologo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements iService<Odontologo> {
    private iDao<Odontologo> odontologoDao;

    public OdontologoService(iDao<Odontologo> odontologoDao) {
        this.odontologoDao = odontologoDao;
    }

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        return odontologoDao.guardar(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Integer id) {
        return odontologoDao.buscar(id);
    }

    @Override
    public List<Odontologo> buscarTodos() {
        return odontologoDao.buscarTodos();
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        odontologoDao.actualizar(odontologo);
    }

    @Override
    public void eliminar(Integer id) {
        odontologoDao.eliminar(id);
    }
}
