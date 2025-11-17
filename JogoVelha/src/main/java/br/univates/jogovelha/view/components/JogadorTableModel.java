package br.univates.jogovelha.view.components;

import br.univates.jogovelha.model.Jogador;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabela para historico de partidas
 * @author mateus.brambilla
 */
public class JogadorTableModel extends AbstractTableModel {
    
    private final List<Jogador> jogadorList;
    private final String[] colunas = {"CPF", "Nome", "Endereço", "V", "E", "D", "Jogos"};
    
    public JogadorTableModel(List<Jogador> jogadorList) {
        this.jogadorList = jogadorList;
        Collections.sort(jogadorList);
    }
    
    @Override
    public int getRowCount() {
        return this.jogadorList.size();
    }
    
    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return this.colunas[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Jogador jogador = jogadorList.get(rowIndex);
        
        switch (columnIndex) {
            case 0 -> {return jogador.getCPF().getCpfFormatado();}
            case 1 -> {return jogador.getNome();}
            case 2 -> {return jogador.getEndereco();}
            case 3 -> {return jogador.getQtdeVitorias();}
            case 4 -> {return jogador.getQtdeEmpates();}
            case 5 -> {return jogador.getQtdeDerrotas();}
            case 6 -> {return jogador.getQtdeDerrotas() + jogador.getQtdeEmpates() + jogador.getQtdeVitorias();}
            default -> {return null;}
        }
    }
    
    /**
     * Retorna um objeto de Jogador dado uma linha em específico
     * @param rowIndex - linha da tabela
     * @return - objeto de jogador
     */
    public Jogador getJogadorAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < jogadorList.size()) {
            return this.jogadorList.get(rowIndex);
        }
        return null;
    }
}
