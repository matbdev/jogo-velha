package br.univates.jogovelha.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.model.Partida;
import br.univates.jogovelha.repository.interfaces.IDaoPartida;

/**
 * Classe responsável por fazer a persistência das partidas
 */
public class PartidaDao implements IDaoPartida {

    /**
     * {@inheritedDoc}
     */
    @Override
    public void create(Partida partida, DataBaseConnectionManager db) throws DataBaseException {
        if (db == null) {
            throw new DataBaseException("A conexão com o banco de dados não pode ser nula.");
        }

        db.runPreparedSQL(
            "INSERT INTO partida (cpf_jogador_x, cpf_jogador_o, resultado, data_jogo) VALUES (?,?,?,?);",
            partida.getJogadorX().getCPF().getCpf(), 
            partida.getJogadorO().getCPF().getCpf(),
            partida.getResultado(),
            partida.getDateTime()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Partida> read(CPF cpfJogador) throws RecordNotFoundException, DataBaseException {
        IDao<Jogador, String> jogadorDao = DAOFactory.getJogadorDao();
        DataBaseConnectionManager db = DAOFactory.getDataBaseConnectionManager();

        ArrayList<Partida> partidasList = new ArrayList<>();

        try (ResultSet rs = db.runPreparedQuerySQL(
            "SELECT * FROM transacao WHERE cpf_jogador_x = ? OR cpf_jogador_o = ?;",
            cpfJogador.getCpf(),
            cpfJogador.getCpf()
        )) 
        {
            if (rs.isBeforeFirst()) {
                rs.next();

                while (!rs.isAfterLast()) {
                    Date d = rs.getTimestamp("data_jogo");
                    String cpf_jogador_x = rs.getString("cpf_jogador_x");
                    String cpf_jogador_o = rs.getString("cpf_jogador_o");
                    char resultado = rs.getString("resultado").charAt(0);

                    Jogador jogadorX = jogadorDao.read(cpf_jogador_x);
                    Jogador jogadorO = jogadorDao.read(cpf_jogador_o);

                    partidasList.add(new Partida(jogadorX, jogadorO, d, resultado));
                    rs.next();
                }
            }

            Collections.sort(partidasList);

        } catch (SQLException e) {
            throw new RecordNotFoundException();
        } finally {
            db.closeConnection();
        }
        
        return partidasList;
    }
}