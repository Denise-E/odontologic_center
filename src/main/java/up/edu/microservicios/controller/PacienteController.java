package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import up.edu.microservicios.model.Paciente;
import org.springframework.http.ResponseEntity;
import up.edu.microservicios.service.PacienteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // sin vista
@RequestMapping("/api/pacientes") // todo lo que venga de endpoints de pacientes
public class PacienteController {
    private static final Logger LOGGER= Logger.getLogger(PacienteController.class);
    private PacienteService pacienteService;

    // @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // Metodos CRUD conectando con el service

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Integer id){
        LOGGER.info("Buscando paciente por ID: "+id);
        Paciente pacienteBuscado = pacienteService.buscarPorId(id);

        LOGGER.info("Paciente encontrado: "+pacienteBuscado);
        if(pacienteBuscado != null){
            return ResponseEntity.ok(pacienteBuscado);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> buscarTodos(){
        LOGGER.info("Buscando todos pacientes");
        List<Paciente> pacientes = pacienteService.buscarTodos();
        if(pacientes != null){
            return ResponseEntity.ok(pacientes);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente){
        LOGGER.info("Creando paciente: "+paciente);
        Paciente pacienteCreado = pacienteService.guardar(paciente);

        LOGGER.info("Paciente creado: "+pacienteCreado);
        if(pacienteCreado != null){
            return ResponseEntity.ok(pacienteCreado);
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarPaciente(@PathVariable Integer id){
        LOGGER.info("Eliminando paciente: "+id);
        pacienteService.eliminar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Paciente eliminado exitosamente");

        return ResponseEntity.ok(response);    }
}
