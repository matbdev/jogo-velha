package br.univates.jogovelha.view.components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

/**
 * Classe que representa cada item do tabuleiro
 * Possui uma coordenada que o define
 */
public class TabuleiroItemPanel extends JButton {
    private int linha;
    private int coluna;
    private char simbolo;
    
    // Construtor
    public TabuleiroItemPanel(){
        setBackground(Color.WHITE);
        setBorder(null);
        setBackground(null);
        setFocusPainted(false);
        this.setEnabled(true);
        this.setFont(new Font("SansSerif", Font.PLAIN, 18));
    }
    
    // Define um símbolo na posição (coloca como texto)
    public void setSimbolo(char simbolo) {
        this.simbolo = simbolo;

        switch(simbolo){
            case 'X' -> {this.setForeground(Color.BLUE);}
            case 'O' -> {this.setForeground(Color.RED);}
            case ' ' -> {this.setForeground(Color.WHITE);}
        }

        setText(String.valueOf(this.simbolo));
    }

    // Setters
    public void setLinha(int linha) {
        if (linha < 0) {
            throw new IllegalArgumentException("A linha não pode ser negativa.");
        }
        this.linha = linha;
    }
    
    public void setColuna(int coluna) {
        if (coluna < 0) {
            throw new IllegalArgumentException("A coluna não pode ser negativa.");
        }
        this.coluna = coluna;
    }

    // Getters
    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
