package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import up.edu.microservicios.entity.Odontologo;
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
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Odontologo>> buscarTodos(){
        LOGGER.info("Buscando todos odontologos");
        List<Odontologo> odontologos = odontologoService.buscarTodos();
        if(odontologos != null){
            return ResponseEntity.ok(odontologos);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crearOdontologo(@RequestBody Odontologo odontologo) {
        try {
            // Validar matrícula única
            Optional<Odontologo> odontologoPorMatricula = odontologoService.buscarPorMatricula(odontologo.getMatricula());
            if(odontologoPorMatricula.isPresent()){
                Map<String, String> response = new HashMap<>();
                response.put("message", "La matrícula " + odontologo.getMatricula() + " ya está registrada");
                response.put("field", "matricula");
                return ResponseEntity.status(409).body(response); // 409 Conflict
            }

            LOGGER.info("Creando odontologo: " + odontologo);
            Odontologo odontologoCreado = odontologoService.guardar(odontologo);
            LOGGER.info("Odontologo creado: "+odontologoCreado);
            if (odontologoCreado != null) {
                return ResponseEntity.ok(odontologoCreado);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No se pudo crear el odontologo");
                return ResponseEntity.internalServerError().body(response);
            }
        } catch (Exception e) {
            LOGGER.error("Error al crear odontologo", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ocurrió un error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarOdontologo(@PathVariable Integer id){
        LOGGER.info("Eliminando odontologo: "+id);
        
        Optional<Odontologo> odontologoExistente = odontologoService.buscarPorId(id);
        if (odontologoExistente.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Odontologo no encontrado");
            return ResponseEntity.status(404).body(response);
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
            Map<String, String> response = new HashMap<>();
            response.put("message", "Odontologo no encontrado");
            return ResponseEntity.status(404).body(response);
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
