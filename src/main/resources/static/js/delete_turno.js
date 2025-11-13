function deleteTurnoBy(id) {
    if (!confirm('¿Está seguro que desea eliminar este turno?')) {
        return;
    }

    const url = '/api/turnos/' + id;
    const settings = {
        method: 'DELETE'
    };

    fetch(url, settings)
        .then(response => {
            if (response.ok) {
                // Eliminar la fila de la tabla
                const row = document.getElementById('tr_turno_' + id);
                if (row) {
                    row.remove();
                }
                
                // Verificar si quedan turnos en la tabla
                const tbody = document.getElementById('turnoTableBody');
                if (tbody.rows.length === 0) {
                    document.getElementById('noTurnosMessage').style.display = 'block';
                    document.getElementById('turnosTableContainer').style.display = 'none';
                }
                
                alert('Turno eliminado exitosamente');
            } else {
                return response.json().then(err => {
                    throw new Error(err.message || 'Error al eliminar el turno');
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('No se pudo eliminar el turno: ' + error.message);
        });
}

