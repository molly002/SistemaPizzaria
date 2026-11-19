package view;

import dao.ClienteDAO;
import dao.FuncionarioDAO;
import dao.PedidoDAO;
import model.Cliente;
import model.Funcionario;
import model.Pedido;
import util.Formato;
import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class PedidoPanel extends CrudPanel<Pedido> {
    private static final String[] STATUS = {"Recebido", "Em preparo", "Pronto", "Saiu para entrega", "Entregue", "Cancelado"};

    private final PedidoDAO dao = new PedidoDAO();
    private final JComboBox<Cliente> cbCliente = new JComboBox<>();
    private final JComboBox<Funcionario> cbFunc = new JComboBox<>();
    private final JComboBox<String> cbStatus = new JComboBox<>(STATUS);
    private final JLabel lblValor = new JLabel("R$ 0,00");

    public PedidoPanel() {
        textoParaNulo(cbFunc, "(não atribuído)");
        addCampo("Cliente:", cbCliente);
        addCampo("Funcionário da cozinha:", cbFunc);
        addCampo("Status:", cbStatus);
        addCampo("Valor total:", lblValor);

        JButton btPizzas = new JButton("Pizzas e pagamento...");
        btPizzas.addActionListener(e -> abrirPizzas());
        botoes.add(btPizzas);
        montar();
    }

    private void abrirPizzas() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Salve o pedido e selecione-o na tabela para adicionar pizzas.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new PizzaDialog(SwingUtilities.getWindowAncestor(this), selecionado).setVisible(true);
        carregar();
    }

    @Override
    protected void atualizarListas() throws SQLException {
        preencher(cbCliente, new ClienteDAO().listar(), false);
        preencher(cbFunc, new FuncionarioDAO().listar(), true);
    }

    @Override protected String[] colunas() { return new String[]{"Nº", "Cliente", "Cozinha", "Data/Hora", "Status", "Valor"}; }
    @Override protected List<Pedido> listar() throws SQLException { return dao.listar(); }
    @Override protected Object[] linha(Pedido p) {
        return new Object[]{p.getId(), p.getNomeCliente(), p.getNomeFunc() == null ? "-" : p.getNomeFunc(),
                Formato.dataHora(p.getDataHora()), p.getStatus(), Formato.moeda(p.getValor())};
    }

    @Override
    protected void preencherForm(Pedido p) {
        selecionar(cbCliente, p.getIdCliente(), Cliente::getId);
        if (p.getIdFunc() == null) cbFunc.setSelectedIndex(0); else selecionar(cbFunc, p.getIdFunc(), Funcionario::getId);
        cbStatus.setSelectedItem(p.getStatus());
        lblValor.setText(Formato.moeda(p.getValor()));
    }

    @Override
    protected void limparForm() {
        if (cbCliente.getItemCount() > 0) cbCliente.setSelectedIndex(0);
        if (cbFunc.getItemCount() > 0) cbFunc.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        lblValor.setText(Formato.moeda(java.math.BigDecimal.ZERO));
    }

    @Override
    protected Pedido lerForm() {
        Cliente c = (Cliente) cbCliente.getSelectedItem();
        if (c == null) throw new IllegalArgumentException("Cadastre e selecione um cliente.");
        Funcionario f = (Funcionario) cbFunc.getSelectedItem();
        Pedido p = new Pedido();
        p.setId(selecionado == null ? 0 : selecionado.getId());
        p.setIdCliente(c.getId());
        p.setIdFunc(f == null ? null : f.getId());
        p.setStatus((String) cbStatus.getSelectedItem());
        return p;
    }

    @Override
    protected void gravar(Pedido p) throws SQLException {
        if (selecionado == null) dao.inserir(p); else dao.atualizar(p);
    }

    @Override
    protected void remover(Pedido p) throws SQLException { dao.excluir(p.getId()); }
}
