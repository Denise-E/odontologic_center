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

        // Crear turno usando DTO
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente.getId(),
                odontologo.getId(),
                LocalDate.of(2025, 10, 25)
        );

        TurnoDTO turnoGuardado = turnoService.guardar(turnoDTO);

        Assertions.assertNotNull(turnoGuardado.getId(), "El turno debe tener ID después de guardar");
        Assertions.assertEquals(paciente.getId(), turnoGuardado.getPacienteId());
        Assertions.assertEquals(odontologo.getId(), turnoGuardado.getOdontologoId());
        Assertions.assertEquals(LocalDate.of(2025, 10, 25), turnoGuardado.getFecha());
    }


    @Test
    public void testGuardarTurnoConPacienteInexistente() {
        // Crear solo el odontólogo
        Odontologo odontologo = odontologoService.guardar(
                new Odontologo("Dr. María", "García", "MAT456")
        );

        // Intentar crear turno con paciente inexistente (ID = 9999)
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                9999,
                odontologo.getId(),
                LocalDate.of(2025, 10, 25)
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

        // Intentar crear turno con odontólogo inexistente (ID = 9999)
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente.getId(),
                9999,
                LocalDate.of(2025, 10, 25)
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

        // Crear turno
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente.getId(),
                odontologo.getId(),
                LocalDate.of(2025, 11, 15)
        );
        TurnoDTO turnoGuardado = turnoService.guardar(turnoDTO);

        // Buscar el turno por ID
        Optional<TurnoDTO> turnoEncontrado = turnoService.buscarPorId(turnoGuardado.getId());

        Assertions.assertTrue(turnoEncontrado.isPresent());
        TurnoDTO turno = turnoEncontrado.get();
        Assertions.assertEquals(turnoGuardado.getId(), turno.getId());
        Assertions.assertEquals(paciente.getId(), turno.getPacienteId());
        Assertions.assertEquals(odontologo.getId(), turno.getOdontologoId());
        Assertions.assertEquals(LocalDate.of(2025, 11, 15), turno.getFecha());
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

        // Crear varios turnos
        turnoService.guardar(new TurnoDTO(null, paciente1.getId(), odontologo.getId(), LocalDate.of(2025, 10, 1)));
        turnoService.guardar(new TurnoDTO(null, paciente2.getId(), odontologo.getId(), LocalDate.of(2025, 10, 2)));

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

        // Crear dos turnos para el mismo paciente
        turnoService.guardar(new TurnoDTO(null, paciente.getId(), odontologo1.getId(), LocalDate.of(2025, 11, 1)));
        turnoService.guardar(new TurnoDTO(null, paciente.getId(), odontologo2.getId(), LocalDate.of(2025, 11, 5)));

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

        // Crear dos turnos para el mismo odontólogo
        turnoService.guardar(new TurnoDTO(null, paciente1.getId(), odontologo.getId(), LocalDate.of(2025, 11, 10)));
        turnoService.guardar(new TurnoDTO(null, paciente2.getId(), odontologo.getId(), LocalDate.of(2025, 11, 12)));

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

        // Crear turno inicial
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente1.getId(),
                odontologo.getId(),
                LocalDate.of(2025, 12, 1)
        );
        TurnoDTO turnoGuardado = turnoService.guardar(turnoDTO);

        // Actualizar el turno (cambiar paciente y fecha)
        TurnoDTO turnoActualizado = new TurnoDTO(
                turnoGuardado.getId(),
                paciente2.getId(),
                odontologo.getId(),
                LocalDate.of(2025, 12, 15)
        );
        TurnoDTO resultado = turnoService.actualizar(turnoGuardado.getId(), turnoActualizado);

        Assertions.assertEquals(turnoGuardado.getId(), resultado.getId());
        Assertions.assertEquals(paciente2.getId(), resultado.getPacienteId());
        Assertions.assertEquals(odontologo.getId(), resultado.getOdontologoId());
        Assertions.assertEquals(LocalDate.of(2025, 12, 15), resultado.getFecha());
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

        // Intentar actualizar turno inexistente
        TurnoDTO turnoDTO = new TurnoDTO(
                9999,
                paciente.getId(),
                odontologo.getId(),
                LocalDate.of(2025, 12, 1)
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

        // Crear turno
        TurnoDTO turnoDTO = new TurnoDTO(
                null,
                paciente.getId(),
                odontologo.getId(),
                LocalDate.of(2025, 11, 20)
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

    @Test
    public void testMultiplesTurnosMismoPaciente() {
        // Un paciente puede tener múltiples turnos
        Paciente paciente = pacienteService.guardar(
                new Paciente(null, "Daniela", "Sánchez", "101010", LocalDate.now(), null, "daniela@test.com")
        );
        Odontologo odontologo1 = odontologoService.guardar(
                new Odontologo("Dr. Martín", "González", "MAT888")
        );
        Odontologo odontologo2 = odontologoService.guardar(
                new Odontologo("Dra. Lucía", "Ramírez", "MAT999")
        );

        // Crear dos turnos para el mismo paciente
        TurnoDTO turno1 = turnoService.guardar(
                new TurnoDTO(null, paciente.getId(), odontologo1.getId(), LocalDate.of(2025, 10, 10))
        );
        TurnoDTO turno2 = turnoService.guardar(
                new TurnoDTO(null, paciente.getId(), odontologo2.getId(), LocalDate.of(2025, 10, 20))
        );

        Assertions.assertNotNull(turno1.getId());
        Assertions.assertNotNull(turno2.getId());
        Assertions.assertNotEquals(turno1.getId(), turno2.getId());
        Assertions.assertEquals(paciente.getId(), turno1.getPacienteId());
        Assertions.assertEquals(paciente.getId(), turno2.getPacienteId());
    }
}

