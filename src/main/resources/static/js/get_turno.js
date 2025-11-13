// Formateo de fecha a DD-MM-YYYY
function formatearFecha(fechaISO) {
    if (!fechaISO) return '';
    const fecha = new Date(fechaISO + 'T00:00:00'); 
    const dia = String(fecha.getDate()).padStart(2, '0');
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const anio = fecha.getFullYear();
    return `${dia}-${mes}-${anio}`;
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

                // Botón de eliminar con ícono de tacho
                let deleteButton = '<button' +
                                      ' id=' + '\"' + 'btn_delete_turno_' + turno.id + '\"' +
                                      ' type="button" onclick="deleteTurnoBy('+turno.id+')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;">' +
                                      '🗑️' +
                                      '</button>';

                // Botón de ver con ícono de ojo
                let viewButton = '<button' +
                                      ' id=' + '\"' + 'btn_view_turno_' + turno.id + '\"' +
                                      ' type="button" onclick="findTurnoBy('+turno.id+')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;">' +
                                      '👁️' +
                                      '</button>';

                // Formatear la fecha en formato argentino
                const fechaFormateada = formatearFecha(turno.fecha);

                turnoRow.innerHTML = '<td>' + turno.id + '</td>' +
                        '<td class=\"td_turno_paciente\">' + (turno.paciente?.nombre || '') + ' ' + (turno.paciente?.apellido || '') + '</td>' +
                        '<td class=\"td_turno_odontologo\">' + 'Dr/a. ' + (turno.odontologo?.nombre || '') + ' ' + (turno.odontologo?.apellido || '') + '</td>' +
                        '<td class=\"td_turno_fecha\">' + fechaFormateada + '</td>' +
                        '<td>' + viewButton + ' ' + deleteButton + '</td>';
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
            
            // Limpiar todos los campos primero
            document.getElementById('turno_id').value = '';
            document.getElementById('turno_paciente').value = '';
            document.getElementById('turno_odontologo').value = '';
            document.getElementById('turno_fecha').value = '';
            document.getElementById('turno_paciente_email').value = '';
            document.getElementById('turno_paciente_telefono').value = '';
            document.getElementById('turno_odontologo_matricula').value = '';
            
            // Llenar el modal con los datos del turno
            document.getElementById('turno_id').value = turno.id || '';
            
            // Formatear fecha para mostrar
            if (turno.fecha) {
                const fechaFormateada = formatearFecha(turno.fecha);
                document.getElementById('turno_fecha').value = fechaFormateada;
            }
            
            // Datos del paciente
            if (turno.paciente) {
                console.log('Datos del paciente:', turno.paciente);
                document.getElementById('turno_paciente').value = 
                    `${turno.paciente.nombre || ''} ${turno.paciente.apellido || ''}`.trim();
                document.getElementById('turno_paciente_email').value = turno.paciente.email || '';
                document.getElementById('turno_paciente_telefono').value = turno.paciente.numeroContacto || '';
            }
            
            // Datos del odontólogo
            if (turno.odontologo) {
                console.log('Datos del odontólogo:', turno.odontologo);
                document.getElementById('turno_odontologo').value = 
                    `Dr/a. ${turno.odontologo.nombre || ''} ${turno.odontologo.apellido || ''}`.trim();
                document.getElementById('turno_odontologo_matricula').value = turno.odontologo.matricula || '';
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

