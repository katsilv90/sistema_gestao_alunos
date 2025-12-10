document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('form-aluno');
    const tabela = document.getElementById('tabela-alunos');
    const contador = document.getElementById('contador-alunos');
    const filtro = document.getElementById('filtro-nome');

    // Contagem inicial de alunos
    atualizarContador();

    // Validação simples do formulário de alunos
    if (form) {
        form.addEventListener('submit', (e) => {
            const nome = form.nome.value.trim();
            if (!nome) {
                e.preventDefault();
                alert('Por favor, insira o nome do aluno.');
            }
        });
    }

    // Filtro instantâneo por nome (apenas para alunos)
    if (filtro) {
        filtro.addEventListener('input', () => {
            const valorFiltro = filtro.value.toLowerCase();
            Array.from(tabela.querySelectorAll('tbody tr')).forEach(tr => {
                const nomeAluno = tr.querySelector('td').textContent.toLowerCase();
                tr.style.display = nomeAluno.includes(valorFiltro) ? '' : 'none';
            });
            atualizarContador();
        });
    }

    // Configuração da remoção de entidades (aluno, curso, uc)
    configurarRemocaoEntidades();

    function configurarRemocaoEntidades() {
        document.addEventListener('click', function(e) {
            if (e.target.classList.contains('btn-remover')) {
                const id = e.target.getAttribute('data-id');
                const nome = e.target.getAttribute('data-nome');

                if (id && nome) {
                    const tipo = detectarTipoPagina();
                    confirmarRemocao(tipo, id, nome);
                } else {
                    console.error('Dados não encontrados');
                    mostrarToast('Erro: Dados não encontrados', 'error');
                }
            }
        });
    }

    function detectarTipoPagina() {
        const path = window.location.pathname;
        if (path.includes("/alunos")) return "aluno";
        if (path.includes("/cursos")) return "curso";
        if (path.includes("/ucs")) return "uc";
        return "entidade";
    }

    function confirmarRemocao(tipo, id, nome) {
        abrirPopupConfirmacao(tipo, id, nome);
    }

    function abrirPopupConfirmacao(tipo, id, nome) {
        const popup = document.getElementById('popup-confirmacao');
        const mensagem = document.getElementById('popup-mensagem');
        const btnConfirmar = document.getElementById('btn-confirmar');
        const btnCancelar = document.getElementById('btn-cancelar');

        mensagem.textContent = `Tem certeza que deseja remover o ${tipo} "${nome}"?`;
        popup.style.display = 'flex';

        btnConfirmar.onclick = () => {
            popup.style.display = 'none';
            removerEntidade(tipo, id, nome);
        };

        btnCancelar.onclick = () => {
            popup.style.display = 'none';
        };
    }

    function removerEntidade(tipo, id, nome) {
        console.log(`Removendo ${tipo}: ${nome} (${id})`);

        const botoes = document.querySelectorAll('.btn-remover');
        botoes.forEach(btn => btn.disabled = true);

        // 🔹 Usando PathVariable para todos os tipos
        const url = `/${tipo}s/remover?id=${encodeURIComponent(id)}`;

        fetch(url, { method: 'GET' })
        .then(response => {
            if (response.ok) {
                mostrarToast(`${tipo.charAt(0).toUpperCase() + tipo.slice(1)} "${nome}" removido com sucesso!`, 'success');
                setTimeout(() => {
                    window.location.reload();
                }, 3000);
            } else {
                throw new Error(`Erro HTTP: ${response.status}`);
            }
        })
        .catch(error => {
            console.error('Erro na remoção:', error);
            mostrarToast(`Erro ao remover ${tipo}. Tente novamente.`, 'error');
            botoes.forEach(btn => btn.disabled = false);
        });
    }

    function atualizarContador() {
        if (contador && tabela) {
            const linhasVisiveis = Array.from(tabela.querySelectorAll('tbody tr'))
                .filter(tr => tr.style.display !== 'none').length;
            const total = tabela.querySelectorAll('tbody tr').length;

            if (linhasVisiveis === total) {
                contador.textContent = `Total de alunos: ${total}`;
            } else {
                contador.textContent = `Mostrando ${linhasVisiveis} de ${total} alunos`;
            }
        }
    }

    function mostrarToast(mensagem, tipo = 'success') {
        if (typeof window.mostrarToast === 'function') {
            window.mostrarToast(mensagem, tipo);
            return;
        }

        const toast = document.createElement('div');
        toast.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            background: ${tipo === 'error' ? '#e74c3c' : '#2ecc71'};
            color: white;
            padding: 15px 25px;
            border-radius: 8px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            z-index: 1000;
            font-weight: bold;
        `;
        toast.textContent = mensagem;

        document.body.appendChild(toast);

        setTimeout(() => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 5000);
    }

    // Exibir toast se vier mensagem do backend
    const toastDiv = document.getElementById("toast-data");
    if (toastDiv) {
        const mensagem = toastDiv.dataset.mensagem;
        const tipo = toastDiv.dataset.tipo || "success";

        if (mensagem) {
            mostrarToast(mensagem, tipo);
        }
    }

    // Destacar link ativo na navbar
    document.addEventListener('DOMContentLoaded', function() {
            const currentPath = window.location.pathname;
            const navLinks = document.querySelectorAll('.nav-link');

            navLinks.forEach(link => {
                if (link.getAttribute('href') === currentPath) {
                    link.classList.add('active');
                }
            });
    });

    // Função dedicada para remover Unidade Curricular (UC) na lista de UCs
    function removerUC(id, nome) {
        console.log(`Removendo UC: ${nome} (${id})`);

        const botoesUC = document.querySelectorAll('.btn-remover-uc');
        botoesUC.forEach(btn => btn.disabled = true);

        const url = `/ucs/remover?id=${encodeURIComponent(id)}`;

        fetch(url, { method: 'GET' })
            .then(response => {
                if (response.ok) {
                    mostrarToast(`UC "${nome}" removida com sucesso!`, 'success');
                    // Remove do DOM imediatamente sem recarregar a página
                    const li = document.querySelector(`.item-uc button[data-id='${id}']`)?.closest('li');
                    if (li) li.remove();
                } else {
                    throw new Error(`Erro HTTP: ${response.status}`);
                }
            })
            .catch(error => {
                console.error('Erro na remoção da UC:', error);
                mostrarToast(`Erro ao remover UC. Tente novamente.`, 'error');
                botoesUC.forEach(btn => btn.disabled = false);
            });
    }

    // Configuração específica para UCs
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-remover-uc')) {
            const id = e.target.getAttribute('data-id');
            const nome = e.target.getAttribute('data-nome');
            if (id && nome) {
                removerUC(id, nome);
            } else {
                mostrarToast('Erro: Dados da UC não encontrados', 'error');
            }
        }
    });

});
