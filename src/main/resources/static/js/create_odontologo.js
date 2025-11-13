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
        if (!response.ok) {
          // Intentar leer el mensaje de error del servidor
          return response.text().then(text => {
            throw new Error(text || 'Error al crear odontólogo');
          });
        }
        return response.json();
      })
      .then(created => {
        const alertError = document.getElementById('alert-error-odontologo');
        if (alertError) {
          alertError.style.display = 'none';
        }
        
        // Si es el primer odontólogo, ocultar mensaje y mostrar tabla
        const noOdontologosMsg = document.getElementById('noOdontologosMessage');
        const tableContainer = document.getElementById('odontologosTableContainer');
        if (noOdontologosMsg && noOdontologosMsg.style.display !== 'none') {
          noOdontologosMsg.style.display = 'none';
          if (tableContainer) {
            tableContainer.style.display = 'flex';
          }
        }

        // agregar fila a la tabla
        const tbody = document.getElementById('odontologoTableBody');
        if (tbody) {
          const row = tbody.insertRow();
          row.id = 'tr_od_' + created.id;

          const viewButton = '<button id="btn_view_od_' + created.id + '" type="button" onclick="findOdBy(' + created.id + ')" class="btn btn-link p-0 me-2" style="font-size: 1.2rem; text-decoration: none;" title="Ver/editar odontólogo">' +
            '👁️' +
            '</button>';

          const deleteButton = '<button id="btn_delete_od_' + created.id + '" type="button" onclick="deleteOdBy(' + created.id + ')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;" title="Eliminar odontólogo">' +
            '🗑️' +
            '</button>';

          row.innerHTML = '<td>' + created.id + '</td>' +
            '<td class="td_od_nombre">' + (created.nombre || '').toUpperCase() + '</td>' +
            '<td class="td_od_apellido">' + (created.apellido || '').toUpperCase() + '</td>' +
            '<td class="td_od_matricula">' + (created.matricula || '') + '</td>' +
            '<td class="td_od_requisitos">' + (created.requisitosTurnos || '') + '</td>' +
            '<td>' + viewButton + ' ' + deleteButton + '</td>';
        }

        const alertSuccess = document.getElementById('alert-success-odontologo');
        if (alertSuccess) {
          alertSuccess.style.display = 'block';
          
          // Ocultar después de 3 segundos
          setTimeout(() => {
            if (alertSuccess) {
              alertSuccess.style.display = 'none';
            }
          }, 3000);
        }

        form.reset();
        console.log('Odontólogo creado exitosamente:', created);
      })
      .catch(err => {
        console.error('Error al crear odontólogo:', err);
        
        // Mostrar error en la UI
        const alertError = document.getElementById('alert-error-odontologo');
        const errorMessage = document.getElementById('error-message-odontologo');
        
        if (alertError && errorMessage) {
          // Extraer el mensaje de error limpio
          let mensaje = err.message || 'Error desconocido al crear odontólogo';
          
          // Limpiar el mensaje si viene con "Error: " al principio
          if (mensaje.startsWith('Error: ')) {
            mensaje = mensaje.substring(7);
          }
          
          errorMessage.textContent = mensaje;
          alertError.style.display = 'block';
          
          // Hacer scroll al error
          alertError.scrollIntoView({ behavior: 'smooth', block: 'center' });
        } else {
          // Fallback si no hay elementos de alerta
          alert('Error al crear odontólogo: ' + (err.message || 'Error desconocido'));
        }
      });
  });
});


