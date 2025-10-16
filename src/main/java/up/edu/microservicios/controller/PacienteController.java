package up.edu.microservicios.controller;

import up.edu.microservicios.model.Paciente;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import up.edu.microservicios.service.PacienteService;

@RestController // sin vista
@RequestMapping("/api/pacientes") // todo lo que venga de endpoints de pacientes
public class PacienteController {
    private PacienteService pacienteService;

    // @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // Metodos CRUD conectando con el service

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Integer id){
        System.out.println("Buscando paciente por ID: "+id);
        Paciente pacienteBuscado = pacienteService.buscarPorId(id);

        System.out.println("Paciente buscado: " +pacienteBuscado);
        if(pacienteBuscado != null){
            return ResponseEntity.ok(pacienteBuscado);
        }
        return ResponseEntity.notFound().build();
    }
}
