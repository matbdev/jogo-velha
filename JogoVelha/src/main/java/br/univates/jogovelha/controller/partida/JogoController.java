package br.univates.jogovelha.controller.partida;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.model.Partida;
import br.univates.jogovelha.repository.DAOFactory;
import br.univates.jogovelha.repository.interfaces.IDaoPartida;
import br.univates.jogovelha.view.partida.JTabuleiro;
import br.univates.jogovelha.view.partida.PainelTabuleiro;
import java.awt.CardLayout;
import java.util.Calendar;
import javax.swing.JPanel;

/**
 * Controller geral da tela de jogo.
 * Gerencia o functionamento do jogo (placar, jogadores e salvamento do banco)
 * A lógica do tabuleiro está em outro controller
 */
public class JogoController {
    // Necessário
    private final PainelTabuleiro view;
    private final JPanel painelPrincipal;
    private final Jogador jogadorX;
    private final Jogador jogadorO;
    private final Calendar c = Calendar.getInstance();
    
    // Controller
    private final TabuleiroController tabuleiroController; 
    
    // DAOs
    private final IDaoPartida partidaDao;
    private final IDao<Jogador, String> jogadorDao;
    
    // Pontuação em memória
    private int pontuacaoX = 0;
    private int pontuacaoO = 0;

    // Controller
    public JogoController(
            PainelTabuleiro view, 
            IDaoPartida partidaDao,
            IDao<Jogador, String> jogadorDao,
            Jogador jogadorX, 
            Jogador jogadorO, 
            JPanel painelPrincipal
    ) {
        this.view = view;
        this.painelPrincipal = painelPrincipal;
        this.jogadorX = jogadorX;
        this.jogadorO = jogadorO;
        this.partidaDao = partidaDao;
        this.jogadorDao = jogadorDao;

        // Tabuleiro presente na view
        JTabuleiro gridTabuleiro = view.getjTabuleiro1();

        this.tabuleiroController = new TabuleiroController(
                gridTabuleiro,
                jogadorX,
                jogadorO,
                painelPrincipal,
                this
        );
        
        this.view.getBotaoVoltar().addActionListener(e -> voltar());
        this.view.getBotaoRevanche().addActionListener(e -> revanche());

        atualizarPlacar();
    }
    
    /**
     * Atualiza o placar, colocando o nome dos jogadores, o player que está jogando ('X' ou 'O')
     * e o placar, com base nos atributos privados
     */
    private void atualizarPlacar() {
        this.view.getJogador1().setText(jogadorX.getNome() + " (X)");
        this.view.getJogador2().setText(jogadorO.getNome() + " (O)");
        this.view.getPontuacaoJ1().setText(String.valueOf(pontuacaoX));
        this.view.getPontuacaoJ2().setText(String.valueOf(pontuacaoO));
    }
    
    /**
     * Realiza o salvamento no banco de dados
     * Atualiza os objetos dos jogadores e cria um registro de partida, tudo
     * gerenciado por uma única transação
     * @param vencedor 'X', 'O', ou 'E' (Empate)
     */
    private void atualizarBanco(char vencedor) {
        DataBaseConnectionManager db = null;
        
        try{
            db = DAOFactory.getDataBaseConnectionManager();
            Partida p = new Partida(jogadorX, jogadorO, c.getTime(), vencedor);
            
            switch (vencedor) {
                case 'X' -> {
                    jogadorX.adicionarVitoria();
                    jogadorO.adicionarDerrota();
                }
                case 'O' -> {
                    jogadorX.adicionarDerrota();
                    jogadorO.adicionarVitoria();
                }
                case 'E' -> {
                    jogadorX.adicionarEmpate();
                    jogadorO.adicionarEmpate();
                }
            }
            
            db.runSQL("BEGIN TRANSACTION");
            partidaDao.create(p, db);
            jogadorDao.update(jogadorX, db);
            jogadorDao.update(jogadorO, db);
            db.runSQL("COMMIT");
            
        } catch (DataBaseException | RecordNotFoundException e) {
            this.view.exibirErro(e.getMessage());
            
            if (db != null) {
                try{
                    db.runSQL("ROLLBACK");
                    db.closeConnection();
                }catch (DataBaseException ex) {
                    this.view.exibirErro("Erro ao encerrar conexão com o banco");
                }
            }
        }
    }

    /**
     * Ação do botão de voltar
     */
    private void voltar() {
        ((CardLayout) painelPrincipal.getLayout()).show(painelPrincipal, "selecao");
        painelPrincipal.remove(this.view);
    }
    
    /**
     * Ação do botão de revanche
     */
    private void revanche() {
        this.tabuleiroController.resetarParaRevanche();
        this.view.getBotaoRevanche().setEnabled(false);
        this.view.getBotaoVoltar().setEnabled(false);
    }
    
    /**
     * Este método é chamado pelo TabuleiroController quando o jogo termina.
     * @param vencedor 'X', 'O', ou 'E' (Empate)
     */
    public void notificarFimDeJogo(char vencedor) {
        if (vencedor == 'X') {
            pontuacaoX++;
        } else if (vencedor == 'O') {
            pontuacaoO++;
        }
        
        atualizarPlacar();
        atualizarBanco(vencedor);
        this.view.getBotaoRevanche().setEnabled(true);
        this.view.getBotaoVoltar().setEnabled(true);
    }
}