function findBy(id) {
  const url = '/api/pacientes/' + id;

  fetch(url)
    .then(response => {
      if (!response.ok) throw new Error('No se encontró el paciente');
      return response.json();
    })
    .then(data => {
      document.getElementById('paciente_id').value = data.id;
      document.getElementById('nombre').value = data.nombre || '';
      document.getElementById('apellido').value = data.apellido || '';
      document.getElementById('numeroContacto').value = data.numeroContacto || '';
      // fechaIngreso es LocalDate en backend, formato yyyy-MM-dd esperado por input date
      document.getElementById('fechaIngreso').value = data.fechaIngreso || '';
      document.getElementById('email').value = data.email || '';

      document.getElementById('div_paciente_updating').style.display = 'block';
    })
    .catch(err => {
      console.error(err);
      alert('No se pudo cargar el paciente');
    });
}

// submit del formulario para actualizar
window.addEventListener('load', function () {
  const form = document.getElementById('update_paciente_form');
  if (!form) return;

  form.addEventListener('submit', function (event) {
    event.preventDefault();

    const id = document.getElementById('paciente_id').value;
    const payload = {
      nombre: document.getElementById('nombre').value,
      apellido: document.getElementById('apellido').value,
      numeroContacto: document.getElementById('numeroContacto').value,
      fechaIngreso: document.getElementById('fechaIngreso').value,
      email: document.getElementById('email').value
    };

    fetch('/api/pacientes/' + id, {
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
        // actualizar fila en la tabla sin recargar
        const row = document.getElementById('tr_' + updated.id);
        if (row) {
          row.querySelector('.td_titulo').textContent = (updated.nombre || '').toUpperCase();
          row.querySelector('.td_categoria').textContent = (updated.apellido || '').toUpperCase();
        }
        document.getElementById('div_paciente_updating').style.display = 'none';
        form.reset();
      })
      .catch(err => {
        console.error(err);
        alert('No se pudo actualizar el paciente');
      });
  });
});


