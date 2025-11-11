package br.univates.jogovelha.model;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

/**
 * Classe que representa uma partida do jogo, com dois jogadores, resultado e data
 */
public class Partida implements Comparable<Partida> {
    // Constantes
    public static final char EMPATE = 'E';
    public static final char VITORIA_X = 'X';
    public static final char VITORIA_O = 'O';

    // Atributos da partida
    private char resultado;
    private final Jogador jogadorX;
    private final Jogador jogadorO;
    private final Date d;

    // Constructor overload
    public Partida(Jogador jogadorX, Jogador jogadorO, Date data){
        this.jogadorX = jogadorX;
        this.jogadorO = jogadorO;
        this.d = data;
    }

    public Partida(Jogador jogadorX, Jogador jogadorO, Date data, char resultado){
        this.jogadorX = jogadorX;
        this.jogadorO = jogadorO;
        this.d = data;
        this.resultado = resultado;
    }

    // Getters
    public Date getDateTime() {
        return d;
    }

    public Jogador getJogadorX() {
        return jogadorX;
    }

    public Jogador getJogadorO() {
        return jogadorO;
    }

    public char getResultado() {
        return resultado;
    }

    /**
     * Setter para resultado
     * Só é setado se não houver um resultado (evita manipulação)
     * @param resultado - resultado da partida 
     */
    public void setResultado(char resultado) {
        List<Character> resultados = Arrays.asList(EMPATE, VITORIA_O, VITORIA_X);
        
        if (!resultados.contains(resultado)) {
            throw new IllegalArgumentException("""
                    Informe uma opção válida.
                    As opções disponíveis são:

                    - Jogador.VITORIA_X = 'X';
                    - Jogador.VITORIA_O = 'O';
                    - Jogador.EMPATE = 'E'.
                """);
        }

        if (this.resultado != 0){
            String text = "";

            switch (resultado) {
                case EMPATE -> {text = "empate";}
                case VITORIA_O -> {text = "vitória do jogador O";}
                case VITORIA_X -> {text = "vitória do jogador X";}
            }

            throw new IllegalAccessError("Impossível modificar resultado -> a partida terminou em " + text);
        }

        this.resultado = resultado;
    }

    /**
     * Método sobrescrito da interface comparable
     * Compara duas datas
     * @param p - o objeto da outra partida
     * @return resultado da comparação das datas (ordem decrescente - mais recente primeiro)
     */
    @Override
    public int compareTo(Partida p) {
        return p.getDateTime().compareTo(this.getDateTime());
    }
}
