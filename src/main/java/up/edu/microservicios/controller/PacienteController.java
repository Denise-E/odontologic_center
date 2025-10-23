package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import up.edu.microservicios.entity.Paciente;
import org.springframework.http.ResponseEntity;
import up.edu.microservicios.service.PacienteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes") // todo lo que venga de endpoints de pacientes
public class PacienteController {
    private static final Logger LOGGER= Logger.getLogger(PacienteController.class);
    private PacienteService pacienteService;

    // @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // Metodos

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

    @PostMapping
    public ResponseEntity<?> crearPaciente(@RequestBody Paciente paciente) {
        try {
            LOGGER.info("Creando paciente: " + paciente);
            Paciente pacienteCreado = pacienteService.guardar(paciente);
            LOGGER.info("Paciente creado: "+pacienteCreado);
            if (pacienteCreado != null) {
                return ResponseEntity.ok(pacienteCreado);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No se pudo crear el paciente");
                return ResponseEntity.internalServerError().body(response);
            }
        } catch (Exception e) {
            LOGGER.error("Error al crear paciente", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ocurrió un error al procesar la solicitud");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarPaciente(@PathVariable Integer id){
        LOGGER.info("Eliminando paciente: "+id);
        pacienteService.eliminar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Paciente eliminado exitosamente");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable Integer id, @RequestBody Paciente pacienteActualizado) {
        LOGGER.info("Actualizando paciente con ID: " + id);

        Paciente pacienteExistente = pacienteService.buscarPorId(id);
        if (pacienteExistente == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Paciente no encontrado");
            return ResponseEntity.status(404).body(response);
        }

        if (pacienteActualizado.getNombre() != null)
            pacienteExistente.setNombre(pacienteActualizado.getNombre());

        if (pacienteActualizado.getApellido() != null)
            pacienteExistente.setApellido(pacienteActualizado.getApellido());

        if (pacienteActualizado.getNumeroContacto() != null)
            pacienteExistente.setNumeroContacto(pacienteActualizado.getNumeroContacto());

        if (pacienteActualizado.getFechaIngreso() != null)
            pacienteExistente.setFechaIngreso(pacienteActualizado.getFechaIngreso());

        if (pacienteActualizado.getEmail() != null)
            pacienteExistente.setEmail(pacienteActualizado.getEmail());

        if (pacienteActualizado.getDomicilio() != null) {
            if (pacienteExistente.getDomicilio() == null) {
                pacienteExistente.setDomicilio(pacienteActualizado.getDomicilio());
            } else {
                if (pacienteActualizado.getDomicilio().getCalle() != null)
                    pacienteExistente.getDomicilio().setCalle(pacienteActualizado.getDomicilio().getCalle());
                if (pacienteActualizado.getDomicilio().getNumero() != null)
                    pacienteExistente.getDomicilio().setNumero(pacienteActualizado.getDomicilio().getNumero());
                if (pacienteActualizado.getDomicilio().getLocalidad() != null)
                    pacienteExistente.getDomicilio().setLocalidad(pacienteActualizado.getDomicilio().getLocalidad());
                if (pacienteActualizado.getDomicilio().getProvincia() != null)
                    pacienteExistente.getDomicilio().setProvincia(pacienteActualizado.getDomicilio().getProvincia());
            }
        }

        pacienteService.actualizar(pacienteExistente);
        LOGGER.info("Paciente actualizado: " + pacienteExistente);

        return ResponseEntity.ok(pacienteExistente);
    }

}
