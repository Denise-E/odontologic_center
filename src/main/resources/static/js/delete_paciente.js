function deleteBy(id) {
  // Confirmación antes de eliminar
  if (!confirm('¿Está seguro que desea eliminar este paciente?\n\nEsta acción no se puede deshacer.')) {
    return; // Si el usuario cancela, no hacer nada
  }

  const url = '/api/pacientes/' + id;
  const settings = {
    method: 'DELETE'
  };

  fetch(url, settings)
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al eliminar paciente');
      }
      // eliminar fila de la tabla
      const rowId = 'tr_' + id;
      const row = document.getElementById(rowId);
      if (row && row.parentNode) {
        row.parentNode.removeChild(row);
      }
      // Mostrar mensaje de éxito
      alert('Paciente eliminado exitosamente');
    })
    .catch(err => {
      console.error(err);
      alert('No se pudo eliminar el paciente');
    });
}


