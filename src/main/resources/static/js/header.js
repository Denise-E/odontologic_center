document.addEventListener('DOMContentLoaded', function() {
  var placeholder = document.getElementById('app-header');
  if (!placeholder) return;
  fetch('partials/header.html')
    .then(function(resp) { return resp.text(); })
    .then(function(html) {
      placeholder.innerHTML = html;
    })
    .catch(function() {
      console.error('No se pudo cargar el header');
    });
});


