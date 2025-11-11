package br.univates.jogovelha.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.interfaces.IFilter;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.jogovelha.model.Jogador;

/**
 * Classe responsável por fazer a persistência dos jogadores
 */
public class JogadorDao implements IDao<Jogador, String> {
    /**
     * {@inheritedDoc}
     */
    @Override
    public void create(Jogador jogador) throws DuplicatedKeyException, DataBaseException {
        DataBaseConnectionManager db = null;
        try {
            db = DAOFactory.getDataBaseConnectionManager();
            
            if (db == null) {
                throw new DataBaseException("A conexão com o banco de dados não pode ser nula.");
            }
            try {
                db.runPreparedSQL(
                    "INSERT INTO jogador VALUES (?,?,?,?,?,?);",
                    jogador.getCPF().getCpf(), 
                    jogador.getNome(), 
                    jogador.getEndereco(), 
                    jogador.getQtdeVitorias(),
                    jogador.getQtdeDerrotas(), 
                    jogador.getQtdeEmpates()
                );

            } catch (DataBaseException e) {
                throw new DuplicatedKeyException();
            }
        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }  
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public ArrayList<Jogador> readAll() throws RecordNotReady, DataBaseException {
        ArrayList<Jogador> jogadorList = new ArrayList<>();
        DataBaseConnectionManager db = null;

        try {
            db = DAOFactory.getDataBaseConnectionManager();

            try (ResultSet rs = db.runQuerySQL("SELECT * FROM jogador;")) {

                if (rs.isBeforeFirst()) {
                    rs.next();
                    while (!rs.isAfterLast()) {
                        String cpf = rs.getString("cpf_jogador");
                        String nome = rs.getString("nome");
                        String endereco = rs.getString("endereco");
                        int qtdeVitorias = rs.getInt("qtdeVitoria");
                        int qtdeEmpates = rs.getInt("qtdeEmpates");
                        int qtdeDerrotas = rs.getInt("qtdeDerrotas");

                        jogadorList.add(new Jogador(cpf, nome, endereco, qtdeVitorias, qtdeEmpates, qtdeDerrotas));
                        rs.next();
                    }
                }

                Collections.sort(jogadorList);
            } catch (SQLException e) {
                throw new RecordNotReady();
            }

        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }

        return jogadorList;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void delete(Jogador jogador) throws RecordNotFoundException, DataBaseException {
        DataBaseConnectionManager db = null;
        try {
            db = DAOFactory.getDataBaseConnectionManager();

            db.runPreparedSQL("DELETE FROM correntista WHERE cpf_jogador = ?",
                    jogador.getCPF().getCpf()
            );

        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Jogador read(String cpf) throws RecordNotFoundException, DataBaseException {
        Jogador jogadorEncontrado = null;
        DataBaseConnectionManager db = null;

        db = DAOFactory.getDataBaseConnectionManager();

        try (
            ResultSet rs = db.runPreparedQuerySQL(
                "SELECT * FROM jogador where cpf_jogador = ?;",
                cpf
            )
        ) { 
            if (rs.isBeforeFirst()) {
                rs.next();
                String nome = rs.getString("nome");
                String endereco = rs.getString("endereco");
                int qtdeVitorias = rs.getInt("qtdeVitoria");
                int qtdeEmpates = rs.getInt("qtdeEmpates");
                int qtdeDerrotas = rs.getInt("qtdeDerrotas");

                jogadorEncontrado = new Jogador(cpf, nome, endereco, qtdeVitorias, qtdeEmpates, qtdeDerrotas);
            }

        } catch (SQLException | CpfInvalidoException e) {
            throw new RecordNotFoundException();
        }

        // Fecha a conexão
        if (db != null) {
            db.closeConnection();
        }
        
        if (jogadorEncontrado == null) {
            throw new RecordNotFoundException();
        }

        return jogadorEncontrado;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public ArrayList<Jogador> readAll(IFilter<Jogador> filtro) throws RecordNotReady, DataBaseException {
        ArrayList<Jogador> listaCompleta = this.readAll();
        ArrayList<Jogador> listaFiltrada = new ArrayList<>();

        for (Jogador jogador : listaCompleta) {
            if (filtro.isAccept(jogador)) {
                listaFiltrada.add(jogador);
            }
        }

        return listaFiltrada;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void update(Jogador jogador) throws RecordNotFoundException, DataBaseException {
       DataBaseConnectionManager db = null;

        try {
            db = DAOFactory.getDataBaseConnectionManager();
            this.update(jogador, db);
        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * Este método não gerencia a sua própria conexão
     */
    @Override
    public void update(Jogador jogador, DataBaseConnectionManager db) throws RecordNotFoundException, DataBaseException {
        if (db == null) {
            throw new DataBaseException("A conexão com o banco de dados não pode ser nula.");
        }
            
        db.runPreparedSQL(
            "UPDATE correntista SET nome = ?, endereco = ?, qtdeVitorias = ?, qtdeEmpates = ?, qtdeDerrotas = ? WHERE cpf_correntista = ?",
            jogador.getNome(), 
            jogador.getEndereco(),
            jogador.getQtdeVitorias(),
            jogador.getQtdeEmpates(),
            jogador.getQtdeDerrotas(),
            jogador.getCPF().getCpf()
        );
    }
}