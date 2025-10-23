package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import up.edu.microservicios.entity.Odontologo;
import up.edu.microservicios.service.OdontologoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Odontologo odontologoBuscado = odontologoService.buscarPorId(id);

        LOGGER.info("Odontologo encontrado: "+odontologoBuscado);
        if(odontologoBuscado != null){
            return ResponseEntity.ok(odontologoBuscado);
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
            error.put("error", "Ocurrió un error al procesar la solicitud");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarOdontologo(@PathVariable Integer id){
        LOGGER.info("Eliminando odontologo: "+id);
        odontologoService.eliminar(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Odontologo eliminado exitosamente");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOdontologo(@PathVariable Integer id, @RequestBody Odontologo odontologoActualziado) {
        LOGGER.info("Actualizando odontologo con ID: " + id);

        Odontologo odontologoExistente = odontologoService.buscarPorId(id);
        if (odontologoExistente == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Odontologo no encontrado");
            return ResponseEntity.status(404).body(response);
        }

        if (odontologoActualziado.getNombre() != null)
            odontologoExistente.setNombre(odontologoActualziado.getNombre());

        if (odontologoActualziado.getApellido() != null)
            odontologoExistente.setApellido(odontologoActualziado.getApellido());

        if (odontologoActualziado.getMatricula() != null)
            odontologoExistente.setMatricula(odontologoActualziado.getMatricula());

        if (odontologoActualziado.getRequisitosTurnos() != null)
            odontologoExistente.setRequisitosTurnos(odontologoActualziado.getRequisitosTurnos());

        odontologoService.actualizar(odontologoExistente);
        LOGGER.info("Odontologo actualizado: " + odontologoExistente);

        return ResponseEntity.ok(odontologoExistente);
    }

}
