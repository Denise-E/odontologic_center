// Variables globales para almacenar listas de pacientes y odontólogos
let pacientesList = [];
let odontologosList = [];

// Cargar listas al iniciar la página
window.addEventListener('load', function () {
    cargarListaPacientes();
    cargarListaOdontologos();
});

// Cargar lista de pacientes
function cargarListaPacientes() {
    const url = '/api/pacientes';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            pacientesList = data;
            console.log('Lista de pacientes cargada:', pacientesList);
        })
        .catch(error => {
            console.error('Error al cargar lista de pacientes:', error);
        });
}

// Cargar lista de odontólogos
function cargarListaOdontologos() {
    const url = '/api/odontologos';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            odontologosList = data;
            console.log('Lista de odontólogos cargada:', odontologosList);
        })
        .catch(error => {
            console.error('Error al cargar lista de odontólogos:', error);
        });
}

// Llenar el selector de pacientes
function llenarSelectorPacientes(pacienteIdSeleccionado) {
    const select = document.getElementById('turno_paciente_id');
    select.innerHTML = '<option value="">Seleccione un paciente...</option>';
    
    pacientesList.forEach(paciente => {
        const option = document.createElement('option');
        option.value = paciente.id;
        option.text = `${paciente.nombre} ${paciente.apellido} - ${paciente.email}`;
        if (paciente.id === pacienteIdSeleccionado) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// Llenar el selector de odontólogos
function llenarSelectorOdontologos(odontologoIdSeleccionado) {
    const select = document.getElementById('turno_odontologo_id');
    select.innerHTML = '<option value="">Seleccione un odontólogo...</option>';
    
    odontologosList.forEach(odontologo => {
        const option = document.createElement('option');
        option.value = odontologo.id;
        option.text = `Dr/a. ${odontologo.nombre} ${odontologo.apellido} - Mat: ${odontologo.matricula}`;
        if (odontologo.id === odontologoIdSeleccionado) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// Convertir fecha ISO a formato datetime-local (YYYY-MM-DDTHH:MM)
function convertirFechaISOALocal(fechaISO) {
    if (!fechaISO) return '';
    
    // Si la fecha ya tiene formato correcto
    if (fechaISO.includes('T')) {
        // Asegurarse de tener solo YYYY-MM-DDTHH:MM (sin segundos)
        return fechaISO.substring(0, 16);
    }
    
    return fechaISO;
}

// Función para actualizar el turno
function updateTurno() {
    const turnoId = document.getElementById('turno_id').value;
    const pacienteId = document.getElementById('turno_paciente_id').value;
    const odontologoId = document.getElementById('turno_odontologo_id').value;
    const fechaInput = document.getElementById('turno_fecha').value;
    
    // Validar que todos los campos estén completos
    if (!turnoId || !pacienteId || !odontologoId || !fechaInput) {
        mostrarError('Por favor complete todos los campos');
        return;
    }
    
    // Convertir fecha de datetime-local (YYYY-MM-DDTHH:MM) a formato ISO (YYYY-MM-DDTHH:MM:SS)
    const fechaISO = fechaInput.includes(':00') ? fechaInput : fechaInput + ':00';
    
    // Crear el objeto turno para enviar al backend
    const turnoData = {
        pacienteId: parseInt(pacienteId),
        odontologoId: parseInt(odontologoId),
        fecha: fechaISO
    };
    
    console.log('Actualizando turno:', turnoData);
    
    const url = '/api/turnos/' + turnoId;
    const settings = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(turnoData)
    };
    
    fetch(url, settings)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    try {
                        const error = JSON.parse(text);
                        throw new Error(error.message || 'Error al actualizar el turno');
                    } catch {
                        throw new Error(text || 'Error al actualizar el turno');
                    }
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Turno actualizado exitosamente:', data);
            
            // Ocultar error
            document.getElementById('errorTurnoUpdate').classList.add('d-none');
            
            // Cerrar el modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('turnoModal'));
            modal.hide();
            
            // Recargar la lista de turnos
            cargarTurnos();
            
            // Mostrar mensaje de éxito
            alert('Turno actualizado exitosamente');
        })
        .catch(error => {
            console.error('Error al actualizar turno:', error);
            let mensaje = error.message || 'No se pudo actualizar el turno';
            
            // Detectar mensajes específicos del backend
            if (mensaje.includes('conflicto') || mensaje.includes('ocupado') || mensaje.includes('conflict')) {
                mensaje = 'Día y horario ocupados para este odontólogo.';
            }
            
            mostrarError(mensaje);
        });
}

function mostrarError(mensaje) {
    const errorDiv = document.getElementById('errorTurnoUpdate');
    errorDiv.textContent = mensaje;
    errorDiv.classList.remove('d-none');
    
    // Auto-ocultar después de 5 segundos
    setTimeout(() => {
        errorDiv.classList.add('d-none');
    }, 5000);
}
