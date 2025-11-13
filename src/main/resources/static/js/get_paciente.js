window.addEventListener('load', function () {
    cargarPacientes();
});

function cargarPacientes() {
    const url = '/api/pacientes';
    const settings = {
        method: 'GET'
    };

    fetch(url, settings)
        .then(response => response.json())
        .then(data => {
            console.log('Pacientes recibidos del servidor:', data);

            // Verificar si hay pacientes
            if (data.length === 0) {
                // Mostrar mensaje, ocultar tabla
                document.getElementById('noPacientesMessage').style.display = 'block';
                document.getElementById('pacientesTableContainer').style.display = 'none';
                return;
            } else {
                // Mostrar tabla, ocultar mensaje
                document.getElementById('noPacientesMessage').style.display = 'none';
                document.getElementById('pacientesTableContainer').style.display = 'flex';
            }

            // Limpiar el tbody antes de agregar las filas
            const tbody = document.getElementById('pacienteTableBody');
            tbody.innerHTML = '';

            for (paciente of data) {
                console.log('Procesando paciente:', paciente);
                var pacienteRow = tbody.insertRow();
                let tr_id = 'tr_' + paciente.id;
                pacienteRow.id = tr_id;

                let deleteButton = '<button' +
                    ' id=' + '\"' + 'btn_delete_' + paciente.id + '\"' +
                    ' type="button" onclick="deleteBy(' + paciente.id + ')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;" title="Eliminar paciente">' +
                    '🗑️' +
                    '</button>';

                let viewButton = '<button' +
                    ' id=' + '\"' + 'btn_view_' + paciente.id + '\"' +
                    ' type="button" onclick="findBy(' + paciente.id + ')" class="btn btn-link p-0 me-2" style="font-size: 1.2rem; text-decoration: none;" title="Ver/editar paciente">' +
                    '👁️' +
                    '</button>';

                pacienteRow.innerHTML = '<td>' + paciente.id + '</td>' +
                    '<td class=\"td_nombre\">' + (paciente.nombre || '').toUpperCase() + '</td>' +
                    '<td class=\"td_apellido\">' + (paciente.apellido || '').toUpperCase() + '</td>' +
                    '<td class=\"td_email\">' + (paciente.email || '') + '</td>' +
                    '<td>' + viewButton + ' ' + deleteButton + '</td>';
            }
        })
        .catch(error => {
            console.error('Error al cargar pacientes:', error);
            document.getElementById('noPacientesMessage').style.display = 'block';
            document.getElementById('pacientesTableContainer').style.display = 'none';
        });
}
