package br.univates.jogovelha;

import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Ponto inicial da aplicação
 * @author mateus.brambilla
 */
public class App {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        // java.awt.EventQueue.invokeLater(() -> {
        //     FramePrincipal frame = new FramePrincipal();
        //     new FramePrincipalController(frame);
            
        //     frame.pack();
            
        //     frame.setLocationRelativeTo(null);
        //     frame.setVisible(true);
        // });
    }
}
