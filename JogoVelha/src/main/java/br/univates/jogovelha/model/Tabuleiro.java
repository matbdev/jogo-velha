package br.univates.jogovelha.model;

/**
 * Classe que representa o tabuleiro
 */
public class Tabuleiro {
    private final int lado;
    private final char[][] matrizTab;

    public Tabuleiro (int lado) {
        if(lado <= 0)
            throw new IllegalArgumentException("Digite um número positivo");
        this.lado = lado;
        this.matrizTab = new char[lado][lado];
        limpaTabuleiro();
    }

    // Getters
    public int getLado(){
        return this.lado;
    }

    public char[][] getTabuleiro(){
        return this.matrizTab;
    }

    /* Verifica se uma posição no tabuleiro já está ocupada.
     * Se estiver, lança uma exceção.
     * @param i1 - índice da linha
     * @param i2 - índice da coluna
     * @throws IllegalArgumentException se a posição não estiver vazia (' ')
     */
    public void verificarPosicao(int i1, int i2) throws IllegalArgumentException {
        // Se a posição não estiver vazia, lança um erro.
        if (this.matrizTab[i1][i2] != ' ') {
            throw new IllegalArgumentException("Esta posição já foi escolhida. Tente outra.");
        }
    }

    /**
     * Método que preenche o tabuleiro com espaços em branco.
     */
    public void limpaTabuleiro(){
        for (int i = 0; i < this.matrizTab.length; i++) {
            for (int j = 0; j < this.matrizTab[i].length; j++){
                matrizTab[i][j] = ' ';
            }
        }
    }

    /**
     * Preenche a posição com o jogador especificado
     * @param pos - posição do tabuleiro (1-9)
     * @param jogador - ícone ('X' ou 'O') do jogador
     */
    public void preencherPosicao(int pos, char jogador){
        int i1 = 0, i2 = 0;

        switch(pos){
            case 9 -> {i1 = 0; i2 = 2;}
            case 8 -> {i1 = 0; i2 = 1;}
            case 7 -> {i1 = 0; i2 = 0;}
            case 6 -> {i1 = 1; i2 = 2;}
            case 5 -> {i1 = 1; i2 = 1;}
            case 4 -> {i1 = 1; i2 = 0;}
            case 3 -> {i1 = 2; i2 = 2;}
            case 2 -> {i1 = 2; i2 = 1;}
            case 1 -> {i1 = 2; i2 = 0;}
            default -> throw new IllegalArgumentException("Posição inválida.");
        }

        verificarPosicao(i1, i2);
        this.matrizTab[i1][i2] = jogador;
    }
}
