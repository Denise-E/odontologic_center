package up.edu.microservicios.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import up.edu.microservicios.dto.TurnoDTO;
import up.edu.microservicios.entity.Domicilio;
import up.edu.microservicios.entity.Odontologo;
import up.edu.microservicios.entity.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Desactiva auth, seguridad de Spring
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ejecutar tests en orden
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // Limpiar solo después de todos los tests
public class IntegrationTestService {
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void guardarPaciente() throws Exception {
        Paciente paciente = pacienteService.guardar(
                new Paciente("Jorge", "Pereyra", "1153544242", LocalDate.of(2026, 1, 15), new Domicilio("Rivadavia", 123, "Caballito", "Buenos Aires"),"jorgito@gmail.com")
        );

        assertNotNull(paciente);
        assertEquals(1, paciente.getId());
    }

    @Test
    @Order(2)
    public void guardarOdontologo() throws Exception {
        Odontologo  odontologo = odontologoService.guardar(
                new Odontologo("Nancy", "Sik", "M8288", "No hacer actividad física las 24hs previas al turno")
        );
        assertNotNull(odontologo);
        assertEquals(1, odontologo.getId());
    }

    @Test
    @Order(3)
    public void guardarTurno() throws Exception {
        // Crear TurnoDTO - 2026-01-16 es viernes
        TurnoDTO turnoDTO = new TurnoDTO(null, 1, 1, LocalDateTime.of(2026, 1, 16, 14, 0, 0));
        TurnoDTO turnoGuardado = turnoService.guardar(turnoDTO);
        assertNotNull(turnoGuardado);
        assertEquals(1, turnoGuardado.getId());
    }

    @Test
    @Order(4)
    public void listarTurnos() throws Exception {
        // perform arma estructura de datos
        MvcResult respuesta = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/turnos")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print() // print = lo mostramos
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn(); // lo retornamos también para verlo

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @Order(5)
    public void buscarPaciente() throws Exception {
        String email = "jorgito@gmail.com";
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorEmail(email);
        assertTrue(pacienteBuscado.isPresent());
        assertNotNull(pacienteBuscado.get());
        assertEquals("Jorge", pacienteBuscado.get().getNombre());
    }
}
