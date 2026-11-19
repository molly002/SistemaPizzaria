package dao;

import model.Cliente;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements CrudDAO<Cliente> {
    @Override
    public void inserir(Cliente c) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO cliente (nome_cliente) VALUES (?)")) {
            ps.setString(1, c.getNome());
            ps.executeUpdate();
        }
    }
    @Override
    public void atualizar(Cliente c) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("UPDATE cliente SET nome_cliente=? WHERE id_cliente=?")) {
            ps.setString(1, c.getNome());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        }
    }
    @Override
    public void excluir(int id) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM cliente WHERE id_cliente=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    @Override
    public List<Cliente> listar() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_cliente, nome_cliente FROM cliente ORDER BY nome_cliente")) {
            while (rs.next()) lista.add(new Cliente(rs.getInt(1), rs.getString(2)));
        }
        return lista;
    }
}
