package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import up.edu.microservicios.dto.TurnoDTO;
import up.edu.microservicios.service.TurnoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    private static final Logger LOGGER = Logger.getLogger(TurnoController.class);
    
    @Autowired
    private TurnoService turnoService;

    // Crear turno - Recibe y devuelve DTO
    @PostMapping
    public ResponseEntity<?> crearTurno(@RequestBody TurnoDTO turnoDTO) {
        try {
            LOGGER.info("Creando turno: " + turnoDTO);
            TurnoDTO turnoCreado = turnoService.guardar(turnoDTO);
            LOGGER.info("Turno creado: " + turnoCreado);
            return ResponseEntity.ok(turnoCreado);
        } catch (RuntimeException e) {
            LOGGER.error("Error al crear turno: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            LOGGER.error("Error al crear turno", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ocurrió un error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // Buscar turno por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        LOGGER.info("Buscando turno por ID: " + id);
        Optional<TurnoDTO> turnoDTO = turnoService.buscarPorId(id);
        
        if (turnoDTO.isPresent()) {
            LOGGER.info("Turno encontrado: " + turnoDTO.get());
            return ResponseEntity.ok(turnoDTO.get());
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Turno no encontrado");
        return ResponseEntity.status(404).body(response);
    }

    // Buscar todos los turnos
    @GetMapping
    public ResponseEntity<List<TurnoDTO>> buscarTodos() {
        LOGGER.info("Buscando todos los turnos");
        List<TurnoDTO> turnos = turnoService.buscarTodos();
        return ResponseEntity.ok(turnos);
    }

    // Actualizar turno
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTurno(@PathVariable Integer id, @RequestBody TurnoDTO turnoDTO) {
        try {
            LOGGER.info("Actualizando turno con ID: " + id);
            TurnoDTO turnoActualizado = turnoService.actualizar(id, turnoDTO);
            LOGGER.info("Turno actualizado: " + turnoActualizado);
            return ResponseEntity.ok(turnoActualizado);
        } catch (RuntimeException e) {
            LOGGER.error("Error al actualizar turno: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(404).body(error);
            }
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            LOGGER.error("Error al actualizar turno", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ocurrió un error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // Eliminar turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarTurno(@PathVariable Integer id) {
        LOGGER.info("Eliminando turno: " + id);
        
        Optional<TurnoDTO> turnoExistente = turnoService.buscarPorId(id);
        if (turnoExistente.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Turno no encontrado");
            return ResponseEntity.status(404).body(response);
        }
        
        turnoService.eliminar(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Turno eliminado exitosamente");
        return ResponseEntity.ok(response);
    }
}
