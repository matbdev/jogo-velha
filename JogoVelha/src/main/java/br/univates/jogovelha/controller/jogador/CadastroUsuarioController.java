package br.univates.jogovelha.controller.jogador;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.view.jogador.PainelCadastroUsuario;
import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Controller responsável por gerenciar o painel de cadastro de usuário
 * @author mateus.brambilla
 */
public class CadastroUsuarioController {
    private final IDao<Jogador, String> jogadorDao;
    private final PainelCadastroUsuario view;
    private final JPanel painelPrincipal;
    
    // Construtor
    public CadastroUsuarioController(IDao<Jogador, String> jogadorDao, PainelCadastroUsuario view, JPanel painelPrincipal){
        this.view = view;
        this.jogadorDao = jogadorDao;
        this.painelPrincipal = painelPrincipal;
        
        this.view.adicionarAcaoBotao(e -> cadastrarUsuario());
        this.view.adicionarAcaoBotaoVoltar(e -> voltar());
    }
    
    /** Método para ocultar botão de voltar */
    public void hideVoltarBotao() {
        this.view.hideVoltarBotao();
    }
    
    /** Método para exibir botão de voltar */
    public void showVoltarBotao() {
        this.view.showVoltarBotao();
    }
     
    /**
     * Lógica de cadastrar usuário
     */
    private void cadastrarUsuario() {
        try {
            String nome = this.view.getNome().getText();
            CPF cpf = this.view.getCpf().getCpf();
            String endereco = this.view.getEndereco().getText();
            
            if (cpf == null){
                throw new CpfInvalidoException("");
            }

            Jogador jogador = new Jogador(cpf, nome, endereco);
            jogadorDao.create(jogador);
            
            this.view.exibirSucesso("Jogador adicionado com sucesso!");

            this.view.getNome().setText("");
            this.view.getCpf().setText("");
            this.view.getEndereco().setText("");
        } catch (CpfInvalidoException e) {
            this.view.exibirErro("O CPF informado é inválido.");
        } catch (DuplicatedKeyException e) {
            this.view.exibirErro("Já existe um jogador com este CPF.");
        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            this.view.exibirErro("Erro inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Ação do botão "Voltar"
     */
    private void voltar() {
        ((CardLayout) painelPrincipal.getLayout()).show(painelPrincipal, "visualizar");
    }
}
