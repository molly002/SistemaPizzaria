package dao;

import model.Pedido;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements CrudDAO<Pedido> {
    @Override
    public void inserir(Pedido p) throws SQLException {
        String sql = "INSERT INTO pedido (id_cliente, id_func, status_pedido) VALUES (?,?,?)";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCliente());
            if (p.getIdFunc() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, p.getIdFunc());
            ps.setString(3, p.getStatus());
            ps.executeUpdate();
        }
    }
    @Override
    public void atualizar(Pedido p) throws SQLException {
        String sql = "UPDATE pedido SET id_cliente=?, id_func=?, status_pedido=? WHERE id_pedido=?";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCliente());
            if (p.getIdFunc() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, p.getIdFunc());
            ps.setString(3, p.getStatus());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    /** Exclui o pedido e todos os registros dependentes (transação). */
    @Override
    public void excluir(int id) throws SQLException {
        String[] sqls = {
            "DELETE FROM relatorio WHERE id_pedido=?",
            "DELETE FROM avaliacao WHERE id_pedido=?",
            "DELETE FROM pagamento WHERE id_pedido=?",
            "DELETE FROM pizza WHERE id_pedido=?",
            "DELETE FROM pedido WHERE id_pedido=?"
        };
        try (Connection cn = Conexao.getConexao()) {
            cn.setAutoCommit(false);
            try {
                for (String sql : sqls) {
                    try (PreparedStatement ps = cn.prepareStatement(sql)) {
                        ps.setInt(1, id);
                        ps.executeUpdate();
                    }
                }
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        }
    }

    /** Recalcula valor_pedido como a soma das pizzas do pedido. */
    public void atualizarValor(int idPedido) throws SQLException {
        String sql = "UPDATE pedido SET valor_pedido = (SELECT COALESCE(SUM(valor_unitario),0) FROM pizza WHERE id_pedido=?) WHERE id_pedido=?";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Pedido> listar() throws SQLException {
        String sql = "SELECT p.id_pedido, p.id_cliente, c.nome_cliente, p.id_func, f.nome_func, "
                + "p.data_hora_pedido, p.status_pedido, p.valor_pedido "
                + "FROM pedido p JOIN cliente c ON c.id_cliente=p.id_cliente "
                + "LEFT JOIN cozinha f ON f.id_func=p.id_func ORDER BY p.id_pedido DESC";
        List<Pedido> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt(1));
                p.setIdCliente(rs.getInt(2));
                p.setNomeCliente(rs.getString(3));
                int f = rs.getInt(4);
                p.setIdFunc(rs.wasNull() ? null : f);
                p.setNomeFunc(rs.getString(5));
                p.setDataHora(rs.getTimestamp(6).toLocalDateTime());
                p.setStatus(rs.getString(7));
                p.setValor(rs.getBigDecimal(8));
                lista.add(p);
            }
        }
        return lista;
    }
}
