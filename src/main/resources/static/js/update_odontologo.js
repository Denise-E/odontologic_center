function findOdBy(id) {
  const url = '/api/odontologos/' + id;

  fetch(url)
    .then(response => {
      if (!response.ok) throw new Error('No se encontró el odontólogo');
      return response.json();
    })
    .then(od => {
      document.getElementById('odontologo_id').value = od.id;
      document.getElementById('od_nombre').value = od.nombre || '';
      document.getElementById('od_apellido').value = od.apellido || '';
      document.getElementById('od_matricula').value = od.matricula || '';
      document.getElementById('od_requisitos').value = od.requisitosTurnos || '';

      document.getElementById('div_odontologo_updating').style.display = 'block';
    })
    .catch(err => {
      console.error(err);
      alert('No se pudo cargar el odontólogo');
    });
}

window.addEventListener('load', function () {
  const form = document.getElementById('update_odontologo_form');
  if (!form) return;

  form.addEventListener('submit', function (event) {
    event.preventDefault();

    const id = document.getElementById('odontologo_id').value;
    const payload = {
      nombre: document.getElementById('od_nombre').value,
      apellido: document.getElementById('od_apellido').value,
      matricula: document.getElementById('od_matricula').value,
      requisitosTurnos: document.getElementById('od_requisitos').value
    };

    fetch('/api/odontologos/' + id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    })
      .then(response => {
        if (!response.ok) throw new Error('Error al actualizar');
        return response.json();
      })
      .then(updated => {
        const row = document.getElementById('tr_od_' + updated.id);
        if (row) {
          row.querySelector('.td_od_nombre').textContent = (updated.nombre || '').toUpperCase();
          row.querySelector('.td_od_apellido').textContent = (updated.apellido || '').toUpperCase();
          row.querySelector('.td_od_matricula').textContent = (updated.matricula || '');
          row.querySelector('.td_od_requisitos').textContent = (updated.requisitosTurnos || '');
        }
        document.getElementById('div_odontologo_updating').style.display = 'none';
        form.reset();
      })
      .catch(err => {
        console.error(err);
        alert('No se pudo actualizar el odontólogo');
      });
  });
});


