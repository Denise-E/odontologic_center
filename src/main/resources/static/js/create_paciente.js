window.addEventListener('load', function () {
  const form = document.getElementById('create_paciente_form');
  if (!form) return;

  // Validación en tiempo real para el campo de número de contacto
  const numeroContactoInput = document.getElementById('c_numeroContacto');
  if (numeroContactoInput) {
    numeroContactoInput.addEventListener('input', function (e) {
      // Solo permitir números (0-9)
      this.value = this.value.replace(/[^0-9]/g, '');
    });
  }

  form.addEventListener('submit', function (event) {
    event.preventDefault();

    // Obtener fecha actual en formato yyyy-MM-dd
    const today = new Date();
    const fechaIngreso = today.getFullYear() + '-' + 
                         String(today.getMonth() + 1).padStart(2, '0') + '-' + 
                         String(today.getDate()).padStart(2, '0');

    const payload = {
      nombre: document.getElementById('c_nombre').value,
      apellido: document.getElementById('c_apellido').value,
      numeroContacto: document.getElementById('c_numeroContacto').value,
      fechaIngreso: fechaIngreso,
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
        if (!response.ok) {
          // Intentar leer el mensaje de error del servidor
          return response.text().then(text => {
            throw new Error(text || 'Error al crear paciente');
          });
        }
        return response.json();
      })
      .then(created => {
        const alertError = document.getElementById('alert-error-paciente');
        if (alertError) {
          alertError.style.display = 'none';
        }
        
        // Si es el primer paciente, ocultar mensaje y mostrar tabla
        const noPacientesMsg = document.getElementById('noPacientesMessage');
        const tableContainer = document.getElementById('pacientesTableContainer');
        if (noPacientesMsg && noPacientesMsg.style.display !== 'none') {
          noPacientesMsg.style.display = 'none';
          if (tableContainer) {
            tableContainer.style.display = 'flex';
          }
        }

        // agregar fila a la tabla
        const tbody = document.getElementById('pacienteTableBody');
        if (tbody) {
          const row = tbody.insertRow();
          row.id = 'tr_' + created.id;

          const viewButton = '<button id="btn_view_' + created.id + '" type="button" onclick="findBy(' + created.id + ')" class="btn btn-link p-0 me-2" style="font-size: 1.2rem; text-decoration: none;" title="Ver/editar paciente">' +
            '👁️' +
            '</button>';

          const deleteButton = '<button id="btn_delete_' + created.id + '" type="button" onclick="deleteBy(' + created.id + ')" class="btn btn-link p-0" style="font-size: 1.2rem; text-decoration: none;" title="Eliminar paciente">' +
            '🗑️' +
            '</button>';

          row.innerHTML = '<td>' + created.id + '</td>' +
            '<td class="td_nombre">' + (created.nombre || '').toUpperCase() + '</td>' +
            '<td class="td_apellido">' + (created.apellido || '').toUpperCase() + '</td>' +
            '<td class="td_email">' + (created.email || '') + '</td>' +
            '<td>' + viewButton + ' ' + deleteButton + '</td>';
        }

        const alertSuccess = document.getElementById('alert-success-paciente');
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
        console.log('Paciente creado exitosamente:', created);
      })
      .catch(err => {
        console.error('Error al crear paciente:', err);
        
        // Mostrar error en la UI
        const alertError = document.getElementById('alert-error-paciente');
        const errorMessage = document.getElementById('error-message-paciente');
        
        if (alertError && errorMessage) {
          // Extraer el mensaje de error limpio
          let mensaje = err.message || 'Error desconocido al crear paciente';
          
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
          alert('Error al crear paciente: ' + (err.message || 'Error desconocido'));
        }
      });
  });
});


