package br.univates.jogovelha.controller.partida;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.jogovelha.controller.JogadorCbController;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.repository.interfaces.IDaoPartida;
import br.univates.jogovelha.view.partida.PainelTabuleiro;
import br.univates.jogovelha.view.partida.SelecaoJogadorPanel;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

/**
 * Controller que gerencia a escolha dos jogadores
 * @author mateus.brambilla
 */
public class SelecaoJogadoresController {
    private final SelecaoJogadorPanel view;
    private final JPanel painelPrincipal;
    private final JogadorCbController controllerCb1;
    private final JogadorCbController controllerCb2;
    private final IDao<Jogador, String> jogadorDao;
    private final IDaoPartida partidaDao;
    private PainelTabuleiro painelTabuleiroAtual;
    
    public SelecaoJogadoresController(SelecaoJogadorPanel view, IDaoPartida partidaDao, IDao<Jogador, String> jogadorDao, JPanel painelPrincipal) {
        this.view = view;
        this.jogadorDao = jogadorDao;
        this.partidaDao = partidaDao;
        this.painelPrincipal = painelPrincipal;
        this.controllerCb1 = new JogadorCbController(
                this.view.getJogadorComboBox1(),
                jogadorDao
        );
        this.controllerCb2 = new JogadorCbController(
                this.view.getJogadorComboBox2(),
                jogadorDao
        );
        
        view.getJogadorComboBox1().addActionListener(e -> definirSegundoJogador());
        view.getJogadorComboBox2().addActionListener(e -> permitirBotao());
        view.adicionarAcaoBotao(e -> iniciarPartida());
    }
    
    public void carregarDados() throws DataBaseException, RecordNotReady {
        this.controllerCb1.carregarDados();
        this.controllerCb2.carregarDados();
    }
    
    private void definirSegundoJogador() {
        Jogador jogador = this.controllerCb1.getJogadorSelecionado();
        
        try {
            ArrayList<Jogador> jogadoresList = this.jogadorDao.readAll();
            
            if (jogador != null) {
                this.view.getJogadorComboBox2().setEditable(true);
                jogadoresList.remove(jogador);
                this.controllerCb2.setDados(jogadoresList);
            } else {
                this.view.getJogadorComboBox2().setEditable(false);
                this.controllerCb2.setJogadorSelecionado(null);
            }
            
        } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
            view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
        }
    }
    
    private void permitirBotao() {
        Jogador jogador = this.controllerCb2.getJogadorSelecionado();

        if (jogador != null) {
            this.view.getBotao().setEnabled(true);
        } else {
            this.view.getBotao().setEnabled(false);
        }
    }
    
    private void iniciarPartida() {
        List<Jogador> jogadores = sortearIcone();
        
        if (this.painelTabuleiroAtual != null) {
            this.painelPrincipal.remove(this.painelTabuleiroAtual);
        }
        
        this.painelTabuleiroAtual = new PainelTabuleiro();
        
        new JogoController(
            this.painelTabuleiroAtual,
            partidaDao,
            jogadorDao,
            jogadores.get(0), 
            jogadores.get(1),
            this.painelPrincipal
        );
            
        painelPrincipal.add(this.painelTabuleiroAtual, "tabuleiro");
        ((CardLayout) painelPrincipal.getLayout()).show(painelPrincipal, "tabuleiro");
    }
    
    /**
     * Retorna, em ordem, o jogador que jogará com 'X' e aquele que jogará com 'O'
     */
    private List<Jogador> sortearIcone() {
        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(this.controllerCb1.getJogadorSelecionado());
        jogadores.add(this.controllerCb2.getJogadorSelecionado());
        
        Collections.shuffle(jogadores);
        return jogadores;
    }
}
