package br.univates.jogovelha.repository;


import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.jogovelha.model.Jogador;

/**
 * Fábrica para criar instâncias de DAOs.
 * que será compartilhada pelos DAOs.
 */
public class DAOFactory {

    /**
     * Cria e retorna uma instância de JogadorDAO.
     * @return - instância de JogadorDAO
     */
    public static IDao<Jogador, String> getJogadorDao() {
        return new JogadorDao();
    }

    /**
     * Cria e retorna uma instância de DataBaseConnectionManager
     * @return - instância de DataBaseConnectionManager
     * @throws DataBaseException - erro de conexão
     */
    public static DataBaseConnectionManager getDataBaseConnectionManager() throws DataBaseException {
        return getSqliteConnection();
    }

    /**
     * Cria e retorna uma instância do gerenciador de banco de dados do sqlite
     * @return dbcm (DataBaseConnectionManager.SQLITE)
     * @throws DataBaseException - em caso de erro de conexão
     */
    private static DataBaseConnectionManager getSqliteConnection() throws DataBaseException {
        DataBaseConnectionManager dbcm = new DataBaseConnectionManager(
                DataBaseConnectionManager.SQLITE,
                "sistemabancario.db",
                "",
                "");
                
        dbcm.connectionTest();

        dbcm.runSQL(
            """
            CREATE TABLE IF NOT EXISTS jogador (
                cpf_jogador TEXT PRIMARY KEY,
                nome TEXT NOT NULL,
                endereco TEXT,
                qtdeVitorias INTEGER NOT NULL,
                qtdeEmpates INTEGER NOT NULL,
                qtdeDerrotas INTEGER NOT NULL
            );
            """
        );

        dbcm.runSQL(
            """
            CREATE TABLE IF NOT EXISTS partida (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cpf_jogador_x TEXT PRIMARY KEY,
                cpf_jogador_o TEXT PRIMARY KEY,
                resultado TEXT CHECK (resultado IN ('X', 'O', 'E')),
                data_jogo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                FOREIGN KEY (jogador_x_id) REFERENCES jogadores(id),
                FOREIGN KEY (jogador_o_id) REFERENCES jogadores(id)
            );
            """
        );

        return dbcm;
    }
}
