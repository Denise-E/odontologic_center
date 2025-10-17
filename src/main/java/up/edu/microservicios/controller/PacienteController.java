package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import up.edu.microservicios.model.Paciente;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import up.edu.microservicios.service.PacienteService;

import java.util.List;

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
}
