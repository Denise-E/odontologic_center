function deleteOdBy(id) {
  // Confirmación antes de eliminar
  if (!confirm('¿Está seguro que desea eliminar este odontólogo?\n\nEsta acción no se puede deshacer.')) {
    return;
  }

  const url = '/api/odontologos/' + id;
  const settings = {
    method: 'DELETE'
  };

  fetch(url, settings)
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al eliminar odontólogo');
      }
      const rowId = 'tr_od_' + id;
      const row = document.getElementById(rowId);
      if (row && row.parentNode) {
        row.parentNode.removeChild(row);
      }
      alert('Odontólogo eliminado exitosamente');
    })
    .catch(err => {
      console.error(err);
      alert('No se pudo eliminar el odontólogo');
    });
}


