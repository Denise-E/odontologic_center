package service;

import dao.iDao;
import model.Domicilio;
import model.Paciente;

import java.util.List;

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

