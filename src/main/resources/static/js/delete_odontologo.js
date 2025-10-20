function deleteOdBy(id) {
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
    })
    .catch(err => {
      console.error(err);
      alert('No se pudo eliminar el odontólogo');
    });
}


