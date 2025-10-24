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
}

