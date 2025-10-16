package up.edu.microservicios.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import up.edu.microservicios.model.Odontologo;

import java.util.List;

@SpringBootTest  // Levanta el contexto completo de Spring Boot
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// Limpia la BD en memoria después de cada test (útil con H2)
public class OdontologoTestService {

    @Autowired
    private OdontologoService odontologoService; // Spring inyecta el bean real

    @Test
    public void testGuardar() {
        Odontologo odontologo = new Odontologo("Nicolas", "Martinez", "D334");
        Odontologo guardado = odontologoService.guardar(odontologo);

        Assertions.assertNotNull(guardado.getId());
        Assertions.assertEquals("Nicolas", guardado.getNombre());
        Assertions.assertEquals("Martinez", guardado.getApellido());
    }

    @Test
    public void testBuscarPorId() {
        Odontologo odontologo = new Odontologo("Federico", "Rojas", "D3433");
        odontologoService.guardar(odontologo);

        Odontologo buscado = odontologoService.buscarPorId(odontologo.getId());

        Assertions.assertNotNull(buscado);
        Assertions.assertEquals("Federico", buscado.getNombre());
        Assertions.assertEquals("Rojas", buscado.getApellido());
        Assertions.assertEquals("D3433", buscado.getMatricula());
    }

    @Test
    public void testBuscarTodos() {
        odontologoService.guardar(new Odontologo("A", "A", "111"));
        odontologoService.guardar(new Odontologo("B", "B", "222"));

        List<Odontologo> odontologos = odontologoService.buscarTodos();

        Assertions.assertFalse(odontologos.isEmpty());
        Assertions.assertTrue(odontologos.size() >= 2);
    }

    @Test
    public void testEliminarPorId() {
        Odontologo nuevo = odontologoService.guardar(new Odontologo("Denise", "Eichen", "D444"));
        Integer id = nuevo.getId();

        odontologoService.eliminar(id);

        Odontologo buscado = odontologoService.buscarPorId(id);
        Assertions.assertNull(buscado);
    }

    @Test
    public void testActualizarPorId() {
        Odontologo creado = odontologoService.guardar(new Odontologo("Julius", "Hibbert", "MAT001"));

        Odontologo actualizado = new Odontologo(
                creado.getId(),
                "Julius",
                "Hibbert",
                "MAT999",
                "Historial clínico"
        );
        odontologoService.actualizar(actualizado);

        Odontologo odontologoActualizado = odontologoService.buscarPorId(creado.getId());

        Assertions.assertNotNull(odontologoActualizado);
        Assertions.assertEquals("MAT999", odontologoActualizado.getMatricula());
        Assertions.assertEquals("Historial clínico", odontologoActualizado.getRequisitosTurnos());
    }
}
