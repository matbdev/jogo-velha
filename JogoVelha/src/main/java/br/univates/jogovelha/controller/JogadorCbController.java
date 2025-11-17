package br.univates.jogovelha.controller;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.view.components.JogadorComboBox;
import java.util.ArrayList;

/**
 * Controller para o combobox de jogadores
 * @author mateus.brambilla
 */
public class JogadorCbController {
    
    private final JogadorComboBox combobox;
    private final IDao<Jogador, String> jogadorDao;    

    // Construtor
    public JogadorCbController(JogadorComboBox combobox, IDao<Jogador, String> jogadorDao) {
        this.combobox = combobox;
        this.jogadorDao = jogadorDao;
    }
    
    /**
     * Define lista de jogadores (para deletar)
     * @param pessoas - lista de jogadores filtrados
     */
    public void setDados(ArrayList<Jogador> pessoas) {
        combobox.setDados(pessoas);
    }
    
    /**
     * Método para carregar os dados no ComboBox.
     * @throws DataBaseException - erro de banco de dados
     * @throws RecordNotReady - registro com algum erro
     */
    public void carregarDados() throws DataBaseException, RecordNotReady {
        ArrayList<Jogador> pessoas = jogadorDao.readAll();
        combobox.setDados(pessoas);
    }
    
    /**
     * Retorna o jogador selecionado no ComboBox.
     * @return - jogador selecionado
     */
    public Jogador getJogadorSelecionado() {
        return combobox.getSelecionado();
    }
    
    /**
     * Define qual jogador deve ser selecionado no ComboBox.
     * @param jogador - jogador a ser selecionado
     */
    public void setJogadorSelecionado(Jogador jogador) {
        combobox.setSelecionado(jogador);
    }
}
