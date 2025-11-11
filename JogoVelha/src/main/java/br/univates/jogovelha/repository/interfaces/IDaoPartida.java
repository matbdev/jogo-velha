package br.univates.jogovelha.repository.interfaces;

import java.util.ArrayList;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.jogovelha.model.Partida;

/**
 * Define um DAO específico para partidas
 */
public interface IDaoPartida {
    /**
     * Insere um novo objeto no repositório de dados.
     * Recebe a conexão com o banco de dados
     *
     * @param objeto - partidas a ser persistida
     * @param db - banco de dados
     * @throws DataBaseException - erro de conexão
     */
    public void create(Partida objeto, DataBaseConnectionManager db) throws DataBaseException;

    /**
     * Realiza a leitura de uma partida com base em um dos participantes desta partida
     *
     * @param cpf - chave do jogador
     * @throws RecordNotFoundException - erro de chave não encontrada
     * @throws DataBaseException - erro de conexão
     */
    public ArrayList<Partida> read(CPF cpf) throws RecordNotFoundException, DataBaseException;
}