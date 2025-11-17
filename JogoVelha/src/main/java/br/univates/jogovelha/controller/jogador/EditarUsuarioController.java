package br.univates.jogovelha.controller.jogador;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.interfaces.IDao;
import br.univates.jogovelha.model.Jogador;
import br.univates.jogovelha.view.jogador.PainelEditarUsuario;
import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Controller para o painel de ediçäo de usuário.
 * Ele gerencia a lógica do painel.
 */
public class EditarUsuarioController {
    private final PainelEditarUsuario view;
    private final IDao<Jogador, String> jogadorDao;
    private final Jogador jogador;
    private final JPanel painelPrincipal;
    private final VisualizarUsuariosController visController;

    public EditarUsuarioController(PainelEditarUsuario view, IDao<Jogador, String> jogadorDao, JPanel painelPrincipal, Jogador jogador, VisualizarUsuariosController visController) {
        this.view = view;
        this.jogadorDao = jogadorDao;
        this.painelPrincipal = painelPrincipal;
        this.jogador = jogador;
        this.visController = visController;
        
        this.view.adicionarAcaoBotao(e -> editarUsuario());
        this.view.adicionarAcaoBotaoVoltar(e -> voltar());
        
        this.view.getJogadorText().setText(jogador.getCPF().getCpfFormatado() + " - " + jogador.getNome());
        this.view.getEndereco().setText(jogador.getEndereco());
        this.view.getNome().setText(jogador.getNome());
        this.view.getCpf().setCpf(jogador.getCPF());
    }
    
    /**
     * Lógica de editar usuário
     */
    private void editarUsuario() {
        try {
            String nome = this.view.getNome().getText();
            String endereco = this.view.getEndereco().getText();

            Jogador jogador = new Jogador(this.jogador.getCPF(), nome, endereco);
            this.jogadorDao.update(jogador);
            
            this.view.exibirSucesso("Jogador editado com sucesso!");
            
        } catch (RecordNotFoundException e) {
            this.view.exibirErro("Jogador não encontrado para atualizar.");
        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            this.view.exibirErro(e.getMessage());
        }
    }
    
    /**
     * Ação do botão "Voltar"
     * Remove o painel de cadastro e mostra o painel anterior (visualização).
     */
    private void voltar() {
        try {
            this.visController.carregarDados();
            ((CardLayout) painelPrincipal.getLayout()).show(painelPrincipal, "visualizar");
            this.painelPrincipal.remove(this.view); 

        } catch (Exception e) {
            view.exibirErro("Erro ao voltar: " + e.getMessage());
        }
    }
}     