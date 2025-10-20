package up.edu.microservicios.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import up.edu.microservicios.model.Domicilio;
import up.edu.microservicios.model.Paciente;
import up.edu.microservicios.dao.BD;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PacienteTestService {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private DomicilioService domicilioService;

    @BeforeEach
    public void setUpSchema() {
        // Garantiza que las tablas existan para los DAOs JDBC antes de cada test
        BD.crearTablas();
    }

    @Test
    public void testGuardar() {
        Domicilio domicilio = domicilioService.guardar(
                new Domicilio(null, "Av Siempre Viva", 742, "Springfield", "USA")
        );

        Paciente nuevo = new Paciente(
                null,
                "Homero",
                "Simpson",
                "123456789",
                LocalDate.of(2025, 10, 9),
                domicilio,
                "homero@springfield.com"
        );

        Paciente guardado = pacienteService.guardar(nuevo);

        Assertions.assertNotNull(guardado.getId(), "El paciente guardado debe tener ID");
        Assertions.assertEquals("Homero", guardado.getNombre());
        Assertions.assertEquals("Simpson", guardado.getApellido());
        Assertions.assertEquals("123456789", guardado.getNumeroContacto());
        Assertions.assertEquals(LocalDate.of(2025, 10, 9), guardado.getFechaIngreso());
        Assertions.assertEquals("homero@springfield.com", guardado.getEmail());
        Assertions.assertNotNull(guardado.getDomicilio());
        Assertions.assertEquals(domicilio.getId(), guardado.getDomicilio().getId());
    }

    @Test
    public void testBuscarPorId() {
        Domicilio domicilio = domicilioService.guardar(
                new Domicilio(null, "Calle 123", 456, "CABA", "Buenos Aires")
        );
        Paciente pacienteCreado = pacienteService.guardar(
                new Paciente(null, "Federico", "Rojas", "1111", LocalDate.now(), domicilio, "federico@mail.com")
        );

        Paciente paciente = pacienteService.buscarPorId(pacienteCreado.getId());

        Assertions.assertNotNull(paciente);
        Assertions.assertEquals("Federico", paciente.getNombre());
        Assertions.assertEquals("Rojas", paciente.getApellido());
    }

    @Test
    public void testBuscarTodos() {
        pacienteService.guardar(new Paciente(null, "A", "A", "123", LocalDate.now(), null, "a@mail.com"));
        pacienteService.guardar(new Paciente(null, "B", "B", "456", LocalDate.now(), null, "b@mail.com"));

        List<Paciente> pacientes = pacienteService.buscarTodos();

        Assertions.assertFalse(pacientes.isEmpty());
        Assertions.assertTrue(pacientes.size() >= 2);
    }

    @Test
    public void testActualizarPorId() {
        Domicilio domicilio = domicilioService.guardar(
                new Domicilio(null, "Calle 123", 456, "CABA", "Buenos Aires")
        );

        Paciente paciente = pacienteService.guardar(
                new Paciente(null, "Juan", "Pérez", "11111111", LocalDate.of(2024, 5, 5), domicilio, "juan@mail.com")
        );

        Paciente actualizado = new Paciente(
                paciente.getId(),
                "Juan Carlos",
                "Pérez",
                "22222222",
                LocalDate.of(2025, 2, 2),
                domicilio,
                "juanc@mail.com"
        );

        pacienteService.actualizar(actualizado);
        Paciente pacienteActualizado = pacienteService.buscarPorId(paciente.getId());

        Assertions.assertNotNull(pacienteActualizado);
        Assertions.assertEquals("Juan Carlos", pacienteActualizado.getNombre());
        Assertions.assertEquals("22222222", pacienteActualizado.getNumeroContacto());
        Assertions.assertEquals(LocalDate.of(2025, 2, 2), pacienteActualizado.getFechaIngreso());
        Assertions.assertEquals("juanc@mail.com", pacienteActualizado.getEmail());
    }

    @Test
    public void testEliminarPorId() {
        Domicilio domicilio = domicilioService.guardar(
                new Domicilio(null, "Calle Falsa", 123, "Springfield", "USA")
        );
        Paciente nuevo = pacienteService.guardar(
                new Paciente(null, "Laura", "Gómez", "12345678", LocalDate.now(), domicilio, "laura@mail.com")
        );

        Integer id = nuevo.getId();
        pacienteService.eliminar(id);

        Paciente buscado = pacienteService.buscarPorId(id);
        Assertions.assertNull(buscado);
    }
}
