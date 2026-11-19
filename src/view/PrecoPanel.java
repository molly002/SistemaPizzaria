package view;

import dao.PrecoSaborDAO;
import dao.SaborDAO;
import dao.TamanhoDAO;
import model.PrecoSabor;
import model.Sabor;
import model.Tamanho;
import util.Formato;
import javax.swing.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/** Cadastro de preços (sabor x tamanho). */
public class PrecoPanel extends CrudPanel<PrecoSabor> {
    private final PrecoSaborDAO dao = new PrecoSaborDAO();
    private final JComboBox<Sabor> cbSabor = new JComboBox<>();
    private final JComboBox<Tamanho> cbTamanho = new JComboBox<>();
    private final JTextField txtValor = new JTextField(10);

    public PrecoPanel() {
        addCampo("Sabor:", cbSabor);
        addCampo("Tamanho:", cbTamanho);
        addCampo("Valor (R$):", txtValor);
        montar();
    }

    @Override
    protected void atualizarListas() throws SQLException {
        preencher(cbSabor, new SaborDAO().listar(), false);
        preencher(cbTamanho, new TamanhoDAO().listar(), false);
    }

    @Override protected String[] colunas() { return new String[]{"Código", "Sabor", "Tamanho", "Valor"}; }
    @Override protected List<PrecoSabor> listar() throws SQLException { return dao.listar(); }
    @Override protected Object[] linha(PrecoSabor p) {
        return new Object[]{p.getId(), p.getSabor().getNome(), p.getTamanho(), Formato.moeda(p.getValor())};
    }

    @Override
    protected void preencherForm(PrecoSabor p) {
        selecionar(cbSabor, p.getSabor().getId(), Sabor::getId);
        selecionar(cbTamanho, p.getTamanho().getId(), Tamanho::getId);
        txtValor.setText(p.getValor().toPlainString());
    }

    @Override protected void limparForm() { txtValor.setText(""); }

    @Override
    protected PrecoSabor lerForm() {
        Sabor s = (Sabor) cbSabor.getSelectedItem();
        Tamanho t = (Tamanho) cbTamanho.getSelectedItem();
        if (s == null || t == null) throw new IllegalArgumentException("Cadastre e selecione um sabor e um tamanho.");
        BigDecimal v = Formato.parseValor(txtValor.getText());
        if (v.signum() <= 0 || v.compareTo(new BigDecimal("999.99")) > 0)
            throw new IllegalArgumentException("O valor deve estar entre 0,01 e 999,99.");
        return new PrecoSabor(selecionado == null ? 0 : selecionado.getId(), s, t, v);
    }

    @Override
    protected void gravar(PrecoSabor p) throws SQLException {
        if (selecionado == null) dao.inserir(p); else dao.atualizar(p);
    }

    @Override protected void remover(PrecoSabor p) throws SQLException { dao.excluir(p.getId()); }
}
