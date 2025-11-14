package up.edu.microservicios.service;

import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Desactiva auth, seguridad de Spring
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTestService {
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;
    @Autowired
    private MockMvc mockMvc;

    private void cargaDatos(){
        Paciente paciente = pacienteService.guardar(
                new Paciente("Jorge", "Pereyra", "1153544242", LocalDate.of(2025, 11,13), new Domicilio("Rivadavia", 123, "Caballito", "Buenos Aires"),"jorgito@gmail.com")
        );
        Odontologo  odontologo = odontologoService.guardar(
                new Odontologo("Nancy", "Sik", "M8288", "No hacer actividad física las 24hs previas al turno")
        );
        // Crear TurnoDTO - 2026-01-16 es viernes
        TurnoDTO turnoDTO = new TurnoDTO(null, paciente.getId(), odontologo.getId(), LocalDateTime.of(2026, 1, 16, 14, 0, 0));
        turnoService.guardar(turnoDTO);
    }

    @Test
    public void listarTurnos() throws Exception {
        cargaDatos();
        // perform arma estructura de datos
        MvcResult respuesta = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/turnos")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print() // print = lo mostramos
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andReturn(); // lo retornamos también para verlo

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }
}
