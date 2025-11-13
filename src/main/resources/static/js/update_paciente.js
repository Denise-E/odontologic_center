// Función para formatear fecha a DD-MM-YYYY
function formatearFechaArgentina(fechaISO) {
    if (!fechaISO) return '';
    const fecha = new Date(fechaISO + 'T00:00:00'); // Asegurar que sea interpretada como local
    const dia = String(fecha.getDate()).padStart(2, '0');
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const anio = fecha.getFullYear();
    return `${dia}-${mes}-${anio}`;
}

function findBy(id) {
  const url = '/api/pacientes/' + id;

  fetch(url)
    .then(response => {
      if (!response.ok) throw new Error('No se encontró el paciente');
      return response.json();
    })
    .then(data => {
      // Cargar datos básicos del paciente
      document.getElementById('paciente_id').value = data.id;
      document.getElementById('nombre').value = data.nombre || '';
      document.getElementById('apellido').value = data.apellido || '';
      document.getElementById('numeroContacto').value = data.numeroContacto || '';
      
      // Formatear fecha de ingreso a formato argentino
      const fechaFormateada = formatearFechaArgentina(data.fechaIngreso);
      document.getElementById('fechaIngreso').value = fechaFormateada;
      
      document.getElementById('email').value = data.email || '';
      
      // Cargar datos del domicilio si existen
      if (data.domicilio) {
        document.getElementById('calle').value = data.domicilio.calle || '';
        document.getElementById('numero').value = data.domicilio.numero || '';
        document.getElementById('localidad').value = data.domicilio.localidad || '';
        document.getElementById('provincia').value = data.domicilio.provincia || '';
      } else {
        document.getElementById('calle').value = '';
        document.getElementById('numero').value = '';
        document.getElementById('localidad').value = '';
        document.getElementById('provincia').value = '';
      }

      // Abrir el modal de Bootstrap 5
      const modal = new bootstrap.Modal(document.getElementById('pacienteModal'));
      modal.show();
    })
    .catch(err => {
      console.error(err);
      alert('No se pudo cargar el paciente');
    });
}

// Función para actualizar el paciente
function updatePaciente() {
  const id = document.getElementById('paciente_id').value;
  
  const payload = {
    nombre: document.getElementById('nombre').value,
    apellido: document.getElementById('apellido').value,
    numeroContacto: document.getElementById('numeroContacto').value,
    fechaIngreso: document.getElementById('fechaIngreso').value,
    email: document.getElementById('email').value,
    domicilio: {
      calle: document.getElementById('calle').value,
      numero: document.getElementById('numero').value,
      localidad: document.getElementById('localidad').value,
      provincia: document.getElementById('provincia').value
    }
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
      // Actualizar fila en la tabla sin recargar
      const row = document.getElementById('tr_' + updated.id);
      if (row) {
        row.querySelector('.td_nombre').textContent = (updated.nombre || '').toUpperCase();
        row.querySelector('.td_apellido').textContent = (updated.apellido || '').toUpperCase();
        row.querySelector('.td_email').textContent = (updated.email || '');
      }
      
      // Cerrar el modal
      const modal = bootstrap.Modal.getInstance(document.getElementById('pacienteModal'));
      modal.hide();
      
      // Mostrar mensaje de éxito
      alert('Paciente actualizado exitosamente');
    })
    .catch(err => {
      console.error(err);
      alert('No se pudo actualizar el paciente');
    });
}


