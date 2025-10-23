package up.edu.microservicios.service;

import up.edu.microservicios.dao_DELETE.iDao;
import up.edu.microservicios.entity.Domicilio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomicilioService implements iService<Domicilio> {
    private iDao<Domicilio> domicilioDao;

    public DomicilioService(iDao<Domicilio> domicilioDao) {
        this.domicilioDao = domicilioDao;
    }

    @Override
    public Domicilio guardar(Domicilio domicilio) {
        return domicilioDao.guardar(domicilio);
    }

    @Override
    public Domicilio buscarPorId(Integer id) {
        return domicilioDao.buscar(id);
    }

    @Override
    public List<Domicilio> buscarTodos() {
        return domicilioDao.buscarTodos();
    }

    @Override
    public void actualizar(Domicilio domicilio) {
        domicilioDao.actualizar(domicilio);
    }

    @Override
    public void eliminar(Integer id) {
        domicilioDao.eliminar(id);
    }
}

