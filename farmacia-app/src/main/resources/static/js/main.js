// static/js/main.js

document.addEventListener('DOMContentLoaded', function() {
    // --- Lógica do Menu Lateral (Sidebar) ---
    const openSidebarBtn = document.getElementById('openSidebarBtn');
    const closeSidebarBtn = document.getElementById('closeSidebarBtn');
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('overlay');
    const mainContent = document.getElementById('main-content');

    function openSidebar() {
        sidebar.classList.add('active');
        overlay.classList.add('active');
        document.body.classList.add('sidebar-open');
        if (mainContent) {
            mainContent.classList.add('blurred');
        }
    }

    function closeSidebar() {
        sidebar.classList.remove('active');
        overlay.classList.remove('active');
        document.body.classList.remove('sidebar-open');
        if (mainContent) {
            mainContent.classList.remove('blurred');
        }
    }

    if (openSidebarBtn) {
        openSidebarBtn.addEventListener('click', openSidebar);
    }
    if (closeSidebarBtn) {
        closeSidebarBtn.addEventListener('click', closeSidebar);
    }
    if (overlay) {
        overlay.addEventListener('click', closeSidebar);
    }

    // --- Lógica de Expansão/Recolha de Submenus no Sidebar ---
    const submenuToggles = document.querySelectorAll('#sidebar .submenu-toggle');

    submenuToggles.forEach(toggle => {
        toggle.addEventListener('click', function(event) {
            event.preventDefault(); 
            const parentCategory = this.closest('.sidebar-category');
            if (parentCategory) {
                parentCategory.classList.toggle('active');
            }
        });
    });

    // --- Lógica do Indicador de Carregamento (Loading Spinner) ---
    const loadingSpinner = document.getElementById('loading-spinner');

    if (loadingSpinner) {
        // Para requisições HTMX (se você usar)
        document.body.addEventListener('htmx:beforeRequest', function() {
            loadingSpinner.classList.add('show');
        });
        document.body.addEventListener('htmx:afterRequest', function() {
            loadingSpinner.classList.remove('show');
        });

        // Para navegação normal
        window.addEventListener('beforeunload', function() {
            loadingSpinner.classList.add('show');
        });
        // Esconde o spinner no page load, caso ele tenha ficado preso
        window.addEventListener('load', function() {
            loadingSpinner.classList.remove('show');
        });
    }
    
    // --- Lógica Aprimorada para Mensagens Flash (Alertas) ---
    const flashMessagesContainer = document.querySelector('.flash-messages');
    if (flashMessagesContainer) {
        const alerts = flashMessagesContainer.querySelectorAll('.alert');
        alerts.forEach((alert, index) => {
            setTimeout(() => {
                alert.classList.add('show');
            }, 100 + (index * 50));

            setTimeout(() => {
                alert.classList.remove('show');
                alert.addEventListener('transitionend', () => alert.remove(), { once: true });
            }, 4000 + (index * 50));
        });
    }

    // --- NOVO: Lógica de Animação de Entrada ao Rolar ---
    const animatedElements = document.querySelectorAll('.product-card, .content-layout');
    
    if (window.IntersectionObserver) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    // Adiciona a classe 'visible' para acionar a animação CSS
                    entry.target.classList.add('visible');
                    // Opcional: para de observar o elemento após a animação para economizar recursos
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.1 // A animação começa quando 10% do elemento está visível
        });
    
        animatedElements.forEach(el => {
            observer.observe(el);
        });
    } else {
        // Fallback para navegadores antigos que não suportam IntersectionObserver
        // Apenas torna os elementos visíveis sem animação.
        animatedElements.forEach(el => {
            el.classList.add('visible');
        });
    }

});
