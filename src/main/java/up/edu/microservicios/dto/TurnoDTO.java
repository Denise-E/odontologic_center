package up.edu.microservicios.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import up.edu.microservicios.entity.Odontologo;
import up.edu.microservicios.entity.Paciente;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TurnoDTO {
    private Integer id;
    private Integer pacienteId;
    private Integer odontologoId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;
    
    // Objetos completos para el frontend
    private Paciente paciente;
    private Odontologo odontologo;
    
    public TurnoDTO(Integer id, Integer pacienteId, Integer odontologoId, LocalDateTime fecha) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.odontologoId = odontologoId;
        this.fecha = fecha;
    }
    
    // Constructor completo con todas las propiedades
    public TurnoDTO(Integer id, Integer pacienteId, Integer odontologoId, LocalDateTime fecha, 
                    Paciente paciente, Odontologo odontologo) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.odontologoId = odontologoId;
        this.fecha = fecha;
        this.paciente = paciente;
        this.odontologo = odontologo;
    }
}
