package view;

import dao.SaborDAO;
import model.Sabor;
import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class SaborPanel extends CrudPanel<Sabor> {
    private final SaborDAO dao = new SaborDAO();
    private final JTextField txtNome = new JTextField(30);
    private final JTextField txtDescricao = new JTextField(40);

    public SaborPanel() {
        addCampo("Nome do sabor:", txtNome);
        addCampo("Descrição (ingredientes):", txtDescricao);
        montar();
    }

    @Override protected String[] colunas() { return new String[]{"Código", "Sabor", "Descrição"}; }
    @Override protected List<Sabor> listar() throws SQLException { return dao.listar(); }
    @Override protected Object[] linha(Sabor s) { return new Object[]{s.getId(), s.getNome(), s.getDescricao()}; }
    @Override protected void preencherForm(Sabor s) { txtNome.setText(s.getNome()); txtDescricao.setText(s.getDescricao()); }
    @Override protected void limparForm() { txtNome.setText(""); txtDescricao.setText(""); txtNome.requestFocus(); }

    @Override
    protected Sabor lerForm() {
        String nome = txtNome.getText().trim();
        String desc = txtDescricao.getText().trim();
        if (nome.isEmpty()) throw new IllegalArgumentException("Informe o nome do sabor.");
        if (nome.length() > 100) throw new IllegalArgumentException("O nome pode ter no máximo 100 caracteres.");
        if (desc.length() > 300) throw new IllegalArgumentException("A descrição pode ter no máximo 300 caracteres.");
        return new Sabor(selecionado == null ? 0 : selecionado.getId(), nome, desc);
    }

    @Override
    protected void gravar(Sabor s) throws SQLException {
        if (selecionado == null) dao.inserir(s); else dao.atualizar(s);
    }

    @Override protected void remover(Sabor s) throws SQLException { dao.excluir(s.getId()); }
}
