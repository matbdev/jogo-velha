package br.univates.jogovelha.view.components;

import javax.swing.JButton;

/**
 * Classe que representa cada item do tabuleiro
 */
public class TabuleiroItemPanel extends JButton {
    public TabuleiroItemPanel(){
        setBorder(null);
        setBackground(null);
        setFocusPainted(false);
    }

    // Setter
    public void setActionListener(java.awt.event.ActionListener listener) {
        addActionListener(listener);
    }

    // Getter
    public char getCurrentChar(){
        return getText().charAt(0);
    }
}
