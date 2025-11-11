package br.univates.alexandria.controllers;

import br.univates.alexandria.components.combobox.PessoaComboBox;
import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.Pessoa;
import java.util.ArrayList;

/**
 * Controller responsável pelo combobox de pessoa
 */
public class PessoaComboBoxController {

    private final PessoaComboBox comboBoxView;
    private final IDao<Pessoa, String> dao;

    // Constructor
    public PessoaComboBoxController(PessoaComboBox comboBoxView, IDao<Pessoa, String> dao) {
        this.comboBoxView = comboBoxView;
        this.dao = dao;
    }

    /**
     * Método para carregar os dados no ComboBox.
     * @throws DataBaseException - erro de banco de dados
     * @throws RecordNotReady - registro com algum erro
     */
    public void carregarDados() throws DataBaseException, RecordNotReady {
        ArrayList<Pessoa> pessoas = dao.readAll();
        comboBoxView.setDados(pessoas);
    }

    /**
     * Define lista de contas bancárias (com base na seleção de uma pessoa)
     * @param pessoas - lista de pessoas filtradas
     */
    public void setDados(ArrayList<Pessoa> pessoas) {
        comboBoxView.setDados(pessoas);
    }

    /**
     * Retorna a pessoa selecionada no ComboBox.
     * @return - pessoa selecionada
     */
    public Pessoa getPessoaSelecionada() {
        return comboBoxView.getSelecionado();
    }

    /**
     * Define qual pessoa deve ser selecionada no ComboBox.
     * @param p - pessoa a ser selecionada
     */
    public void setPessoaSelecionada(Pessoa p) {
        comboBoxView.setSelecionado(p);
    }
}