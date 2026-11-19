package dao;

import model.PrecoSabor;
import model.Sabor;
import model.Tamanho;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrecoSaborDAO implements CrudDAO<PrecoSabor> {
    @Override
    public void inserir(PrecoSabor p) throws SQLException {
        String sql = "INSERT INTO preco_sabor (id_sabor, id_tamanho, valor_unitario) VALUES (?,?,?)";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, p.getSabor().getId());
            ps.setInt(2, p.getTamanho().getId());
            ps.setBigDecimal(3, p.getValor());
            ps.executeUpdate();
        }
    }
    @Override
    public void atualizar(PrecoSabor p) throws SQLException {
        String sql = "UPDATE preco_sabor SET id_sabor=?, id_tamanho=?, valor_unitario=? WHERE id_preco_sabor=?";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, p.getSabor().getId());
            ps.setInt(2, p.getTamanho().getId());
            ps.setBigDecimal(3, p.getValor());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }
    @Override
    public void excluir(int id) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM preco_sabor WHERE id_preco_sabor=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    @Override
    public List<PrecoSabor> listar() throws SQLException {
        String sql = "SELECT ps.id_preco_sabor, ps.valor_unitario, s.id_sabor, s.nome_sabor, t.id_tamanho, t.qtd_pedacos "
                + "FROM preco_sabor ps JOIN sabor s ON s.id_sabor=ps.id_sabor "
                + "JOIN tamanho t ON t.id_tamanho=ps.id_tamanho ORDER BY s.nome_sabor, t.qtd_pedacos";
        List<PrecoSabor> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new PrecoSabor(rs.getInt(1),
                        new Sabor(rs.getInt(3), rs.getString(4), null),
                        new Tamanho(rs.getInt(5), rs.getInt(6)),
                        rs.getBigDecimal(2)));
            }
        }
        return lista;
    }
}
