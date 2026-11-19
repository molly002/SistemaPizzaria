package dao;

import model.Tamanho;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TamanhoDAO implements CrudDAO<Tamanho> {
    @Override
    public void inserir(Tamanho t) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO tamanho (qtd_pedacos) VALUES (?)")) {
            ps.setInt(1, t.getQtdPedacos());
            ps.executeUpdate();
        }
    }
    @Override
    public void atualizar(Tamanho t) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("UPDATE tamanho SET qtd_pedacos=? WHERE id_tamanho=?")) {
            ps.setInt(1, t.getQtdPedacos());
            ps.setInt(2, t.getId());
            ps.executeUpdate();
        }
    }
    @Override
    public void excluir(int id) throws SQLException {
        try (Connection cn = Conexao.getConexao();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM tamanho WHERE id_tamanho=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    @Override
    public List<Tamanho> listar() throws SQLException {
        List<Tamanho> lista = new ArrayList<>();
        try (Connection cn = Conexao.getConexao();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_tamanho, qtd_pedacos FROM tamanho ORDER BY qtd_pedacos")) {
            while (rs.next()) lista.add(new Tamanho(rs.getInt(1), rs.getInt(2)));
        }
        return lista;
    }
}
