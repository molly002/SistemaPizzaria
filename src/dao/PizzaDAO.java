package dao;

import model.Pizza;
import model.Sabor;
import model.Tamanho;
import util.Conexao;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PizzaDAO {
    public void inserir(Pizza z) throws SQLException {
        String sql = "INSERT INTO pizza (id_pedido, id_tamanho, id_sabor1, id_sabor2, valor_unitario) VALUES (?,?,?,?,?)";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, z.getIdPedido());
            ps.setInt(2, z.getTamanho().getId());
            ps.setInt(3, z.getSabor1().getId());
            if (z.getSabor2() == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, z.getSabor2().getId());
            ps.setBigDecimal(5, z.getValor());
            ps.executeUpdate();
        }
    }

    public void excluir(int idPizza) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM pizza WHERE id_pizza=?")) {
            ps.setInt(1, idPizza);
            ps.executeUpdate();
        }
    }

    public List<Pizza> listarPorPedido(int idPedido) throws SQLException {
        String sql = "SELECT z.id_pizza, z.valor_unitario, t.id_tamanho, t.qtd_pedacos, "
                + "s1.id_sabor, s1.nome_sabor, s2.id_sabor, s2.nome_sabor "
                + "FROM pizza z JOIN tamanho t ON t.id_tamanho=z.id_tamanho "
                + "JOIN sabor s1 ON s1.id_sabor=z.id_sabor1 "
                + "LEFT JOIN sabor s2 ON s2.id_sabor=z.id_sabor2 WHERE z.id_pedido=? ORDER BY z.id_pizza";
        List<Pizza> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id2 = rs.getInt(7);
                    Sabor s2 = rs.wasNull() ? null : new Sabor(id2, rs.getString(8), null);
                    lista.add(new Pizza(rs.getInt(1), idPedido,
                            new Tamanho(rs.getInt(3), rs.getInt(4)),
                            new Sabor(rs.getInt(5), rs.getString(6), null), s2, rs.getBigDecimal(2)));
                }
            }
        }
        return lista;
    }

    /** Preço cadastrado do sabor no tamanho, ou null se não existir. */
    public BigDecimal buscarPreco(int idSabor, int idTamanho) throws SQLException {
        String sql = "SELECT valor_unitario FROM preco_sabor WHERE id_sabor=? AND id_tamanho=?";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idSabor);
            ps.setInt(2, idTamanho);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getBigDecimal(1) : null;
            }
        }
    }
}
