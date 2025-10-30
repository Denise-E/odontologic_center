package up.edu.microservicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import up.edu.microservicios.entity.Paciente;
import org.springframework.stereotype.Service;
import up.edu.microservicios.exception.EmailInvalidFormatException;
import up.edu.microservicios.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;
    
    // Patrón regex para validar formato de email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    private void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new EmailInvalidFormatException("El email no puede estar vacío");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailInvalidFormatException("El formato del email '" + email + "' es inválido");
        }
    }
    
    public Paciente guardar(Paciente paciente) {
        // Validar formato del email antes de guardar
        validateEmailFormat(paciente.getEmail());
        return pacienteRepository.save(paciente);
    }

    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    public List<Paciente> buscarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente actualizar(Paciente paciente) {
        // Validar formato del email antes de actualizar
        if (paciente.getEmail() != null) {
            validateEmailFormat(paciente.getEmail());
        }
        return pacienteRepository.save(paciente);
    }

    public void eliminar(Integer id) {
        pacienteRepository.deleteById(id);
    }

    public Optional<Paciente> buscarPorEmail(String email) {
        return pacienteRepository.findByEmail(email);
    }

    public Optional<Paciente> buscarPorNumeroContacto(String numeroContacto) {
        return pacienteRepository.findByNumeroContacto(numeroContacto);
    }
}

