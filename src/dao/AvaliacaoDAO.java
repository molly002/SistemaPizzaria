package dao;

import model.Avaliacao;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDAO implements CrudDAO<Avaliacao> {
    @Override
    public void inserir(Avaliacao a) throws SQLException {
        String sql = "INSERT INTO avaliacao (id_cliente, id_pedido, nota_avaliacao, comentario_avaliacao, data_avaliacao) VALUES (?,?,?,?,?)";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, a.getIdCliente());
            ps.setInt(2, a.getIdPedido());
            ps.setInt(3, a.getNota());
            ps.setString(4, a.getComentario());
            ps.setDate(5, Date.valueOf(a.getData()));
            ps.executeUpdate();
        }
    }
    @Override
    public void atualizar(Avaliacao a) throws SQLException {
        String sql = "UPDATE avaliacao SET nota_avaliacao=?, comentario_avaliacao=? WHERE id_avaliacao=?";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, a.getNota());
            ps.setString(2, a.getComentario());
            ps.setInt(3, a.getId());
            ps.executeUpdate();
        }
    }
    @Override
    public void excluir(int id) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM avaliacao WHERE id_avaliacao=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    @Override
    public List<Avaliacao> listar() throws SQLException {
        String sql = "SELECT a.id_avaliacao, a.id_pedido, a.id_cliente, c.nome_cliente, a.nota_avaliacao, "
                + "a.comentario_avaliacao, a.data_avaliacao FROM avaliacao a "
                + "JOIN cliente c ON c.id_cliente=a.id_cliente ORDER BY a.id_avaliacao DESC";
        List<Avaliacao> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Avaliacao(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4),
                        rs.getInt(5), rs.getString(6), rs.getDate(7).toLocalDate()));
            }
        }
        return lista;
    }
}
