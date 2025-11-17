package br.univates.jogovelha.controller.jogador;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.jogovelha.controller.historico.HistoricoController;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.view.components.JogadorTableModel;
import br.univates.jogovelha.view.jogador.PainelEditarUsuario;
import br.univates.jogovelha.view.jogador.PainelVisualizarUsuarios;
import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Controller do painel de visualização de usuários cadastrados
 * Várias ações podem ser executadas a partir desta tela
 * @author mateus.brambilla
 */
public class VisualizarUsuariosController {
    private final IDao<Jogador, String> jogadorDao;
    private final PainelVisualizarUsuarios view;
    private final JPanel painelPrincipal;
    private final HistoricoController historicoController;
    private PainelEditarUsuario painelEditarAtual;
    private final CadastroUsuarioController cadastroController;
    
    private JogadorTableModel tableModel;
    private Jogador jogadorSelecionado;
    
    // Construtor
    public VisualizarUsuariosController(
        IDao<Jogador, String> jogadorDao,
        PainelVisualizarUsuarios view, 
        JPanel painelPrincipal, 
        HistoricoController historicoController,
        CadastroUsuarioController cadastroController
    ) {
        this.jogadorDao = jogadorDao;
        this.view = view;
        this.painelPrincipal = painelPrincipal;
        this.historicoController = historicoController;
        this.cadastroController = cadastroController;
        
        adicionarListenerTabela();
        
        this.view.getCadastrarBotao().addActionListener(e -> irParaCadastro());
        this.view.getEditarBotao().addActionListener(e -> irParaEdicao());
        this.view.getExcluirBotao().addActionListener(e -> excluirJogador());
        this.view.getPartidasBotao().addActionListener(e -> irParaPartidas());
    }
    
    /**
     * Busca os dados no DAO e os carrega na JTable da View.
     * @throws DataBaseException - erro de conexão com o banco de dados
     * @throws RecordNotReady - erro de atributo no registro
     */
    public void carregarDados() throws DataBaseException, RecordNotReady{
        this.tableModel = new JogadorTableModel(jogadorDao.readAll());
        view.getTable().setModel(tableModel); 
        this.jogadorSelecionado = null;
    }
    
    /**
     * Adiciona o listener que atualiza o 'jogadorSelecionado' sempre que 
     * uma linha da tabela é clicada.
     * Realiza o permissionamento de clique nos botões
     */
    private void adicionarListenerTabela() {
        this.view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = this.view.getTable().getSelectedRow();
                
                if (selectedRow != -1) {
                    this.jogadorSelecionado = this.tableModel.getJogadorAt(selectedRow);
                    this.view.getEditarBotao().setEnabled(true);
                    
                    int derrotas = this.jogadorSelecionado.getQtdeDerrotas();
                    int empates = this.jogadorSelecionado.getQtdeEmpates();
                    int vitorias = this.jogadorSelecionado.getQtdeVitorias();
                    
                    if(derrotas + empates + vitorias == 0) {
                        this.view.getPartidasBotao().setEnabled(false);
                        this.view.getExcluirBotao().setEnabled(true);
                    } else {
                        this.view.getPartidasBotao().setEnabled(true);
                        this.view.getExcluirBotao().setEnabled(false);
                    }
                    
                } else {
                    this.jogadorSelecionado = null;
                    this.view.getEditarBotao().setEnabled(false);
                    this.view.getExcluirBotao().setEnabled(false);
                    this.view.getPartidasBotao().setEnabled(false);
                }
            }
        });
    }

    /**
     * Ação do botão "Cadastrar".
     * Navega para o painel de cadastro.
     */
    private void irParaCadastro() {
        this.cadastroController.showVoltarBotao();
        ((CardLayout) painelPrincipal.getLayout()).show(painelPrincipal, "cadastro");
    }
    
    /**
     * Ação do botão "Editar".
     * Navega para a tela de edição.
     * Gerencia os paineis
     */
    private void irParaEdicao() {
        if (this.painelEditarAtual != null) {
            this.painelPrincipal.remove(this.painelEditarAtual);
        }
        
        this.painelEditarAtual = new PainelEditarUsuario();
        
        new EditarUsuarioController(
                painelEditarAtual, 
                jogadorDao, 
                painelPrincipal,
                this.jogadorSelecionado,
                this
        );

        painelPrincipal.add(painelEditarAtual, "editar");
        ((CardLayout) painelPrincipal.getLayout()).show(painelPrincipal, "editar");
    }
    
    /**
     * Ação do botão "Partidas".
     * Bavega para a tela de histórico de partidas.
     */
    private void irParaPartidas() {
        try {
            this.historicoController.setSelecionado(this.jogadorSelecionado);
            this.historicoController.carregarDados();
            this.historicoController.showVoltarBotao();
            ((CardLayout) painelPrincipal.getLayout()).show(painelPrincipal, "historico");

        } catch (Exception e) {
            view.exibirErro("Erro ao carregar dados: " + e.getMessage());
        }
    }
    
    /**
     * Ação do botão "Excluir".
     * Verifica se um jogador está selecionado, pede confirmação e o exclui.
     */
    private void excluirJogador() {
        try {
            int resposta = this.view.exibirConfirmacao(
                    "Tem certeza que deseja excluir o jogador " + 
                    this.jogadorSelecionado.getNome() + "?"
            );
                
            if (resposta == javax.swing.JOptionPane.YES_OPTION) {
                jogadorDao.delete(this.jogadorSelecionado);
                this.view.exibirSucesso("Jogador excluído com sucesso!");
                carregarDados();
            }
        } catch (RecordNotFoundException e) {
            this.view.exibirErro("Jogador não encontrado para excluir.");
        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de banco de dados: " + e.getMessage());
        } catch (RecordNotReady e) {
            this.view.exibirErro("Erro ao recarregar dados: " + e.getMessage());
        }
    }
}
