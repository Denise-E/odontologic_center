package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import up.edu.microservicios.entity.Odontologo;
import up.edu.microservicios.exception.DuplicateResourceException;
import up.edu.microservicios.service.OdontologoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/odontologos")
public class OdontologoController {
    private static final Logger LOGGER= Logger.getLogger(OdontologoController.class);
    private OdontologoService odontologoService;

    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    // Metodos

    @GetMapping("/{id}")
    public ResponseEntity<Odontologo> buscarPorId(@PathVariable Integer id){
        LOGGER.info("Buscando odontologo por ID: "+id);
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorId(id);

        LOGGER.info("Odontologo encontrado: "+odontologoBuscado);
        if(odontologoBuscado.isPresent()){
            return ResponseEntity.ok(odontologoBuscado.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Odontologo no encontrado");

    }

    @GetMapping
    public ResponseEntity<List<Odontologo>> buscarTodos(){
        LOGGER.info("Buscando todos odontologos");
        List<Odontologo> odontologos = odontologoService.buscarTodos();
        if(odontologos != null){
            return ResponseEntity.ok(odontologos);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay odontologos registrados por el momento");
    }

    @PostMapping
    public ResponseEntity<Odontologo> crearOdontologo(@RequestBody Odontologo odontologo) {
        // Validar matrícula única
        Optional<Odontologo> odontologoPorMatricula = odontologoService.buscarPorMatricula(odontologo.getMatricula());
        if(odontologoPorMatricula.isPresent()){
            throw new DuplicateResourceException("La matrícula " + odontologo.getMatricula() + " ya está registrada");
        }

        LOGGER.info("Creando odontologo: " + odontologo);
        Odontologo odontologoCreado = odontologoService.guardar(odontologo);
        LOGGER.info("Odontologo creado: " + odontologoCreado);
        
        return ResponseEntity.ok(odontologoCreado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarOdontologo(@PathVariable Integer id){
        LOGGER.info("Eliminando odontologo: "+id);
        
        Optional<Odontologo> odontologoExistente = odontologoService.buscarPorId(id);
        if (odontologoExistente.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Odontologo no encontrado");
        }
        
        odontologoService.eliminar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Odontologo eliminado exitosamente");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOdontologo(@PathVariable Integer id, @RequestBody Odontologo odontologoActualizado) {
        LOGGER.info("Actualizando odontologo con ID: " + id);

        Optional<Odontologo> odontologoOpt = odontologoService.buscarPorId(id);
        if (odontologoOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Odontologo no encontrado");
        }

        Odontologo odontologoExistente = odontologoOpt.get();

        if (odontologoActualizado.getNombre() != null)
            odontologoExistente.setNombre(odontologoActualizado.getNombre());

        if (odontologoActualizado.getApellido() != null)
            odontologoExistente.setApellido(odontologoActualizado.getApellido());

        if (odontologoActualizado.getMatricula() != null)
            odontologoExistente.setMatricula(odontologoActualizado.getMatricula());

        if (odontologoActualizado.getRequisitosTurnos() != null)
            odontologoExistente.setRequisitosTurnos(odontologoActualizado.getRequisitosTurnos());

        Odontologo odontologoActualizadoFinal = odontologoService.actualizar(odontologoExistente);
        LOGGER.info("Odontologo actualizado: " + odontologoActualizadoFinal);

        return ResponseEntity.ok(odontologoActualizadoFinal);
    }

}
