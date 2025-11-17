package br.univates.jogovelha.controller;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.jogovelha.controller.historico.HistoricoController;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.util.Messages;
import br.univates.jogovelha.controller.jogador.CadastroUsuarioController;
import br.univates.jogovelha.controller.jogador.VisualizarUsuariosController;
import br.univates.jogovelha.controller.partida.SelecaoJogadoresController;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.repository.DAOFactory;
import br.univates.jogovelha.repository.interfaces.IDaoPartida;
import br.univates.jogovelha.view.FramePrincipal;
import br.univates.jogovelha.view.historico.HistoricoPanel;
import br.univates.jogovelha.view.PainelInicial;
import br.univates.jogovelha.view.jogador.PainelCadastroUsuario;
import br.univates.jogovelha.view.jogador.PainelVisualizarUsuarios;
import br.univates.jogovelha.view.partida.SelecaoJogadorPanel;
import javax.swing.JPanel;

/**
 * Controller para o frame principal da aplicação
 * @author mateus.brambilla
 */
public class FramePrincipalController {
    private final FramePrincipal view;
    private final java.awt.CardLayout cardLayout;
    
    // DAOs
    private final IDao<Jogador, String> jogadorDao;
    private final IDaoPartida partidaDao;
    
    // Controllers
    private HistoricoController historicoController;
    private CadastroUsuarioController cadastroController;
    private SelecaoJogadoresController selecaoController;
    private VisualizarUsuariosController visualizarUsuariosController;
    
    // Construtor
    public FramePrincipalController(FramePrincipal view) {
        this.view = view;
        this.cardLayout = (java.awt.CardLayout) view.getPainelPrincipal().getLayout();
        
        this.jogadorDao = DAOFactory.getJogadorDao();
        this.partidaDao = DAOFactory.getPartidaDao();
        
        // Paineis
        JPanel painelPrincipal = view.getPainelPrincipal();
        PainelInicial inicialView = new PainelInicial();
        PainelCadastroUsuario cadastroView = new PainelCadastroUsuario();
        PainelVisualizarUsuarios visualizarView = new PainelVisualizarUsuarios();
        HistoricoPanel historicoView = new HistoricoPanel();
        SelecaoJogadorPanel selecaoView = new SelecaoJogadorPanel();
        
        // Adicionando paineis ao card layout
        painelPrincipal.add(inicialView, "inicial");
        painelPrincipal.add(cadastroView, "cadastro");
        painelPrincipal.add(visualizarView, "visualizar");
        painelPrincipal.add(historicoView, "historico");
        painelPrincipal.add(selecaoView, "selecao");
        
        // Painel principal
        inicialView.adicionarAcaoBotaoIniciar(e -> cardLayout.show(view.getPainelPrincipal(), "iniciar"));
        inicialView.adicionarAcaoBotaoJogadores(e -> cardLayout.show(view.getPainelPrincipal(), "visualizar"));
        inicialView.adicionarAcaoBotaoHistorico(e -> cardLayout.show(view.getPainelPrincipal(), "historico"));
        inicialView.adicionarAcaoBotaoSair(e -> {
            Messages.infoMessage(inicialView, "Saindo da aplicação...");
            System.exit(0);
        });  
        
        // Controllers
        this.cadastroController = new CadastroUsuarioController(jogadorDao, cadastroView, painelPrincipal);
        this.cadastroController.hideVoltarBotao();
        this.historicoController = new HistoricoController(historicoView, partidaDao, jogadorDao);
        this.visualizarUsuariosController = new VisualizarUsuariosController(
                jogadorDao, 
                visualizarView, 
                painelPrincipal, 
                this.historicoController, 
                this.cadastroController
        );
        this.selecaoController = new SelecaoJogadoresController(selecaoView, partidaDao, jogadorDao, painelPrincipal);
        new InicioController(
                inicialView, 
                painelPrincipal, 
                jogadorDao, 
                this.visualizarUsuariosController, 
                this.historicoController,
                this.selecaoController
        );
        
        // Navegação
        this.view.getSairMenuItem().addActionListener(e -> {
            System.exit(0);
            Messages.infoMessage(painelPrincipal, "Saindo do programa...");
        });
        view.getPainelInicialMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "inicial"));
        view.getCadastrarUserMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "cadastro"));
        
        // Visualização
        view.getVisualizarUserMenuItem().addActionListener(e -> {
            try {
                this.visualizarUsuariosController.carregarDados();
                cardLayout.show(view.getPainelPrincipal(), "visualizar");
                
            } catch (DataBaseException | RecordNotReady ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });
        
        // Partidas
        view.getPartidasMenuItem().addActionListener(e -> {
            try {
                if (jogadorDao.readAll().isEmpty()) {
                    view.exibirErro("Não há jogadores cadastrados para exibir o histórico.");
                    return;
                }
                
                this.historicoController.carregarDadosCombobox();
                this.historicoController.carregarDados();
                cardLayout.show(view.getPainelPrincipal(), "historico");
                
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });
        
        // Iniciar jogo
        view.getIniciarJogoMenuItem().addActionListener(e -> {
            try {
                if (jogadorDao.readAll().size() < 2) {
                    view.exibirErro("É preciso ter pelo menos 2 jogadores cadastrados para iniciar um jogo.");
                    return;
                }
                
                this.selecaoController.carregarDados();
                cardLayout.show(view.getPainelPrincipal(), "selecao");
                
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });    
        
        cardLayout.show(view.getPainelPrincipal(), "inicial");
    }
}