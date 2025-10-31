package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import up.edu.microservicios.dto.TurnoDTO;
import up.edu.microservicios.exception.ResourceNotFoundException;
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
    public ResponseEntity<TurnoDTO> crearTurno(@RequestBody TurnoDTO turnoDTO) {
        LOGGER.info("Creando turno: " + turnoDTO);
        TurnoDTO turnoCreado = turnoService.guardar(turnoDTO);
        LOGGER.info("Turno creado: " + turnoCreado);
        return ResponseEntity.ok(turnoCreado);
    }

    // Buscar turno por ID
    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> buscarPorId(@PathVariable Integer id) {
        LOGGER.info("Buscando turno por ID: " + id);
        Optional<TurnoDTO> turnoDTO = turnoService.buscarPorId(id);
        
        if (turnoDTO.isPresent()) {
            LOGGER.info("Turno encontrado: " + turnoDTO.get());
            return ResponseEntity.ok(turnoDTO.get());
        }

        throw new ResourceNotFoundException("Turno con ID " + id + " no encontrado");
    }

    // Buscar todos los turnos
    @GetMapping
    public ResponseEntity<List<TurnoDTO>> buscarTodos() {
        LOGGER.info("Buscando todos los turnos");
        List<TurnoDTO> turnos = turnoService.buscarTodos();

        if (turnos.isEmpty()) {
            throw new ResourceNotFoundException("No hay turnos registrados por el momento");
        }
        return ResponseEntity.ok(turnos);
    }

    // Buscar turnos por paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<TurnoDTO>> buscarPorPaciente(@PathVariable Integer pacienteId) {
        LOGGER.info("Buscando turnos del paciente ID: " + pacienteId);
        List<TurnoDTO> turnos = turnoService.buscarPorPacienteId(pacienteId);
        if (turnos.isEmpty()) {
            throw new ResourceNotFoundException("No hay turnos registrados para el paciente con ID " + pacienteId);
        }
        return ResponseEntity.ok(turnos);
    }

    // Buscar turnos por odontólogo
    @GetMapping("/odontologo/{odontologoId}")
    public ResponseEntity<List<TurnoDTO>> buscarPorOdontologo(@PathVariable Integer odontologoId) {
        LOGGER.info("Buscando turnos del odontólogo ID: " + odontologoId);
        List<TurnoDTO> turnos = turnoService.buscarPorOdontologoId(odontologoId);
        if (turnos.isEmpty()) {
            throw new ResourceNotFoundException("No hay turnos registrados para el odontólogo con ID " + odontologoId);
        }
        return ResponseEntity.ok(turnos);
    }

    // Actualizar turno
    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTO> actualizarTurno(@PathVariable Integer id, @RequestBody TurnoDTO turnoDTO) {
        LOGGER.info("Actualizando turno con ID: " + id);
        Optional<TurnoDTO> turnoExistente = turnoService.buscarPorId(id);
        if (turnoExistente.isEmpty()) {
            throw new ResourceNotFoundException("Turno con ID " + id + " no encontrado");
        }
        
        TurnoDTO turnoActualizado = turnoService.actualizar(id, turnoDTO);
        LOGGER.info("Turno actualizado: " + turnoActualizado);
        return ResponseEntity.ok(turnoActualizado);
    }

    // Eliminar turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarTurno(@PathVariable Integer id) {
        LOGGER.info("Eliminando turno: " + id);
        
        Optional<TurnoDTO> turnoExistente = turnoService.buscarPorId(id);
        if (turnoExistente.isEmpty()) {
            throw new ResourceNotFoundException("Turno con ID " + id + " no encontrado");
        }
        
        turnoService.eliminar(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Turno eliminado exitosamente");
        return ResponseEntity.ok(response);
    }
}
