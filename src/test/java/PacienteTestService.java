import dao.BD;
import dao.DomicilioDAOH2;
import dao.PacienteDAOH2;
import model.Domicilio;
import model.Paciente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.DomicilioService;
import service.PacienteService;

import java.time.LocalDate;
import java.util.List;

public class PacienteTestService {
    @Test
    public void testBuscarPorId(){
        //DADO
        BD.crearTablas();
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());

        //CUANDO
        Paciente paciente= pacienteService.buscarPorId(1);
        System.out.println("datos encontrados: ");
        System.out.println(paciente);

        //ENTONCES
        Assertions.assertTrue(paciente!=null);
    }

    @Test
    public void testBuscarTodos(){
        //DADO
        BD.crearTablas();
        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());

        //CUANDO
        List<Paciente> pacientes= pacienteService.buscarTodos();
        System.out.println("datos encontrados: ");
        System.out.println(pacientes);

        //ENTONCES
        Assertions.assertTrue(!pacientes.isEmpty());
    }

    @Test
    public void testActualizarPorId() {
        // DADO
        BD.crearTablas();

        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        DomicilioService domicilioService= new DomicilioService(new DomicilioDAOH2());
        Domicilio domicilio = domicilioService.buscarPorId(1);

        // CUANDO: cambio algunos datos y actualizo
        Paciente actualizado = new Paciente(
                1,
                "Juan Carlos",
                "Pérez",
                22222222,
                LocalDate.of(2025, 2, 2),
                domicilio,
                "juanc@mail.com"
        );
        pacienteService.actualizar(actualizado);

        Paciente pacienteActualizado = pacienteService.buscarPorId(1);
        System.out.println("datos actualizados: " + pacienteActualizado);

        Assertions.assertNotNull(pacienteActualizado);
        Assertions.assertEquals("Juan Carlos", pacienteActualizado.getNombre());
        Assertions.assertEquals("Pérez", pacienteActualizado.getApellido());
        Assertions.assertEquals(22222222, pacienteActualizado.getNumeroContacto());
        Assertions.assertEquals(LocalDate.of(2025, 2, 2), pacienteActualizado.getFechaIngreso());
        Assertions.assertEquals("juanc@mail.com", pacienteActualizado.getEmail());
        Assertions.assertEquals(domicilio.getId(), pacienteActualizado.getDomicilio().getId());

    }

    @Test
    public void testEliminarPorId() {
        // DADO
        BD.crearTablas();

        DomicilioService domicilioService= new DomicilioService(new DomicilioDAOH2());
        Domicilio domicilio = domicilioService.guardar(
                new Domicilio(null, "Calle 123", 456, "CABA", "Buenos Aires")
        );

        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());
        Paciente nuevo = new Paciente(
                null,
                "Laura",
                "Gómez",
                12345678,
                LocalDate.of(2025, 1, 10),
                domicilio,
                "laura@mail.com"
        );
        pacienteService.guardar(nuevo);
        Integer idGenerado = nuevo.getId();

        // CUANDO
        pacienteService.eliminar(idGenerado);

        // ENTONCES
        Paciente buscado = pacienteService.buscarPorId(idGenerado);
        Assertions.assertNull(buscado);
    }

    @Test
    public void testGuardar() {
        // DADO
        BD.crearTablas();

        DomicilioService domicilioService= new DomicilioService(new DomicilioDAOH2());
        Domicilio domicilio = domicilioService.guardar(
                new Domicilio(null, "Av Siempre Viva", 742, "Springfield", "USA")
        );
        Assertions.assertNotNull(domicilio.getId(), "El domicilio debe guardarse y tener ID");

        PacienteService pacienteService= new PacienteService(new PacienteDAOH2());

        Paciente nuevo = new Paciente(
                null,
                "Homero",
                "Simpson",
                123456789,
                LocalDate.of(2025, 10, 9),
                domicilio,
                "homero@springfield.com"
        );

        // CUANDO
        Paciente guardado = pacienteService.guardar(nuevo);

        // ENTONCES
        Assertions.assertNotNull(guardado.getId(), "El paciente guardado debe tener ID");
        Assertions.assertEquals("Homero", guardado.getNombre());
        Assertions.assertEquals("Simpson", guardado.getApellido());
        Assertions.assertEquals(123456789, guardado.getNumeroContacto());
        Assertions.assertEquals(LocalDate.of(2025, 10, 9), guardado.getFechaIngreso());
        Assertions.assertEquals("homero@springfield.com", guardado.getEmail());
        Assertions.assertNotNull(guardado.getDomicilio());
        Assertions.assertEquals(domicilio.getId(), guardado.getDomicilio().getId());
    }
}

