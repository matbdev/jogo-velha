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
    private PainelTabuleiro painelTabuleiroAtual;

    // Controllers (combobox)
    private final JogadorCbController controllerCb1;
    private final JogadorCbController controllerCb2;

    // Dao
    private final IDao<Jogador, String> jogadorDao;
    private final IDaoPartida partidaDao;
    
    // Construtor
    public SelecaoJogadoresController(
        SelecaoJogadorPanel view, 
        IDaoPartida partidaDao, 
        IDao<Jogador, String> jogadorDao, 
        JPanel painelPrincipal
    ) {
        this.view = view;
        this.jogadorDao = jogadorDao;
        this.partidaDao = partidaDao;
        this.painelPrincipal = painelPrincipal;

        // Controllers
        this.controllerCb1 = new JogadorCbController(
                this.view.getJogadorComboBox1(),
                jogadorDao
        );
        this.controllerCb2 = new JogadorCbController(
                this.view.getJogadorComboBox2(),
                jogadorDao
        );
        
        // Ações dos combobox e botões
        view.getJogadorComboBox1().addActionListener(e -> definirSegundoJogador());
        view.getJogadorComboBox2().addActionListener(e -> permitirBotao());
        view.adicionarAcaoBotao(e -> iniciarPartida());
        view.adicionarAcaoBotaoVoltar(e -> voltar());
    }
    
    /**
     * M~etodo que carrega os dados das duas combobox
     */
    public void carregarDados() throws DataBaseException, RecordNotReady {
        this.controllerCb1.carregarDados();
        this.controllerCb2.carregarDados();
    }
    
    /**
     * Método que é chamado assim que a primeira combobox é clicada
     * Ela filtra os jogadores, excluindo aquele que foi selecionado como primeiro jogador
     */
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
    
    /**
     * Método que é chamado assim que a segunda combobox é clicada
     * Ela permite ou não o clique no botão de início
     */
    private void permitirBotao() {
        Jogador jogador = this.controllerCb2.getJogadorSelecionado();

        if (jogador != null) {
            this.view.getBotao().setEnabled(true);
        } else {
            this.view.getBotao().setEnabled(false);
        }
    }
    
    /**
     * Ação do botão de iniciar
     * Gerencia o painel de tabuleiro
     */
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
     * @return lista de jogadores
     */
    private List<Jogador> sortearIcone() {
        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(this.controllerCb1.getJogadorSelecionado());
        jogadores.add(this.controllerCb2.getJogadorSelecionado());
        
        Collections.shuffle(jogadores);
        return jogadores;
    }

    /**
     * Ação do botão "Voltar"
     */
    private void voltar() {
        ((CardLayout) painelPrincipal.getLayout()).show(painelPrincipal, "inicial");
    }
}
