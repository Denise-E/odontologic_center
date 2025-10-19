package up.edu.microservicios.controller;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import up.edu.microservicios.model.Odontologo;
import up.edu.microservicios.service.OdontologoService;

import java.util.List;

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
}
