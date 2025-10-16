package up.edu.microservicios.service;

import java.util.List;

public interface iService<T> {

    T guardar(T t);

    T buscarPorId(Integer id);

    List<T> buscarTodos();

    void actualizar(T t);

    void eliminar(Integer id);
}
