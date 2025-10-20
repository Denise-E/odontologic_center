function deleteBy(id) {
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
    })
    .catch(err => {
      console.error(err);
      alert('No se pudo eliminar el paciente');
    });
}


