window.addEventListener('load', function () {
    cargarOdontologos();
});

function cargarOdontologos() {
    const url = '/api/odontologos';
    const settings = {
        method: 'GET'
    };

    fetch(url, settings)
        .then(response => response.json())
        .then(data => {
            console.log('Odontólogos recibidos del servidor:', data);

            // Verificar si hay odontólogos
            if (data.length === 0) {
                // Mostrar mensaje, ocultar tabla
                document.getElementById('noOdontologosMessage').style.display = 'block';
                document.getElementById('odontologosTableContainer').style.display = 'none';
                return;
            } else {
                // Mostrar tabla, ocultar mensaje
                document.getElementById('noOdontologosMessage').style.display = 'none';
                document.getElementById('odontologosTableContainer').style.display = 'flex';
            }

            // Limpiar el tbody antes de agregar las filas
            const tbody = document.getElementById('odontologoTableBody');
            tbody.innerHTML = '';

            for (od of data) {
                console.log('Procesando odontólogo:', od);
                var row = tbody.insertRow();
                let tr_id = 'tr_od_' + od.id;
                row.id = tr_id;

                let deleteButton = '<button' +
                    ' id=' + '\"' + 'btn_delete_od_' + od.id + '\"' +
                    ' type="button" onclick="deleteOdBy(' + od.id + ')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;" title="Eliminar odontólogo">' +
                    '🗑️' +
                    '</button>';

                let viewButton = '<button' +
                    ' id=' + '\"' + 'btn_view_od_' + od.id + '\"' +
                    ' type="button" onclick="findOdBy(' + od.id + ')" class="btn btn-link p-0 me-2" style="font-size: 1.2rem; text-decoration: none;" title="Ver/editar odontólogo">' +
                    '👁️' +
                    '</button>';

                row.innerHTML = '<td>' + od.id + '</td>' +
                    '<td class=\"td_od_nombre\">' + (od.nombre || '').toUpperCase() + '</td>' +
                    '<td class=\"td_od_apellido\">' + (od.apellido || '').toUpperCase() + '</td>' +
                    '<td class=\"td_od_matricula\">' + (od.matricula || '') + '</td>' +
                    '<td class=\"td_od_requisitos\">' + (od.requisitosTurnos || '') + '</td>' +
                    '<td>' + viewButton + ' ' + deleteButton + '</td>';
            }
        })
        .catch(error => {
            console.error('Error al cargar odontólogos:', error);
            document.getElementById('noOdontologosMessage').style.display = 'block';
            document.getElementById('odontologosTableContainer').style.display = 'none';
        });
}
