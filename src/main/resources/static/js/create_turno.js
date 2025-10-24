window.addEventListener('load', function () {
    // Cargar pacientes y odontólogos al cargar la página
    cargarPacientes();
    cargarOdontologos();
    
    // Establecer fecha mínima como hoy
    const fechaInput = document.getElementById('fecha');
    const hoy = new Date().toISOString().split('T')[0];
    fechaInput.setAttribute('min', hoy);
    fechaInput.value = hoy;

    // Manejar el envío del formulario
    const formulario = document.getElementById('create_turno_form');
    formulario.addEventListener('submit', function (event) {
        event.preventDefault();
        crearTurno();
    });
});

// Cargar lista de pacientes
function cargarPacientes() {
    const url = '/api/pacientes';
    const settings = {
        method: 'GET'
    };

    fetch(url, settings)
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById('pacienteId');
            // Limpiar opciones existentes excepto la primera
            select.innerHTML = '<option value="">Seleccione un paciente...</option>';
            
            data.forEach(paciente => {
                const option = document.createElement('option');
                option.value = paciente.id;
                option.text = `${paciente.nombre} ${paciente.apellido} - ${paciente.email}`;
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar pacientes:', error);
            mostrarError('No se pudieron cargar los pacientes');
        });
}

// Cargar lista de odontólogos
function cargarOdontologos() {
    const url = '/api/odontologos';
    const settings = {
        method: 'GET'
    };

    fetch(url, settings)
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById('odontologoId');
            // Limpiar opciones existentes excepto la primera
            select.innerHTML = '<option value="">Seleccione un odontólogo...</option>';
            
            data.forEach(odontologo => {
                const option = document.createElement('option');
                option.value = odontologo.id;
                option.text = `Dr/a. ${odontologo.nombre} ${odontologo.apellido} - Mat: ${odontologo.matricula}`;
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar odontólogos:', error);
            mostrarError('No se pudieron cargar los odontólogos');
        });
}

// Crear turno
function crearTurno() {
    const pacienteId = parseInt(document.getElementById('pacienteId').value);
    const odontologoId = parseInt(document.getElementById('odontologoId').value);
    const fecha = document.getElementById('fecha').value;

    // Validar que todos los campos estén completos
    if (!pacienteId || !odontologoId || !fecha) {
        mostrarError('Por favor complete todos los campos');
        return;
    }

    const turnoDTO = {
        pacienteId: pacienteId,
        odontologoId: odontologoId,
        fecha: fecha
    };

    const url = '/api/turnos';
    const settings = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(turnoDTO)
    };

    fetch(url, settings)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(err => Promise.reject(err));
            }
        })
        .then(data => {
            console.log('Turno creado:', data);
            mostrarExito();
            limpiarFormulario();
        })
        .catch(error => {
            console.error('Error al crear turno:', error);
            const mensaje = error.message || 'No se pudo crear el turno. Verifique que el paciente y odontólogo existan.';
            mostrarError(mensaje);
        });
}

// Mostrar mensaje de éxito
function mostrarExito() {
    const alertSuccess = document.getElementById('alert-success');
    const alertError = document.getElementById('alert-error');
    
    alertError.style.display = 'none';
    alertSuccess.style.display = 'block';
    
    // Ocultar después de 3 segundos
    setTimeout(() => {
        alertSuccess.style.display = 'none';
    }, 3000);
}

// Mostrar mensaje de error
function mostrarError(mensaje) {
    const alertSuccess = document.getElementById('alert-success');
    const alertError = document.getElementById('alert-error');
    const errorMessage = document.getElementById('error-message');
    
    alertSuccess.style.display = 'none';
    errorMessage.textContent = mensaje;
    alertError.style.display = 'block';
    
    // Ocultar después de 5 segundos
    setTimeout(() => {
        alertError.style.display = 'none';
    }, 5000);
}

// Limpiar formulario
function limpiarFormulario() {
    document.getElementById('pacienteId').value = '';
    document.getElementById('odontologoId').value = '';
    const hoy = new Date().toISOString().split('T')[0];
    document.getElementById('fecha').value = hoy;
}

