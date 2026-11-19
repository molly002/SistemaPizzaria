package dao;

import util.Conexao;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {
    /** Lança no relatório os pedidos com pagamento "Pago" que ainda não constam nele. Retorna quantos foram lançados. */
    public int gerar(int idAdmin) throws SQLException {
        String sql = "INSERT INTO relatorio (id_admin, id_pedido, faturamento_relatorio) "
                + "SELECT ?, p.id_pedido, p.valor_pedido FROM pedido p "
                + "JOIN pagamento pg ON pg.id_pedido=p.id_pedido "
                + "WHERE pg.status_pagamento='Pago' AND p.id_pedido NOT IN (SELECT id_pedido FROM relatorio)";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAdmin);
            return ps.executeUpdate();
        }
    }

    public void limpar() throws SQLException {
        try (Connection cn = Conexao.getConexao(); Statement st = cn.createStatement()) {
            st.executeUpdate("DELETE FROM relatorio");
        }
    }

    /** Linhas: Nº relatório, Administrador, Pedido, Cliente, Faturamento. */
    public List<Object[]> listar() throws SQLException {
        String sql = "SELECT r.id_relatorio, a.nome_admin, r.id_pedido, c.nome_cliente, r.faturamento_relatorio "
                + "FROM relatorio r JOIN administrador a ON a.id_admin=r.id_admin "
                + "JOIN pedido p ON p.id_pedido=r.id_pedido JOIN cliente c ON c.id_cliente=p.id_cliente "
                + "ORDER BY r.id_relatorio";
        List<Object[]> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getBigDecimal(5)});
            }
        }
        return lista;
    }

    public BigDecimal total() throws SQLException {
        try (Connection cn = Conexao.getConexao(); Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(SUM(faturamento_relatorio),0) FROM relatorio")) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }
}
