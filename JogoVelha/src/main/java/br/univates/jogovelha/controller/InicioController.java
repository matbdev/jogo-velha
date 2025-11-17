package br.univates.jogovelha.controller;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.util.Messages;
import br.univates.jogovelha.controller.historico.HistoricoController;
import br.univates.jogovelha.controller.jogador.VisualizarUsuariosController;
import br.univates.jogovelha.controller.partida.SelecaoJogadoresController;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.view.PainelInicial;
import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Controller para o PainelInicial.
 * Gerencia a navegação dos botões da tela principal.
 */
public class InicioController {
    private final PainelInicial view;
    private final JPanel painelPrincipal;
    private final CardLayout cardLayout;
    
    // DAOs
    private final IDao<Jogador, String> jogadorDao;
    
    // Controllers
    private final HistoricoController historicoController;
    private final VisualizarUsuariosController visualizarController;
    private final SelecaoJogadoresController selecaoController;

    // Construtor
    public InicioController(
            PainelInicial view, JPanel painelPrincipal, 
            IDao<Jogador, String> jogadorDao, 
            VisualizarUsuariosController visualizarController, 
            HistoricoController historicoController, 
            SelecaoJogadoresController selecaoController
    ) {
        
        this.view = view;
        this.painelPrincipal = painelPrincipal;
        this.cardLayout = (CardLayout) painelPrincipal.getLayout();
        
        this.jogadorDao = jogadorDao;
        this.visualizarController = visualizarController;
        this.historicoController = historicoController;
        this.selecaoController = selecaoController;

        // Ações dos botões
        this.view.adicionarAcaoBotaoSair(e -> {
            System.exit(0);
            Messages.infoMessage(painelPrincipal, "Saindo do programa...");
        });
        this.view.adicionarAcaoBotaoJogadores(e -> navegarParaJogadores());
        this.view.adicionarAcaoBotaoHistorico(e -> navegarParaHistorico());
        this.view.adicionarAcaoBotaoIniciar(e -> navegarParaIniciar());
    }

    /**
     * Navega para a tela de visualização de jogadores.
     * Verifica se há jogadores antes de navegar.
     */
    private void navegarParaJogadores() {
        try {
            if (jogadorDao.readAll().isEmpty()) {
                view.exibirErro("Não há jogadores cadastrados para exibir.");
                return;
            }

            this.visualizarController.carregarDados();
            cardLayout.show(painelPrincipal, "visualizar");

        } catch (DataBaseException | RecordNotReady ex) {
            view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
        }
    }

    /**
     * Navega para a tela de histórico.
     * Verifica se há jogadores antes de navegar.
     */
    private void navegarParaHistorico() {
        try {
            if (jogadorDao.readAll().isEmpty()) {
                view.exibirErro("Não há jogadores cadastrados para exibir o histórico.");
                return;
            }

            this.historicoController.carregarDadosCombobox();
            this.historicoController.carregarDados();
            cardLayout.show(painelPrincipal, "historico");

        } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
            view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
        }
    }

    /**
     * Navega para a tela de seleção de jogadores (Iniciar Jogo).
     * Verifica se há pelo menos 2 jogadores.
     */
    private void navegarParaIniciar() {
        try {
            if (jogadorDao.readAll().size() < 2) {
                view.exibirErro("É preciso ter pelo menos 2 jogadores cadastrados para iniciar um jogo.");
                return;
            }

            this.selecaoController.carregarDados();
            cardLayout.show(painelPrincipal, "selecao");

        } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
            view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
        }
    }
}