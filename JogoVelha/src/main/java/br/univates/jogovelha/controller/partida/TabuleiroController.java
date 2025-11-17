package br.univates.jogovelha.controller.partida;

import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.model.JogoVelha;
import br.univates.jogovelha.view.partida.JTabuleiro;
import javax.swing.JPanel;

/**
 * Controller destinado para o controle do tabuleiro
 * @author mateus.brambilla
 */
public class TabuleiroController {
    private final JogoVelha jogoVelha;
    private final JTabuleiro view;
    private final Jogador jogadorX;
    private final Jogador jogadorO;
    private final JPanel painelPrincipal;
    private final JogoController jogoController;

    public TabuleiroController(JTabuleiro view, Jogador jogadorX, Jogador jogadorO, JPanel painelPrincipal, JogoController jogoController) {
        this.view = view;
        this.jogadorX = jogadorX;
        this.jogadorO = jogadorO;
        this.painelPrincipal = painelPrincipal;
        this.jogoController = jogoController;
        
        this.jogoVelha = new JogoVelha();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int linha = i;
                final int coluna = j;
                this.view.adicionarAcaoBotao(linha, coluna, e -> cliqueBotao(linha, coluna));
            }
        }
    }
    
    private void processarFimDeJogo(char resultado) {
        // Desabilita a view
        this.view.desabilitarTabuleiro(); 

        // Define a mensagem
        String mensagem;
        if (resultado == jogoVelha.RESULTADO_EMPATE) {
            mensagem = "Deu velha! (Empate)";
        } else {
            Jogador vencedor = (resultado == jogoVelha.JOGADOR_X) ? jogadorX : jogadorO;
            mensagem = "Vitória de: " + vencedor.getNome();
        }

        this.view.exibirSucesso(mensagem);
    
        if (this.jogoController != null) {
            this.jogoController.notificarFimDeJogo(resultado);
        }
    }
    
    /**
     * Reseta a lógica e a view para uma nova partida.
     */
    public void resetarParaRevanche() {
        this.jogoVelha.resetarJogo();
        this.view.resetarTabuleiro(); 
    }
    
    private void cliqueBotao(int linha, int col) {
        char simboloDoTurno = jogoVelha.getTurnoAtual();
        boolean jogadaValida = jogoVelha.fazerJogada(linha, col);

        if (jogadaValida) {
            this.view.desenharSimbolo(linha, col, simboloDoTurno);
            char resultado = jogoVelha.verificarVencedor();

            // Verifica vitória
            if (resultado != jogoVelha.RESULTADO_EM_JOGO) {
                processarFimDeJogo(resultado);
            }
        }
    }
           
}