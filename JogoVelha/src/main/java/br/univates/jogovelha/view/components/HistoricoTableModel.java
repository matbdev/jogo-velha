package br.univates.jogovelha.view.components;

import br.univates.jogovelha.model.Partida;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabela para historico de partidas
 * @author mateus.brambilla
 */
public class HistoricoTableModel extends AbstractTableModel {
    
    private final List<Partida> partidaList;
    private final String[] colunas = {"Data", "Jogador X", "Jogador O", "Resultado"};
    
    public HistoricoTableModel(List<Partida> partidaList) {
        this.partidaList = partidaList;
        Collections.sort(partidaList);
    }
    
    @Override
    public int getRowCount() {
        return this.partidaList.size();
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
        Partida partida = partidaList.get(rowIndex);
        
        switch (columnIndex) {
            case 0 -> {return partida.getDateTime();}
            case 1 -> {return partida.getJogadorX();}
            case 2 -> {return partida.getJogadorO();}
            case 3 -> {return partida.getResultado();}
            default -> {return null;}
        }
    }
}
