package up.edu.microservicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import up.edu.microservicios.dto.TurnoDTO;
import up.edu.microservicios.entity.Odontologo;
import up.edu.microservicios.entity.Paciente;
import up.edu.microservicios.entity.Turno;
import up.edu.microservicios.exception.AppointmentConflictException;
import up.edu.microservicios.exception.InvalidDateException;
import up.edu.microservicios.exception.ResourceNotFoundException;
import up.edu.microservicios.repository.TurnoRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private OdontologoService odontologoService;

    private void validarFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new InvalidDateException("La fecha del turno no puede ser nula");
        }
        
        // Validar que no sea una fecha/hora pasada
        if (fecha.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("No se pueden crear turnos en fechas pasadas");
        }
        
        // Validar que no sea domingo (día no laborable)
        if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new InvalidDateException("No se pueden crear turnos los días domingo");
        }
    }
    
    /**
     * Valida que no haya conflictos de turnos (mismo día y hora)
     */
    private void validarConflictos(Integer pacienteId, Integer odontologoId, LocalDateTime fecha, Integer turnoIdExcluir) {
        // Validar que el odontólogo no tenga otro turno en la misma fecha y hora
        Optional<Turno> turnoOdontologo = turnoRepository.findByOdontologoIdAndFecha(odontologoId, fecha);
        if (turnoOdontologo.isPresent() && !turnoOdontologo.get().getId().equals(turnoIdExcluir)) {
            throw new AppointmentConflictException("Día y horario ocupados.");
        }
        
        // Validar que el paciente no tenga otro turno en la misma fecha y hora
        Optional<Turno> turnoPaciente = turnoRepository.findByPacienteIdAndFecha(pacienteId, fecha);
        if (turnoPaciente.isPresent() && !turnoPaciente.get().getId().equals(turnoIdExcluir)) {
            throw new AppointmentConflictException("El paciente ya tiene un turno asignado en la fecha y hora " + fecha);
        }
    }

    // Guardar un turno (recibe DTO, devuelve DTO)
    public TurnoDTO guardar(TurnoDTO turnoDTO) {
        // Validar que existan paciente y odontólogo
        Optional<Paciente> paciente = pacienteService.buscarPorId(turnoDTO.getPacienteId());
        Optional<Odontologo> odontologo = odontologoService.buscarPorId(turnoDTO.getOdontologoId());
        
        if (paciente.isEmpty()) {
            throw new ResourceNotFoundException("Paciente con ID " + turnoDTO.getPacienteId() + " no encontrado");
        }
        if (odontologo.isEmpty()) {
            throw new ResourceNotFoundException("Odontólogo con ID " + turnoDTO.getOdontologoId() + " no encontrado");
        }
        
        // Validar fecha del turno
        validarFecha(turnoDTO.getFecha());
        
        // Validar conflictos de turnos
        validarConflictos(turnoDTO.getPacienteId(), turnoDTO.getOdontologoId(), turnoDTO.getFecha(), null);
        
        // Convertir DTO a Entidad
        Turno turno = dtoAEntidad(turnoDTO, paciente.get(), odontologo.get());
        
        // Guardar en BD
        Turno turnoGuardado = turnoRepository.save(turno);
        
        // Convertir Entidad a DTO y devolver
        return entidadADto(turnoGuardado);
    }

    // Buscar por ID
    public Optional<TurnoDTO> buscarPorId(Integer id) {
        return turnoRepository.findById(id)
                .map(this::entidadADto);
    }

    // Buscar todos
    public List<TurnoDTO> buscarTodos() {
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoDTO> turnosDTO = new ArrayList<>();
        for (Turno turno : turnos) {
            turnosDTO.add(entidadADto(turno));
        }
        return turnosDTO;
    }

    // Actualizar turno
    public TurnoDTO actualizar(Integer id, TurnoDTO turnoDTO) {
        // Verificar que el turno existe
        Optional<Turno> turnoExistente = turnoRepository.findById(id);
        if (turnoExistente.isEmpty()) {
            throw new ResourceNotFoundException("Turno con ID " + id + " no encontrado");
        }
        
        // Validar que existan paciente y odontólogo
        Optional<Paciente> paciente = pacienteService.buscarPorId(turnoDTO.getPacienteId());
        Optional<Odontologo> odontologo = odontologoService.buscarPorId(turnoDTO.getOdontologoId());
        
        if (paciente.isEmpty()) {
            throw new ResourceNotFoundException("Paciente con ID " + turnoDTO.getPacienteId() + " no encontrado");
        }
        if (odontologo.isEmpty()) {
            throw new ResourceNotFoundException("Odontólogo con ID " + turnoDTO.getOdontologoId() + " no encontrado");
        }
        
        // Validar fecha del turno
        validarFecha(turnoDTO.getFecha());
        
        // Validar conflictos de turnos (excluyendo el turno actual)
        validarConflictos(turnoDTO.getPacienteId(), turnoDTO.getOdontologoId(), turnoDTO.getFecha(), id);
        
        // Actualizar el turno
        turnoDTO.setId(id); // Asegurar que mantiene el ID
        Turno turno = dtoAEntidad(turnoDTO, paciente.get(), odontologo.get());
        Turno turnoActualizado = turnoRepository.save(turno);
        
        return entidadADto(turnoActualizado);
    }

    // Eliminar turno
    public void eliminar(Integer id) {
        turnoRepository.deleteById(id);
    }

    // Buscar turnos por paciente
    public List<TurnoDTO> buscarPorPacienteId(Integer pacienteId) {
        return turnoRepository.findByPacienteId(pacienteId).stream()
                .map(this::entidadADto)
                .collect(Collectors.toList());
    }

    // Buscar turnos por odontólogo
    public List<TurnoDTO> buscarPorOdontologoId(Integer odontologoId) {
        return turnoRepository.findByOdontologoId(odontologoId).stream()
                .map(this::entidadADto)
                .collect(Collectors.toList());
    }

    // Convertir Entidad a DTO (incluye objetos completos para el frontend)
    private TurnoDTO entidadADto(Turno turno) {
        TurnoDTO dto = new TurnoDTO();
        dto.setId(turno.getId());
        dto.setPacienteId(turno.getPaciente().getId());
        dto.setOdontologoId(turno.getOdontologo().getId());
        dto.setFecha(turno.getFecha());
        dto.setPaciente(turno.getPaciente());
        dto.setOdontologo(turno.getOdontologo());
        return dto;
    }

    // Convertir DTO a Entidad
    private Turno dtoAEntidad(TurnoDTO dto, Paciente paciente, Odontologo odontologo) {
        Turno turno = new Turno();
        turno.setId(dto.getId());
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(dto.getFecha());
        return turno;
    }
}
