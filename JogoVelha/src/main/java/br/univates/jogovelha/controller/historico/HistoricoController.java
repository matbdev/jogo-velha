package br.univates.jogovelha.controller.historico;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.jogovelha.controller.JogadorCbController;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.repository.interfaces.IDaoPartida;
import br.univates.jogovelha.view.historico.HistoricoPanel;
import br.univates.jogovelha.view.components.HistoricoTableModel;
import java.awt.CardLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Controller para o painel que contém o histórico de partidas
 * @author mateus.brambilla
 */
public class HistoricoController {
    private IDaoPartida partidaDao;
    private final HistoricoPanel view;
    private JogadorCbController controllerCb;
    private JPanel painelPrincipal;
    
    public HistoricoController(HistoricoPanel view, IDaoPartida partidaDao, IDao<Jogador, String> jogadorDao){
        this.view = view;
        this.partidaDao = partidaDao;
        this.controllerCb = new JogadorCbController(
                view.getJogadorCb(), 
                jogadorDao
        );
        
        this.view.hideVoltarBotao();
        view.getJogadorCb().adicionarAcao(
            e -> {
                carregarDados();
                Jogador jogador = this.controllerCb.getJogadorSelecionado();

                if (jogador == null) {
                    view.getLabelTitulo().setText("Partidas");
                } else {
                    view.getLabelTitulo().setText("Partidas - " + jogador.getNome());
                }
            }
        );
    }
    
    public void hideVoltarBotao() {
        this.view.hideVoltarBotao();
    }
    
    public void showVoltarBotao() {
        this.view.showVoltarBotao();
    }
    
    public HistoricoController(HistoricoPanel view, IDaoPartida partidaDao, JPanel painelPrincipal, Jogador jogadorVisualizacao){
        this.view = view;
        this.partidaDao = partidaDao;
        this.painelPrincipal = painelPrincipal;

        view.getLabelTitulo().setText("Partidas - " + jogadorVisualizacao.getNome());
        this.controllerCb.setJogadorSelecionado(jogadorVisualizacao);
        this.view.getJogadorCb().setEditable(false);
        this.view.adicionarAcaoBotaoVoltar(e -> voltar());
    }
    
    /**
     * Carrega os dados dos comboboxes personalizados
     */
    public void carregarDadosCombobox() {
        try {
            this.controllerCb.carregarDados();
        } catch (RecordNotReady | DataBaseException ex) {
            this.view.exibirErro("Falha ao carregar correntistas: " + ex.getMessage());
        }
    }
    
    /**
     * Busca os dados no DAO e os carrega no jtable
     */
    public void carregarDados() {
        Jogador jogador = this.controllerCb.getJogadorSelecionado();
        HistoricoTableModel tableModel;
        
        try {
            if (jogador == null) {
                tableModel = new HistoricoTableModel(partidaDao.readAll());
            } else {
                tableModel = new HistoricoTableModel(partidaDao.read(jogador.getCPF()));
            }
        } catch (DataBaseException | RecordNotFoundException ex) {
            view.exibirErro("Erro ao carregar usuários: " + ex.getMessage());
            tableModel = new HistoricoTableModel(new ArrayList<>());
        }
         
        view.getTabela().setModel(tableModel); 
    }
    
    /**
     * Coloca um jogador como o selecionado
     */
    public void setSelecionado(Jogador jogador) {
        this.controllerCb.setJogadorSelecionado(jogador);
    }
    
    /**
     * Ação do botão "Voltar"
     */
    private void voltar() {
        ((CardLayout) this.painelPrincipal.getLayout()).show(this.painelPrincipal, "visualizarUsuarios");
    }
}
