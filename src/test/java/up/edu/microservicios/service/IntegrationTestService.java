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

    @Test
    @Order(6)
    public void buscarTurnosPorPaciente() throws Exception {
        MvcResult respuesta = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/turnos/paciente/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();
        
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @Order(7)
    public void buscarTurnosPorOdontologo() throws Exception {
        MvcResult respuesta = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/turnos/odontologo/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();
        
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @Order(8)
    public void buscarOdontologoPorMatricula() throws Exception {
        Optional<Odontologo> odontologo = odontologoService.buscarPorMatricula("M8288");
        assertTrue(odontologo.isPresent());
        assertEquals("Nancy", odontologo.get().getNombre());
        assertEquals("Sik", odontologo.get().getApellido());
        assertEquals(1, odontologo.stream().count());
    }

    @Test
    @Order(9)
    public void buscarPacientePorId() throws Exception {
        MvcResult respuesta = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/pacientes/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();
        
        String contenido = respuesta.getResponse().getContentAsString();
        assertFalse(contenido.isEmpty());
    }

    @Test
    @Order(10)
    public void buscarTurnoPorId() throws Exception {
        MvcResult respuesta = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/turnos/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();
        
        String contenido = respuesta.getResponse().getContentAsString();
        assertFalse(contenido.isEmpty());        
    }

    @Test
    @Order(11)
    public void actualizarTurno() throws Exception {
        // Actualizar el turno a una nueva fecha (2026-01-17 es sábado, usemos 2026-01-20 lunes)
        String turnoActualizado = "{\"id\":1,\"pacienteId\":1,\"odontologoId\":1,\"fecha\":\"2026-01-20T15:00:00\"}";
        
        MvcResult respuesta = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/turnos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(turnoActualizado)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();
        
        String contenido = respuesta.getResponse().getContentAsString();
        assertFalse(contenido.isEmpty());
        assertTrue(contenido.contains("2026-01-20"), "La fecha debe estar actualizada");
    }

    @Test
    @Order(12)
    public void eliminarTurno() throws Exception {
        MvcResult respuesta = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/turnos/1")
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn();
        
        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue(contenido.contains("Turno eliminado exitosamente"), "Debe contener mensaje de éxito");
        
        // Verificar que el turno fue eliminado
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/turnos/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()
        );
    }
}
