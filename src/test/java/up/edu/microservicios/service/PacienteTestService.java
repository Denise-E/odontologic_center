package up.edu.microservicios.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import up.edu.microservicios.entity.Domicilio;
import up.edu.microservicios.entity.Paciente;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PacienteTestService {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private DomicilioService domicilioService;

    @Test
    public void testGuardar() {
        Domicilio domicilio = new Domicilio(null, "Av Siempre Viva", 742, "Springfield", "USA");

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
        Assertions.assertNotNull(guardado.getDomicilio().getId(), "El domicilio debe tener ID después de guardar");
    }

    @Test
    public void testBuscarPorId() {
        Domicilio domicilio = new Domicilio(null, "Calle 123", 456, "CABA", "Buenos Aires");
        Paciente pacienteCreado = pacienteService.guardar(
                new Paciente(null, "Federico", "Rojas", "1111", LocalDate.now(), domicilio, "federico@mail.com")
        );

        Optional<Paciente> pacienteOpt = pacienteService.buscarPorId(pacienteCreado.getId());

        Assertions.assertTrue(pacienteOpt.isPresent());
        Paciente paciente = pacienteOpt.get();
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
        Domicilio domicilio = new Domicilio(null, "Calle 123", 456, "CABA", "Buenos Aires");

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
        Optional<Paciente> pacienteActualizadoOpt = pacienteService.buscarPorId(paciente.getId());

        Assertions.assertTrue(pacienteActualizadoOpt.isPresent());
        Paciente pacienteActualizado = pacienteActualizadoOpt.get();
        Assertions.assertEquals("Juan Carlos", pacienteActualizado.getNombre());
        Assertions.assertEquals("22222222", pacienteActualizado.getNumeroContacto());
        Assertions.assertEquals(LocalDate.of(2025, 2, 2), pacienteActualizado.getFechaIngreso());
        Assertions.assertEquals("juanc@mail.com", pacienteActualizado.getEmail());
    }

    @Test
    public void testEliminarPorId() {
        Domicilio domicilio = new Domicilio(null, "Calle Falsa", 123, "Springfield", "USA");
        Paciente nuevo = pacienteService.guardar(
                new Paciente(null, "Laura", "Gómez", "12345678", LocalDate.now(), domicilio, "laura@mail.com")
        );

        Integer id = nuevo.getId();
        pacienteService.eliminar(id);

        Optional<Paciente> buscado = pacienteService.buscarPorId(id);
        Assertions.assertFalse(buscado.isPresent());
    }
}
