package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Painel base de CRUD (Template Method): monta tabela, formulário e botões
 * Novo / Salvar / Excluir / Atualizar. As subclasses só definem os campos e
 * como ler/gravar o objeto. Subclasses devem chamar montar() no fim do construtor.
 */
public abstract class CrudPanel<T> extends JPanel implements Atualizavel {
    protected DefaultTableModel modelo;
    protected JTable tabela;
    protected List<T> dados = new ArrayList<>();
    protected T selecionado;
    protected final JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JPanel form = new JPanel(new GridBagLayout());
    private int linhaForm = 0;

    protected CrudPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // ---- pontos de extensão ----
    protected abstract String[] colunas();
    protected abstract List<T> listar() throws SQLException;
    protected abstract Object[] linha(T obj);
    protected abstract void preencherForm(T obj);
    protected abstract void limparForm();
    /** Lê e valida o formulário; lança IllegalArgumentException com a mensagem de erro. */
    protected abstract T lerForm();
    protected abstract void gravar(T obj) throws SQLException;
    protected abstract void remover(T obj) throws SQLException;
    /** Recarrega listas auxiliares (combos) antes de listar. */
    protected void atualizarListas() throws SQLException { }

    protected void addCampo(String rotulo, JComponent comp) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 3, 3, 3);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0; g.gridy = linhaForm;
        form.add(new JLabel(rotulo), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        form.add(comp, g);
        linhaForm++;
    }

    protected void montar() {
        modelo = new DefaultTableModel(colunas(), 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(22);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = tabela.getSelectedRow();
            if (r >= 0 && r < dados.size()) {
                selecionado = dados.get(r);
                preencherForm(selecionado);
            }
        });

        JButton btNovo = new JButton("Novo");
        JButton btSalvar = new JButton("Salvar");
        JButton btExcluir = new JButton("Excluir");
        JButton btAtualizar = new JButton("Atualizar lista");
        btNovo.addActionListener(e -> novo());
        btSalvar.addActionListener(e -> salvarAcao());
        btExcluir.addActionListener(e -> excluirAcao());
        btAtualizar.addActionListener(e -> carregar());
        botoes.add(btNovo);
        botoes.add(btSalvar);
        botoes.add(btExcluir);
        botoes.add(btAtualizar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        carregar();
    }

    protected void novo() {
        selecionado = null;
        tabela.clearSelection();
        limparForm();
    }

    public void carregar() {
        try {
            atualizarListas();
            dados = listar();
            modelo.setRowCount(0);
            for (T t : dados) modelo.addRow(linha(t));
            novo();
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    @Override public void atualizar() { carregar(); }

    private void salvarAcao() {
        try {
            T obj = lerForm();
            gravar(obj);
            carregar();
            JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    private void excluirAcao() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um registro na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o registro selecionado?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        try {
            remover(selecionado);
            carregar();
        } catch (SQLException ex) {
            erro(ex);
        }
    }

    protected void erro(SQLException ex) {
        String msg;
        switch (ex.getErrorCode()) {
            case 1451: msg = "Não é possível excluir: o registro está sendo usado em outro cadastro."; break;
            case 1062: msg = "Já existe um registro com esses dados."; break;
            default: msg = "Erro no banco de dados: " + ex.getMessage();
        }
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    // ---- utilitários de combo ----
    protected static <E> void preencher(JComboBox<E> cb, List<E> itens, boolean comVazio) {
        cb.removeAllItems();
        if (comVazio) cb.addItem(null);
        for (E e : itens) cb.addItem(e);
    }

    protected static <E> void selecionar(JComboBox<E> cb, int id, ToIntFunction<E> getId) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            E item = cb.getItemAt(i);
            if (item != null && getId.applyAsInt(item) == id) { cb.setSelectedIndex(i); return; }
        }
    }

    /** Combo que mostra um texto quando o item é nulo. */
    protected static <E> void textoParaNulo(JComboBox<E> cb, String texto) {
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                return super.getListCellRendererComponent(l, v == null ? texto : v, i, s, f);
            }
        });
    }
}
