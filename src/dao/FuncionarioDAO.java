package dao;

import model.Funcionario;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO implements CrudDAO<Funcionario> {
    @Override
    public void inserir(Funcionario f) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO cozinha (nome_func) VALUES (?)")) {
            ps.setString(1, f.getNome());
            ps.executeUpdate();
        }
    }
    @Override
    public void atualizar(Funcionario f) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("UPDATE cozinha SET nome_func=? WHERE id_func=?")) {
            ps.setString(1, f.getNome());
            ps.setInt(2, f.getId());
            ps.executeUpdate();
        }
    }
    @Override
    public void excluir(int id) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM cozinha WHERE id_func=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    @Override
    public List<Funcionario> listar() throws SQLException {
        List<Funcionario> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_func, nome_func FROM cozinha ORDER BY nome_func")) {
            while (rs.next()) lista.add(new Funcionario(rs.getInt(1), rs.getString(2)));
        }
        return lista;
    }
}
