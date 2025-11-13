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
        // Si es el primer odontólogo, ocultar mensaje y mostrar tabla
        const noOdontologosMsg = document.getElementById('noOdontologosMessage');
        const tableContainer = document.getElementById('odontologosTableContainer');
        if (noOdontologosMsg.style.display !== 'none') {
          noOdontologosMsg.style.display = 'none';
          tableContainer.style.display = 'flex';
        }

        // agregar fila a la tabla
        const tbody = document.getElementById('odontologoTableBody');
        const row = tbody.insertRow();
        row.id = 'tr_od_' + created.id;

        const deleteButton = '<button id="btn_delete_od_' + created.id + '" type="button" onclick="deleteOdBy(' + created.id + ')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;">' +
          '🗑️' +
          '</button>';

        const viewButton = '<button id="btn_view_od_' + created.id + '" type="button" onclick="findOdBy(' + created.id + ')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;">' +
          '👁️' +
          '</button>';

        row.innerHTML = '<td>' + created.id + '</td>' +
          '<td class="td_od_nombre">' + (created.nombre || '').toUpperCase() + '</td>' +
          '<td class="td_od_apellido">' + (created.apellido || '').toUpperCase() + '</td>' +
          '<td class="td_od_matricula">' + (created.matricula || '') + '</td>' +
          '<td class="td_od_requisitos">' + (created.requisitosTurnos || '') + '</td>' +
          '<td>' + viewButton + ' ' + deleteButton + '</td>';

        form.reset();
        console.log('Odontólogo creado exitosamente:', created);
      })
      .catch(err => {
        console.error('Error al crear odontólogo:', err);
      });
  });
});


