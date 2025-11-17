package br.univates.jogovelha.view.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * Classe que representa cada item do tabuleiro
 */
public class TabuleiroItemPanel extends JButton {
    private int linha;
    private int coluna;
    private char simbolo;
    
    public TabuleiroItemPanel(){
        setBackground(Color.WHITE);
        setBorder(null);
        setBackground(null);
        setFocusPainted(false);
        this.setEnabled(true);
    }
    
    public void setSimbolo(char simbolo) {
        this.simbolo = simbolo;
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

    // Getter
    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
