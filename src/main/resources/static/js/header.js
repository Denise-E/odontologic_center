document.addEventListener('DOMContentLoaded', function() {
  var placeholder = document.getElementById('app-header');
  if (!placeholder) return;
  fetch('partials/header.html')
    .then(function(resp) { return resp.text(); })
    .then(function(html) {
      placeholder.innerHTML = html;
      
      // Activar la navegación activa después de cargar el header
      activarNavegacionActiva();
    })
    .catch(function() {
      console.error('No se pudo cargar el header');
    });
});

function activarNavegacionActiva() {
  // Obtener el nombre del archivo actual
  const paginaActual = window.location.pathname.split('/').pop() || 'index.html';
  
  // Buscar todos los enlaces del navbar
  const enlaces = document.querySelectorAll('.navbar-nav .nav-link');
  
  enlaces.forEach(function(enlace) {
    // Obtener las páginas asociadas a este enlace
    const paginasAsociadas = enlace.getAttribute('data-page');
    
    if (paginasAsociadas) {
      // Separar las páginas si hay múltiples (separadas por coma)
      const paginas = paginasAsociadas.split(',');
      
      // Verificar si la página actual está en la lista
      if (paginas.includes(paginaActual)) {
        enlace.classList.add('active');
        enlace.style.fontWeight = 'bold';
        enlace.style.color = '#0d6efd'; // Color azul de Bootstrap
      }
    }
  });
}


