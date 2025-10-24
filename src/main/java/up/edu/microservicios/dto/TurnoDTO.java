package up.edu.microservicios.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoDTO {
    private Integer id;
    private Integer pacienteId;
    private Integer odontologoId;
    private LocalDate fecha;
}
