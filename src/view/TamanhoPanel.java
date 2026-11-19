package view;

import dao.TamanhoDAO;
import model.Tamanho;
import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class TamanhoPanel extends CrudPanel<Tamanho> {
    private final TamanhoDAO dao = new TamanhoDAO();
    private final JSpinner spPedacos = new JSpinner(new SpinnerNumberModel(8, 1, 40, 1));

    public TamanhoPanel() {
        addCampo("Quantidade de pedaços:", spPedacos);
        montar();
    }

    @Override protected String[] colunas() { return new String[]{"Código", "Pedaços"}; }
    @Override protected List<Tamanho> listar() throws SQLException { return dao.listar(); }
    @Override protected Object[] linha(Tamanho t) { return new Object[]{t.getId(), t.getQtdPedacos()}; }
    @Override protected void preencherForm(Tamanho t) { spPedacos.setValue(t.getQtdPedacos()); }
    @Override protected void limparForm() { spPedacos.setValue(8); }

    @Override
    protected Tamanho lerForm() {
        return new Tamanho(selecionado == null ? 0 : selecionado.getId(), (Integer) spPedacos.getValue());
    }

    @Override
    protected void gravar(Tamanho t) throws SQLException {
        if (selecionado == null) dao.inserir(t); else dao.atualizar(t);
    }

    @Override protected void remover(Tamanho t) throws SQLException { dao.excluir(t.getId()); }
}
