window.addEventListener('load', function () {
    (function(){

      const url = '/api/pacientes';
      const settings = {
        method: 'GET'
      }

      fetch(url,settings)
      .then(response => response.json())
      .then(data => {

         for(paciente of data){
            //por cada paciente armaremos una fila de la tabla
            var table = document.getElementById("pacienteTable");
            var pacienteRow =table.insertRow();
            let tr_id = 'tr_' + paciente.id;
            pacienteRow.id = tr_id;

            // Botón de eliminar con ícono de tacho
            let deleteButton = '<button' +
                                      ' id=' + '\"' + 'btn_delete_' + paciente.id + '\"' +
                                      ' type="button" onclick="deleteBy('+paciente.id+')" class="btn btn-danger btn-sm">' +
                                      '<i class="bi bi-trash"></i>' +
                                      '</button>';

            // Botón de ver/editar con ícono de ojo
            let viewButton = '<button' +
                                      ' id=' + '\"' + 'btn_view_' + paciente.id + '\"' +
                                      ' type="button" onclick="findBy('+paciente.id+')" class="btn btn-primary btn-sm">' +
                                      '<i class="bi bi-eye"></i>' +
                                      '</button>';

            pacienteRow.innerHTML = '<td>' + paciente.id + '</td>' +
                    '<td class=\"td_nombre\">' + (paciente.nombre || '').toUpperCase() + '</td>' +
                    '<td class=\"td_apellido\">' + (paciente.apellido || '').toUpperCase() + '</td>' +
                    '<td class=\"td_email\">' + (paciente.email || '') + '</td>' +
                    '<td>' + viewButton + ' ' + deleteButton + '</td>';

        };

    })
    })

    })