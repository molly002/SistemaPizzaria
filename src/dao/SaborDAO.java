package dao;

import model.Sabor;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaborDAO implements CrudDAO<Sabor> {
    @Override
    public void inserir(Sabor s) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO sabor (nome_sabor, descricao_sabor) VALUES (?,?)")) {
            ps.setString(1, s.getNome());
            ps.setString(2, s.getDescricao());
            ps.executeUpdate();
        }
    }
    @Override
    public void atualizar(Sabor s) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("UPDATE sabor SET nome_sabor=?, descricao_sabor=? WHERE id_sabor=?")) {
            ps.setString(1, s.getNome());
            ps.setString(2, s.getDescricao());
            ps.setInt(3, s.getId());
            ps.executeUpdate();
        }
    }
    @Override
    public void excluir(int id) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM sabor WHERE id_sabor=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    @Override
    public List<Sabor> listar() throws SQLException {
        List<Sabor> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_sabor, nome_sabor, descricao_sabor FROM sabor ORDER BY nome_sabor")) {
            while (rs.next()) lista.add(new Sabor(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return lista;
    }
}
