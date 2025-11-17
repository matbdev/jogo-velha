package br.univates.jogovelha.view.components;

import br.univates.alexandria.components.combobox.IComboBox;
import br.univates.jogovelha.model.Jogador;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author mateu
 */
public class JogadorComboBox extends JComboBox<Jogador> implements IComboBox<Jogador>{
    private ArrayList<Jogador> jogadorList;
    private final DefaultComboBoxModel<Jogador> model;
    
    public JogadorComboBox(){
        this.model = new DefaultComboBoxModel<>();
        this.jogadorList = new ArrayList<>();
        setModel(model);
    }
    
    /**
     * Construtor que recebe a lista de itens.
     * @param jogadorList - lista com os objetos de Jogador
     */
    public JogadorComboBox(ArrayList<Jogador> jogadorList) {
        this.model = new DefaultComboBoxModel<>();
        this.jogadorList = (jogadorList != null) ? jogadorList : new ArrayList<>();
        setModel(model);
        carregarItens();
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public void setDados(ArrayList<Jogador> jogadorListAtt) {
        this.jogadorList = (jogadorListAtt != null) ? jogadorListAtt : new ArrayList<>();
        carregarItens();
    }
    
    /**
     * Método para carregar os itens no ComboBox.
     */
    private void carregarItens() {
        model.removeAllElements();
        model.addElement(null);

        for (Jogador jogador : jogadorList) {
            model.addElement(jogador);
        }
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public void setSelecionado(Jogador jogador) {
        if (jogador != null && jogadorList.contains(jogador)) {
            model.setSelectedItem(jogador);
        } else {
            model.setSelectedItem(null);
        }
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public Jogador getSelecionado() {
        return (Jogador) getSelectedItem();
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public void deletar(Jogador jogador) {
        if(!jogadorList.contains(jogador)){
            throw new IllegalArgumentException("Este jogador não existe entre as opções do combobox");
        }
        jogadorList.remove(jogador);
        model.removeElement(jogador);
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public void adicionar(Jogador jogador) {
        if(jogador != null && !jogadorList.contains(jogador)){
            jogadorList.add(jogador);
            model.addElement(jogador);
        }
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public int getTamanho() {
        return jogadorList.size();
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public void adicionarAcao(ActionListener listener){
        addActionListener(listener);
    }
}
