window.addEventListener('load', function () {
  const form = document.getElementById('create_paciente_form');
  if (!form) return;

  form.addEventListener('submit', function (event) {
    event.preventDefault();

    const payload = {
      nombre: document.getElementById('c_nombre').value,
      apellido: document.getElementById('c_apellido').value,
      numeroContacto: document.getElementById('c_numeroContacto').value,
      fechaIngreso: document.getElementById('c_fechaIngreso').value,
      email: document.getElementById('c_email').value,
      domicilio: {
        calle: document.getElementById('c_calle').value,
        numero: parseInt(document.getElementById('c_numero').value, 10),
        localidad: document.getElementById('c_localidad').value,
        provincia: document.getElementById('c_provincia').value
      }
    };

    fetch('/api/pacientes', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
      .then(response => {
        if (!response.ok) throw new Error('Error al crear paciente');
        return response.json();
      })
      .then(created => {
        // agregar fila a la tabla
        const table = document.getElementById('pacienteTable');
        const row = table.insertRow();
        row.id = 'tr_' + created.id;

        const deleteButton = '<button id=\"btn_delete_' + created.id + '\" type="button" onclick="deleteBy(' + created.id + ')" class="btn btn-danger btn_delete">&times</button>';
        const updateButton = '<button id=\"btn_id_' + created.id + '\" type="button" onclick="findBy(' + created.id + ')" class="btn btn-info btn_id">' + created.id + '</button>';

        row.innerHTML = '<td>' + updateButton + '</td>' +
          '<td class=\"td_titulo\">' + (created.nombre || '').toUpperCase() + '</td>' +
          '<td class=\"td_categoria\">' + (created.apellido || '').toUpperCase() + '</td>' +
          '<td>' + deleteButton + '</td>';

        form.reset();
      })
      .catch(err => {
        console.error(err);
        alert('No se pudo crear el paciente');
      });
  });
});


