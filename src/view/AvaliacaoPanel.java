package view;

import dao.AvaliacaoDAO;
import dao.PedidoDAO;
import model.Avaliacao;
import model.Pedido;
import util.Formato;
import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AvaliacaoPanel extends CrudPanel<Avaliacao> {
    private final AvaliacaoDAO dao = new AvaliacaoDAO();
    private final JComboBox<Pedido> cbPedido = new JComboBox<>();
    private final JSpinner spNota = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
    private final JTextField txtComentario = new JTextField(40);

    public AvaliacaoPanel() {
        addCampo("Pedido:", cbPedido);
        addCampo("Nota (1 a 5):", spNota);
        addCampo("Comentário:", txtComentario);
        montar();
    }

    @Override
    protected void atualizarListas() throws SQLException {
        preencher(cbPedido, new PedidoDAO().listar(), false);
    }

    @Override protected String[] colunas() { return new String[]{"Nº", "Pedido", "Cliente", "Nota", "Comentário", "Data"}; }
    @Override protected List<Avaliacao> listar() throws SQLException { return dao.listar(); }
    @Override protected Object[] linha(Avaliacao a) {
        return new Object[]{a.getId(), "#" + a.getIdPedido(), a.getNomeCliente(), a.getNota(),
                a.getComentario(), Formato.data(a.getData())};
    }

    @Override
    protected void preencherForm(Avaliacao a) {
        selecionar(cbPedido, a.getIdPedido(), Pedido::getId);
        cbPedido.setEnabled(false); // o pedido avaliado não pode ser trocado
        spNota.setValue(a.getNota());
        txtComentario.setText(a.getComentario());
    }

    @Override
    protected void limparForm() {
        cbPedido.setEnabled(true);
        spNota.setValue(5);
        txtComentario.setText("");
    }

    @Override
    protected Avaliacao lerForm() {
        Pedido p = (Pedido) cbPedido.getSelectedItem();
        if (p == null) throw new IllegalArgumentException("Selecione o pedido avaliado.");
        String coment = txtComentario.getText().trim();
        if (coment.length() > 500) throw new IllegalArgumentException("O comentário pode ter no máximo 500 caracteres.");
        return new Avaliacao(selecionado == null ? 0 : selecionado.getId(), p.getId(), p.getIdCliente(),
                p.getNomeCliente(), (Integer) spNota.getValue(), coment,
                selecionado == null ? LocalDate.now() : selecionado.getData());
    }

    @Override
    protected void gravar(Avaliacao a) throws SQLException {
        if (selecionado == null) dao.inserir(a); else dao.atualizar(a);
    }

    @Override protected void remover(Avaliacao a) throws SQLException { dao.excluir(a.getId()); }
}
