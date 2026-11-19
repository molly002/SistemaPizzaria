package view;

import dao.CrudDAO;
import model.Pessoa;
import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiFunction;

/** CRUD reutilizável para qualquer Pessoa (Cliente, Funcionario) — polimorfismo + generics. */
public class PessoaPanel<P extends Pessoa> extends CrudPanel<P> {
    private final CrudDAO<P> dao;
    private final BiFunction<Integer, String, P> fabrica;
    private final JTextField txtNome = new JTextField(30);

    public PessoaPanel(String rotulo, CrudDAO<P> dao, BiFunction<Integer, String, P> fabrica) {
        this.dao = dao;
        this.fabrica = fabrica;
        addCampo("Nome do " + rotulo + ":", txtNome);
        montar();
    }

    @Override protected String[] colunas() { return new String[]{"Código", "Nome"}; }
    @Override protected List<P> listar() throws SQLException { return dao.listar(); }
    @Override protected Object[] linha(P p) { return new Object[]{p.getId(), p.getNome()}; }
    @Override protected void preencherForm(P p) { txtNome.setText(p.getNome()); }
    @Override protected void limparForm() { txtNome.setText(""); txtNome.requestFocus(); }

    @Override
    protected P lerForm() {
        String nome = txtNome.getText().trim();
        if (nome.length() < 2) throw new IllegalArgumentException("Informe um nome com ao menos 2 caracteres.");
        if (nome.length() > 100) throw new IllegalArgumentException("O nome pode ter no máximo 100 caracteres.");
        return fabrica.apply(selecionado == null ? 0 : selecionado.getId(), nome);
    }

    @Override
    protected void gravar(P p) throws SQLException {
        if (selecionado == null) dao.inserir(p); else dao.atualizar(p);
    }

    @Override protected void remover(P p) throws SQLException { dao.excluir(p.getId()); }
}
