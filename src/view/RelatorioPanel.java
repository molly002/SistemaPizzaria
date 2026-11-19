package view;

import dao.RelatorioDAO;
import util.Formato;
import util.Sessao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

/** Relatório de faturamento (pedidos pagos), registrado pelo administrador logado. */
public class RelatorioPanel extends JPanel implements Atualizavel {
    private final RelatorioDAO dao = new RelatorioDAO();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Nº", "Administrador", "Pedido", "Cliente", "Faturamento"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JLabel lblTotal = new JLabel();

    public RelatorioPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btGerar = new JButton("Gerar relatório (pedidos pagos)");
        JButton btLimpar = new JButton("Limpar relatório");
        JButton btAtualizar = new JButton("Atualizar");
        btGerar.addActionListener(e -> gerar());
        btLimpar.addActionListener(e -> limpar());
        btAtualizar.addActionListener(e -> atualizar());
        topo.add(btGerar); topo.add(btLimpar); topo.add(btAtualizar);

        JTable tabela = new JTable(modelo);
        tabela.setRowHeight(22);
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 14f));

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(lblTotal, BorderLayout.SOUTH);
        atualizar();
    }

    @Override
    public void atualizar() {
        try {
            modelo.setRowCount(0);
            for (Object[] l : dao.listar()) {
                l[4] = Formato.moeda((BigDecimal) l[4]);
                modelo.addRow(l);
            }
            lblTotal.setText("Faturamento total: " + Formato.moeda(dao.total()));
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    private void gerar() {
        try {
            int n = dao.gerar(Sessao.admin.getId());
            atualizar();
            JOptionPane.showMessageDialog(this, n + " pedido(s) pago(s) lançado(s) no relatório.");
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    private void limpar() {
        if (JOptionPane.showConfirmDialog(this, "Apagar todas as linhas do relatório?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            dao.limpar();
            atualizar();
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    private void erro(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erro no banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
