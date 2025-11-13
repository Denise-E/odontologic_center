package up.edu.microservicios.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import up.edu.microservicios.dto.TurnoDTO;
import up.edu.microservicios.entity.Odontologo;
import up.edu.microservicios.entity.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TurnoTestService {

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private OdontologoService odontologoService;

    @Test
    public void testGuardarTurno() {
        // Crear paciente y odontólogo primero
        Paciente paciente = pacienteService.guardar(
                new Paciente(null, "Juan", "Pérez", "123456", LocalDate.now(), null, "juan@test.com")
        );
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dr. Carlos", "López", "MAT123")
        );

        // Crear turno usando DTO con fecha futura (lunes 5 de enero de 2026, 14:30)
        LocalDateTime fechaFutura = LocalDateTime.of(2026, 1, 5, 14, 30, 0);
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente.getId(),
                odontologo.getId(),
                fechaFutura
        );

        TurnoDTO turnoGuardado = turnoService.guardar(turnoDTO);

        Assertions.assertNotNull(turnoGuardado.getId(), "El turno debe tener ID después de guardar");
        Assertions.assertEquals(paciente.getId(), turnoGuardado.getPacienteId());
        Assertions.assertEquals(odontologo.getId(), turnoGuardado.getOdontologoId());
        Assertions.assertEquals(fechaFutura, turnoGuardado.getFecha());
    }


    @Test
    public void testGuardarTurnoConPacienteInexistente() {
        // Crear solo el odontólogo
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dr. María", "García", "MAT456")
        );

        // Intentar crear turno con paciente inexistente (ID = 9999, martes 6 de enero de 2026)
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                9999,
                odontologo.getId(),
                LocalDateTime.of(2026, 1, 6, 10, 0, 0)
        );

        // Debe lanzar excepción
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            turnoService.guardar(turnoDTO);
        });

        Assertions.assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    public void testGuardarTurnoConOdontologoInexistente() {
        // Crear solo el paciente
        Paciente paciente = pacienteService.guardar(
                new Paciente(null, "Laura", "Martínez", "789012", LocalDate.now(), null, "laura@test.com")
        );

        // Intentar crear turno con odontólogo inexistente (ID = 9999, miércoles 7 de enero de 2026)
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente.getId(),
                9999,
                LocalDateTime.of(2026, 1, 7, 10, 0, 0)
        );

        // Debe lanzar excepción
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            turnoService.guardar(turnoDTO);
        });

        Assertions.assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    public void testBuscarTurnoPorId() {
        // Crear paciente y odontólogo
        Paciente paciente = pacienteService.guardar(
                new Paciente(null, "Pedro", "Gómez", "345678", LocalDate.now(), null, "pedro@test.com")
        );
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dra. Ana", "Rodríguez", "MAT789")
        );

        // Crear turno (jueves 8 de enero de 2026, 9:00)
        LocalDateTime fechaTurno = LocalDateTime.of(2026, 1, 8, 9, 0, 0);
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente.getId(),
                odontologo.getId(),
                fechaTurno
        );
        TurnoDTO turnoGuardado = turnoService.guardar(turnoDTO);

        // Buscar el turno por ID
        Optional<TurnoDTO> turnoEncontrado = turnoService.buscarPorId(turnoGuardado.getId());

        Assertions.assertTrue(turnoEncontrado.isPresent());
        TurnoDTO turno = turnoEncontrado.get();
        Assertions.assertEquals(turnoGuardado.getId(), turno.getId());
        Assertions.assertEquals(paciente.getId(), turno.getPacienteId());
        Assertions.assertEquals(odontologo.getId(), turno.getOdontologoId());
        // Comparar truncando a segundos (la BD puede truncar nanosegundos)
        Assertions.assertEquals(fechaTurno.withNano(0), turno.getFecha().withNano(0));
    }

    @Test
    public void testBuscarTurnoPorIdInexistente() {
        Optional<TurnoDTO> turnoEncontrado = turnoService.buscarPorId(9999);
        Assertions.assertFalse(turnoEncontrado.isPresent());
    }

    @Test
    public void testBuscarTodosTurnos() {
        // Crear pacientes y odontólogo
        Paciente paciente1 = pacienteService.guardar(
                new Paciente(null, "Carlos", "Díaz", "111111", LocalDate.now(), null, "carlos@test.com")
        );
        Paciente paciente2 = pacienteService.guardar(
                new Paciente(null, "Sofía", "Hernández", "222222", LocalDate.now(), null, "sofia@test.com")
        );
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dr. Luis", "Fernández", "MAT111")
        );

        // Crear varios turnos (viernes 9 y lunes 12 de enero de 2026)
        turnoService.guardar(new TurnoDTO(null, paciente1.getId(), odontologo.getId(), LocalDateTime.of(2026, 1, 9, 10, 0, 0)));
        turnoService.guardar(new TurnoDTO(null, paciente2.getId(), odontologo.getId(), LocalDateTime.of(2026, 1, 12, 11, 0, 0)));

        List<TurnoDTO> turnos = turnoService.buscarTodos();

        Assertions.assertFalse(turnos.isEmpty());
        Assertions.assertTrue(turnos.size() >= 2);
    }

    @Test
    public void testBuscarTurnosPorPaciente() {
        // Crear un paciente, dos odontólogos
        Paciente paciente = pacienteService.guardar(
                new Paciente(null, "María", "López", "333333", LocalDate.now(), null, "maria@test.com")
        );
        Odontologo odontologo1 = odontologoService.guardar(
                new Odontologo("Dr. Jorge", "Pérez", "MAT222")
        );
        Odontologo odontologo2 = odontologoService.guardar(
                new Odontologo("Dra. Carmen", "Ruiz", "MAT333")
        );

        // Crear dos turnos para el mismo paciente (martes 13 y miércoles 14 de enero de 2026)
        turnoService.guardar(new TurnoDTO(null, paciente.getId(), odontologo1.getId(), LocalDateTime.of(2026, 1, 13, 10, 0, 0)));
        turnoService.guardar(new TurnoDTO(null, paciente.getId(), odontologo2.getId(), LocalDateTime.of(2026, 1, 14, 14, 0, 0)));

        // Buscar turnos del paciente
        List<TurnoDTO> turnosPaciente = turnoService.buscarPorPacienteId(paciente.getId());

        Assertions.assertEquals(2, turnosPaciente.size());
        turnosPaciente.forEach(turno -> {
            Assertions.assertEquals(paciente.getId(), turno.getPacienteId());
        });
    }

    @Test
    public void testBuscarTurnosPorOdontologo() {
        // Crear dos pacientes y un odontólogo
        Paciente paciente1 = pacienteService.guardar(
                new Paciente(null, "Roberto", "Silva", "444444", LocalDate.now(), null, "roberto@test.com")
        );
        Paciente paciente2 = pacienteService.guardar(
                new Paciente(null, "Elena", "Torres", "555555", LocalDate.now(), null, "elena@test.com")
        );
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dr. Fernando", "Ramos", "MAT444")
        );

        // Crear dos turnos para el mismo odontólogo (jueves 15 y viernes 16 de enero de 2026)
        turnoService.guardar(new TurnoDTO(null, paciente1.getId(), odontologo.getId(), LocalDateTime.of(2026, 1, 15, 9, 0, 0)));
        turnoService.guardar(new TurnoDTO(null, paciente2.getId(), odontologo.getId(), LocalDateTime.of(2026, 1, 16, 15, 30, 0)));

        // Buscar turnos del odontólogo
        List<TurnoDTO> turnosOdontologo = turnoService.buscarPorOdontologoId(odontologo.getId());

        Assertions.assertEquals(2, turnosOdontologo.size());
        turnosOdontologo.forEach(turno -> {
            Assertions.assertEquals(odontologo.getId(), turno.getOdontologoId());
        });
    }

    @Test
    public void testActualizarTurno() {
        // Crear pacientes y odontólogo
        Paciente paciente1 = pacienteService.guardar(
                new Paciente(null, "Miguel", "Torres", "666666", LocalDate.now(), null, "miguel@test.com")
        );
        Paciente paciente2 = pacienteService.guardar(
                new Paciente(null, "Andrea", "Vargas", "777777", LocalDate.now(), null, "andrea@test.com")
        );
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dra. Patricia", "Morales", "MAT555")
        );

        // Crear turno inicial (lunes 19 de enero de 2026, 10:00)
        LocalDateTime fechaInicial = LocalDateTime.of(2026, 1, 19, 10, 0, 0);
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente1.getId(),
                odontologo.getId(),
                fechaInicial
        );
        TurnoDTO turnoGuardado = turnoService.guardar(turnoDTO);

        // Actualizar el turno (cambiar paciente y fecha a martes 20 de enero de 2026, 16:30)
        LocalDateTime fechaActualizada = LocalDateTime.of(2026, 1, 20, 16, 30, 0);
        TurnoDTO turnoActualizado = new TurnoDTO(
                turnoGuardado.getId(),
                paciente2.getId(),
                odontologo.getId(),
                fechaActualizada
        );
        TurnoDTO resultado = turnoService.actualizar(turnoGuardado.getId(), turnoActualizado);

        Assertions.assertEquals(turnoGuardado.getId(), resultado.getId());
        Assertions.assertEquals(paciente2.getId(), resultado.getPacienteId());
        Assertions.assertEquals(odontologo.getId(), resultado.getOdontologoId());
        Assertions.assertEquals(fechaActualizada, resultado.getFecha());
    }

    @Test
    public void testActualizarTurnoInexistente() {
        // Crear paciente y odontólogo
        Paciente paciente = pacienteService.guardar(
                new Paciente(null, "Ricardo", "Ortiz", "888888", LocalDate.now(), null, "ricardo@test.com")
        );
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dr. Javier", "Navarro", "MAT666")
        );

        // Intentar actualizar turno inexistente (miércoles 21 de enero de 2026)
        TurnoDTO turnoDTO = new TurnoDTO(
                9999,
                paciente.getId(),
                odontologo.getId(),
                LocalDateTime.of(2026, 1, 21, 10, 0, 0)
        );

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            turnoService.actualizar(9999, turnoDTO);
        });

        Assertions.assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    public void testEliminarTurno() {
        // Crear paciente y odontólogo
        Paciente paciente = pacienteService.guardar(
                new Paciente(null, "Isabel", "Méndez", "999999", LocalDate.now(), null, "isabel@test.com")
        );
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dr. Alberto", "Castro", "MAT777")
        );

        // Crear turno (jueves 22 de enero de 2026, 11:00)
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente.getId(),
                odontologo.getId(),
                LocalDateTime.of(2026, 1, 22, 11, 0, 0)
        );
        TurnoDTO turnoGuardado = turnoService.guardar(turnoDTO);

        // Verificar que existe
        Optional<TurnoDTO> turnoAntes = turnoService.buscarPorId(turnoGuardado.getId());
        Assertions.assertTrue(turnoAntes.isPresent());

        // Eliminar el turno
        turnoService.eliminar(turnoGuardado.getId());

        // Verificar que ya no existe
        Optional<TurnoDTO> turnoDespues = turnoService.buscarPorId(turnoGuardado.getId());
        Assertions.assertFalse(turnoDespues.isPresent());
    }
}

