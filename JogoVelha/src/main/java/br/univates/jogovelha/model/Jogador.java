package br.univates.jogovelha.model;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;

/**
 * Classe que representa um jogador, com elementos básicos de uma pessoa 
 * e status dentro do jogo
 */
public class Jogador extends Pessoa {
    //Atributos
    private int qtdeVitorias;
    private int qtdeEmpates;
    private int qtdeDerrotas;

    // Construtor overload
    public Jogador(CPF cpf, String nome, String endereco) {
        super(cpf, nome, endereco);
    }

    public Jogador(String cpf, String nome, String endereco) throws CpfInvalidoException {
        super(cpf, nome, endereco);
    }

    public Jogador(CPF cpf, String nome, String endereco, int qtdeVitorias, int qtdeEmpates, int qtdeDerrotas) {
        super(cpf, nome, endereco);
        this.qtdeDerrotas = qtdeDerrotas;
        this.qtdeEmpates = qtdeEmpates;
        this.qtdeVitorias = qtdeVitorias;
    }

    public Jogador(String cpf, String nome, String endereco, int qtdeVitorias, int qtdeEmpates, int qtdeDerrotas) throws CpfInvalidoException {
        super(cpf, nome, endereco);
        this.qtdeDerrotas = qtdeDerrotas;
        this.qtdeEmpates = qtdeEmpates;
        this.qtdeVitorias = qtdeVitorias;
    }

    // Getters
    public int getQtdeDerrotas() {
        return qtdeDerrotas;
    }

    public int getQtdeVitorias() {
        return qtdeVitorias;
    }

    public int getQtdeEmpates() {
        return qtdeEmpates;
    }
}
