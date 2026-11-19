package view;

import dao.*;
import model.*;
import util.Formato;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Janela para gerenciar as pizzas e o pagamento de um pedido. */
public class PizzaDialog extends JDialog {
    private final Pedido pedido;
    private final PizzaDAO pizzaDAO = new PizzaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

    private final JComboBox<Tamanho> cbTamanho = new JComboBox<>();
    private final JComboBox<Sabor> cbSabor1 = new JComboBox<>();
    private final JComboBox<Sabor> cbSabor2 = new JComboBox<>();
    private final JComboBox<String> cbMetodo = new JComboBox<>(new String[]{"Dinheiro", "Cartão de crédito", "Cartão de débito", "PIX"});
    private final JComboBox<String> cbStatusPag = new JComboBox<>(new String[]{"Pendente", "Pago", "Cancelado"});
    private final JLabel lblTotal = new JLabel();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Nº", "Tamanho", "Sabor 1", "Sabor 2", "Valor"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);
    private List<Pizza> pizzas = new ArrayList<>();

    public PizzaDialog(Window dono, Pedido pedido) {
        super(dono, "Pizzas e pagamento - pedido " + pedido, ModalityType.APPLICATION_MODAL);
        this.pedido = pedido;
        setSize(720, 520);
        setLocationRelativeTo(dono);
        setLayout(new BorderLayout(8, 8));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        CrudPanel.textoParaNulo(cbSabor2, "(sem 2º sabor)");

        // --- formulário de nova pizza ---
        JPanel add = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add.setBorder(BorderFactory.createTitledBorder("Adicionar pizza"));
        add.add(new JLabel("Tamanho:")); add.add(cbTamanho);
        add.add(new JLabel("Sabor 1:")); add.add(cbSabor1);
        add.add(new JLabel("Sabor 2:")); add.add(cbSabor2);
        JButton btAdd = new JButton("Adicionar");
        btAdd.addActionListener(e -> adicionar());
        add.add(btAdd);
        add(add, BorderLayout.NORTH);

        // --- tabela ---
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- rodapé: remover, total e pagamento ---
        JPanel rodape = new JPanel(new GridLayout(2, 1, 4, 4));
        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btRem = new JButton("Remover pizza selecionada");
        btRem.addActionListener(e -> remover());
        linha1.add(btRem);
        linha1.add(lblTotal);
        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha2.setBorder(BorderFactory.createTitledBorder("Pagamento"));
        linha2.add(new JLabel("Método:")); linha2.add(cbMetodo);
        linha2.add(new JLabel("Status:")); linha2.add(cbStatusPag);
        JButton btPag = new JButton("Salvar pagamento");
        btPag.addActionListener(e -> salvarPagamento());
        linha2.add(btPag);
        rodape.add(linha1);
        rodape.add(linha2);
        add(rodape, BorderLayout.SOUTH);

        carregar();
    }

    private void carregar() {
        try {
            for (Tamanho t : new TamanhoDAO().listar()) cbTamanho.addItem(t);
            List<Sabor> sabores = new SaborDAO().listar();
            cbSabor2.addItem(null);
            for (Sabor s : sabores) { cbSabor1.addItem(s); cbSabor2.addItem(s); }
            Pagamento pg = pagamentoDAO.buscarPorPedido(pedido.getId());
            if (pg != null) { cbMetodo.setSelectedItem(pg.getMetodo()); cbStatusPag.setSelectedItem(pg.getStatus()); }
            recarregarPizzas();
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    private void recarregarPizzas() throws SQLException {
        pizzas = pizzaDAO.listarPorPedido(pedido.getId());
        modelo.setRowCount(0);
        BigDecimal total = BigDecimal.ZERO;
        for (Pizza z : pizzas) {
            modelo.addRow(new Object[]{z.getId(), z.getTamanho(), z.getSabor1(),
                    z.getSabor2() == null ? "-" : z.getSabor2(), Formato.moeda(z.getValor())});
            total = total.add(z.getValor());
        }
        lblTotal.setText("   Total do pedido: " + Formato.moeda(total));
    }

    private void adicionar() {
        Tamanho t = (Tamanho) cbTamanho.getSelectedItem();
        Sabor s1 = (Sabor) cbSabor1.getSelectedItem();
        Sabor s2 = (Sabor) cbSabor2.getSelectedItem();
        if (t == null || s1 == null) {
            aviso("Selecione o tamanho e o primeiro sabor.");
            return;
        }
        if (s2 != null && s2.getId() == s1.getId()) {
            aviso("Os dois sabores devem ser diferentes (ou deixe o 2º sabor vazio).");
            return;
        }
        try {
            BigDecimal p1 = pizzaDAO.buscarPreco(s1.getId(), t.getId());
            if (p1 == null) { aviso("Não há preço cadastrado para " + s1 + " no tamanho " + t + "."); return; }
            BigDecimal valor = p1;
            if (s2 != null) {
                BigDecimal p2 = pizzaDAO.buscarPreco(s2.getId(), t.getId());
                if (p2 == null) { aviso("Não há preço cadastrado para " + s2 + " no tamanho " + t + "."); return; }
                valor = p1.max(p2); // meio a meio: vale o preço do sabor mais caro
            }
            pizzaDAO.inserir(new Pizza(0, pedido.getId(), t, s1, s2, valor));
            pedidoDAO.atualizarValor(pedido.getId());
            recarregarPizzas();
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    private void remover() {
        int r = tabela.getSelectedRow();
        if (r < 0) { aviso("Selecione uma pizza na tabela."); return; }
        try {
            pizzaDAO.excluir(pizzas.get(r).getId());
            pedidoDAO.atualizarValor(pedido.getId());
            recarregarPizzas();
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    private void salvarPagamento() {
        try {
            pagamentoDAO.salvar(new Pagamento(0, pedido.getId(),
                    (String) cbStatusPag.getSelectedItem(), (String) cbMetodo.getSelectedItem()));
            JOptionPane.showMessageDialog(this, "Pagamento salvo!");
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    private void erro(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erro no banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
