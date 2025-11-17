package br.univates.jogovelha.model;

/**
 * Classe que gerencia o estado e as regras do Jogo da Velha.
 */
public class JogoVelha {
    // Constantes
    public static final char JOGADOR_X = 'X';
    public static final char JOGADOR_O = 'O';
    public static final char VAZIO = ' ';
    public static final char RESULTADO_EMPATE = 'E';
    public static final char RESULTADO_EM_JOGO = ' ';

    private char[][] tabuleiro;
    private char turnoAtual;
    private boolean jogoAtivo;

    // Construtor
    public JogoVelha() {
        this.tabuleiro = new char[3][3];
        resetarJogo();
    }

    /**
     * Reseta o tabuleiro para um novo jogo.
     */
    public void resetarJogo() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = VAZIO;
            }
        }
        this.turnoAtual = JOGADOR_X;
        this.jogoAtivo = true;
    }

    /**
     * Tenta registrar uma jogada no tabuleiro
     * @param linha - linha do botão (0-2)
     * @param col - coluna do botão (0-2)
     * @return true se a jogada foi válida e registrada, false caso contrário.
     */
    public boolean fazerJogada(int linha, int col) {
        boolean ok = false;
        
        if (jogoAtivo && tabuleiro[linha][col] == VAZIO) {
            tabuleiro[linha][col] = turnoAtual;
            
            char vencedor = verificarVencedor();
            if (vencedor != RESULTADO_EM_JOGO) {
                this.jogoAtivo = false;
            } else {
                trocarTurno();
            }
            ok = true;
        }
        
        return ok;
    }

    /**
     * Troca o jogador do turno atual.
     */
    private void trocarTurno() {
        this.turnoAtual = (turnoAtual == JOGADOR_X) ? JOGADOR_O : JOGADOR_X;
    }

    /**
     * Verifica o estado atual do jogo.
     * @return resultado da partida:
     *      - 'X' (Vitória do X)
     *      - 'O' (Vitória do O)
     *      - 'E' (Empate)
     *      - ' ' (Jogo em andamento).
     */
    public char verificarVencedor() {
        // Linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] != VAZIO && tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
                return tabuleiro[i][0];
            }
        }

        // Colunas
        for (int j = 0; j < 3; j++) {
            if (tabuleiro[0][j] != VAZIO && tabuleiro[0][j] == tabuleiro[1][j] && tabuleiro[1][j] == tabuleiro[2][j]) {
                return tabuleiro[0][j];
            }
        }

        // Diagonal principal
        if (tabuleiro[0][0] != VAZIO && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
            return tabuleiro[0][0];
        }

        // Diagonal secundária
        if (tabuleiro[0][2] != VAZIO && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
            return tabuleiro[0][2];
        }

        // Velha
        if (isTabuleiroCheio()) {
            return RESULTADO_EMPATE;
        }

        // Jogo em andamento
        return RESULTADO_EM_JOGO;
    }

    /**
     * Verifica se todas as células do tabuleiro estão preenchidas.
     */
    private boolean isTabuleiroCheio() {
        boolean ok = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == VAZIO) {
                    ok = false;
                }
            }
        }
        return ok;
    }

    /**
     * Retorna o símbolo do jogador do turno atual ('X' ou 'O').
     */
    public char getTurnoAtual() {
        return turnoAtual;
    }

    /**
     * Retorna se o jogo ainda está em andamento.
     */
    public boolean isJogoAtivo() {
        return jogoAtivo;
    }
    
    /**
     * Retorna o símbolo em uma célula específica.
     */
    public char getSimbolo(int linha, int col) {
        return tabuleiro[linha][col];
    }
}