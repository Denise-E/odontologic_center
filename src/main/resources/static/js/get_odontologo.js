window.addEventListener('load', function () {
    (function(){

      const url = '/api/odontologos';
      const settings = {
        method: 'GET'
      }

      fetch(url,settings)
      .then(response => response.json())
      .then(data => {

         for(od of data){
            var table = document.getElementById("odontologoTable");
            var row = table.insertRow();
            let tr_id = 'tr_od_' + od.id;
            row.id = tr_id;

            let deleteButton = '<button' +
                                      ' id=' + '\"' + 'btn_delete_od_' + od.id + '\"' +
                                      ' type="button" onclick="deleteOdBy('+od.id+')" class="btn btn-link text-danger p-0" style="font-size: 1.2rem;">' +
                                      '🗑️' +
                                      '</button>';

            let viewButton = '<button' +
                                      ' id=' + '\"' + 'btn_view_od_' + od.id + '\"' +
                                      ' type="button" onclick="findOdBy('+od.id+')" class="btn btn-link text-primary p-0" style="font-size: 1.2rem;">' +
                                      '👁️' +
                                      '</button>';

            row.innerHTML = '<td>' + od.id + '</td>' +
                    '<td class=\"td_od_nombre\">' + (od.nombre || '').toUpperCase() + '</td>' +
                    '<td class=\"td_od_apellido\">' + (od.apellido || '').toUpperCase() + '</td>' +
                    '<td class=\"td_od_matricula\">' + (od.matricula || '') + '</td>' +
                    '<td class=\"td_od_requisitos\">' + (od.requisitosTurnos || '') + '</td>' +
                    '<td>' + viewButton + ' ' + deleteButton + '</td>';

        };

    })
    })
});


