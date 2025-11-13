// Formateo de fecha y hora a DD-MM-YYYY HH:MM
function formatearFecha(fechaISO) {
    if (!fechaISO) return '';
    
    // Si la fecha ya incluye hora (tiene 'T'), parsearla directamente
    // Si solo es fecha (YYYY-MM-DD), agregar hora por defecto
    let fecha;
    if (fechaISO.includes('T')) {
        fecha = new Date(fechaISO);
    } else {
        fecha = new Date(fechaISO + 'T00:00:00');
    }
    
    const dia = String(fecha.getDate()).padStart(2, '0');
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const anio = fecha.getFullYear();
    const horas = String(fecha.getHours()).padStart(2, '0');
    const minutos = String(fecha.getMinutes()).padStart(2, '0');
    
    return `${dia}-${mes}-${anio} ${horas}:${minutos}`;
}

window.addEventListener('load', function () {
    cargarTurnos();
});

function cargarTurnos() {
    const url = '/api/turnos';
    const settings = {
        method: 'GET'
    };

    fetch(url, settings)
        .then(response => response.json())
        .then(data => {
            console.log('Turnos recibidos del servidor:', data);

            if (data.length === 0) {
                document.getElementById('noTurnosMessage').style.display = 'block';
                document.getElementById('turnosTableContainer').style.display = 'none';
                return;
            } else {
                document.getElementById('noTurnosMessage').style.display = 'none';
                document.getElementById('turnosTableContainer').style.display = 'flex';
            }

            const tbody = document.getElementById('turnoTableBody');
            tbody.innerHTML = ''; // Limpiar tabla

            for(turno of data) {
                console.log('Procesando turno:', turno);
                
                var turnoRow = tbody.insertRow();
                let tr_id = 'tr_turno_' + turno.id;
                turnoRow.id = tr_id;

                // Botón de ver/editar con ícono de lápiz
                let editButton = '<button' +
                                      ' id=' + '\"' + 'btn_edit_turno_' + turno.id + '\"' +
                                      ' type="button" onclick="findTurnoBy('+turno.id+')" class="btn btn-link p-0 me-2" style="font-size: 1.2rem; text-decoration: none;" title="Editar turno">' +
                                      '👁️' +
                                      '</button>';

                // Botón de eliminar con ícono de tacho
                let deleteButton = '<button' +
                                      ' id=' + '\"' + 'btn_delete_turno_' + turno.id + '\"' +
                                      ' type="button" onclick="deleteTurnoBy('+turno.id+')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;" title="Eliminar turno">' +
                                      '🗑️' +
                                      '</button>';

                // Formatear la fecha en formato argentino
                const fechaFormateada = formatearFecha(turno.fecha);

                turnoRow.innerHTML = '<td>' + turno.id + '</td>' +
                        '<td class=\"td_turno_paciente\">' + (turno.paciente?.nombre || '') + ' ' + (turno.paciente?.apellido || '') + '</td>' +
                        '<td class=\"td_turno_odontologo\">' + 'Dr/a. ' + (turno.odontologo?.nombre || '') + ' ' + (turno.odontologo?.apellido || '') + '</td>' +
                        '<td class=\"td_turno_fecha\">' + fechaFormateada + '</td>' +
                        '<td>' + editButton + ' ' + deleteButton + '</td>';
            };
        })
        .catch(error => {
            console.error('Error al cargar turnos:', error);
            document.getElementById('noTurnosMessage').style.display = 'block';
            document.getElementById('turnosTableContainer').style.display = 'none';
        });
}

function findTurnoBy(id) {
    const url = '/api/turnos/' + id;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('No se encontró el turno');
            return response.json();
        })
        .then(turno => {
            console.log('Turno recibido:', turno);
            
            // Ocultar mensaje de error si existe
            const errorDiv = document.getElementById('errorTurnoUpdate');
            if (errorDiv) {
                errorDiv.classList.add('d-none');
            }
            
            // Llenar el ID del turno
            document.getElementById('turno_id').value = turno.id || '';
            
            // Llenar selectores de pacientes y odontólogos
            llenarSelectorPacientes(turno.paciente ? turno.paciente.id : null);
            llenarSelectorOdontologos(turno.odontologo ? turno.odontologo.id : null);
            
            // Convertir y llenar fecha
            if (turno.fecha) {
                // Convertir fecha ISO a formato datetime-local (YYYY-MM-DDTHH:MM)
                const fechaLocal = convertirFechaISOALocal(turno.fecha);
                document.getElementById('turno_fecha').value = fechaLocal;
            }

            // Abrir el modal
            const modal = new bootstrap.Modal(document.getElementById('turnoModal'));
            modal.show();
        })
        .catch(err => {
            console.error('Error al cargar el turno:', err);
            alert('No se pudo cargar el turno: ' + err.message);
        });
}

