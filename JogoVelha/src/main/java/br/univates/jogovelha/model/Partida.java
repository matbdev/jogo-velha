package br.univates.jogovelha.model;

import java.util.Date;

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
