window.addEventListener('load', function () {
  const form = document.getElementById('create_odontologo_form');
  if (!form) return;

  form.addEventListener('submit', function (event) {
    event.preventDefault();

    const payload = {
      nombre: document.getElementById('c_od_nombre').value,
      apellido: document.getElementById('c_od_apellido').value,
      matricula: document.getElementById('c_od_matricula').value,
      requisitosTurnos: document.getElementById('c_od_requisitos').value || null
    };

    fetch('/api/odontologos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
      .then(response => {
        if (!response.ok) throw new Error('Error al crear odontólogo');
        return response.json();
      })
      .then(created => {
        // agregar fila a la tabla
        const table = document.getElementById('odontologoTable');
        const row = table.insertRow();
        row.id = 'tr_od_' + created.id;

        const deleteButton = '<button id=\"btn_delete_od_' + created.id + '\" type="button" onclick="deleteOdBy(' + created.id + ')" class="btn btn-danger btn_delete">&times</button>';
        const updateButton = '<button id=\"btn_od_id_' + created.id + '\" type="button" onclick="findOdBy(' + created.id + ')" class="btn btn-info btn_id">' + created.id + '</button>';

        row.innerHTML = '<td>' + updateButton + '</td>' +
          '<td class=\"td_od_nombre\">' + (created.nombre || '').toUpperCase() + '</td>' +
          '<td class=\"td_od_apellido\">' + (created.apellido || '').toUpperCase() + '</td>' +
          '<td class=\"td_od_matricula\">' + (created.matricula || '') + '</td>' +
          '<td class=\"td_od_requisitos\">' + (created.requisitosTurnos || '') + '</td>' +
          '<td>' + deleteButton + '</td>';

        form.reset();
      })
      .catch(err => {
        console.error(err);
        alert('No se pudo crear el odontólogo');
      });
  });
});


