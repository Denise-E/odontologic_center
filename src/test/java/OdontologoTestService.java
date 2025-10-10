import dao.BD;
import dao.OdontologoDAOH2;
import dao.PacienteDAOH2;
import model.Odontologo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.OdontologoService;
import service.PacienteService;

import java.util.List;

public class OdontologoTestService {

    @Test
    public void testGuardar() {
        // Dado
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        Odontologo odontologo = new Odontologo("Nicolas", "Martinez", "D334");

        // Cuando
        Odontologo guardado = odontologoService.guardar(odontologo);

        // Entonces
        Assertions.assertNotNull(guardado.getId());
        Assertions.assertEquals("Nicolas", guardado.getNombre());
        Assertions.assertEquals("Martinez", guardado.getApellido());
    }

    @Test
    public void testBuscarPorId() {
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        Odontologo odontologo = odontologoService.buscarPorId(1);

        Assertions.assertNotNull(odontologo);
        Assertions.assertEquals(1, odontologo.getId());
        Assertions.assertEquals("Federico", odontologo.getNombre());
        Assertions.assertEquals("Rojas", odontologo.getApellido());
        Assertions.assertEquals("D3433", odontologo.getMatricula());
        Assertions.assertNull(odontologo.getRequisitosTurnos());
    }

    @Test
    public void testBuscarTodos() {
        // Cuando
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());
        List<Odontologo> odontologos = odontologoService.buscarTodos();

        // Entonces
        Assertions.assertFalse(odontologos.isEmpty());
    }

    @Test
    public void testEliminarPorId() {
        // DADO
        BD.crearTablas();

        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());

        Odontologo nuevo = new Odontologo();
        nuevo.setNombre("Denise");
        nuevo.setApellido("Eichen");
        nuevo.setMatricula("D444");
        odontologoService.guardar(nuevo);

        Integer id = nuevo.getId();
        Assertions.assertNotNull(id);

        // CUANDO
        odontologoService.eliminar(id);

        // ENTONCES
        Odontologo buscado = odontologoService.buscarPorId(id);
        Assertions.assertNull(buscado);
    }

    @Test
    public void testActualizarPorId() {
        // DADO
        BD.crearTablas();
        OdontologoService odontologoService= new OdontologoService(new OdontologoDAOH2());

        // CUANDO
        Odontologo actualizado = new Odontologo(
                2,
                "Julius",
                "Hibbert",
                "MAT999",
                "Historial clínico"
        );
        odontologoService.actualizar(actualizado);

        // ENTONCES
        Odontologo odontologoActualizado = odontologoService.buscarPorId(2);
        System.out.println("datos actualizados: " + odontologoActualizado);

        Assertions.assertNotNull(odontologoActualizado);
        Assertions.assertEquals("Julius", odontologoActualizado.getNombre());
        Assertions.assertEquals("Hibbert", odontologoActualizado.getApellido());
        Assertions.assertEquals("MAT999", odontologoActualizado.getMatricula());
        Assertions.assertEquals("Historial clínico", odontologoActualizado.getRequisitosTurnos());
    }
}