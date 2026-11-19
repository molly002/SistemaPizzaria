package dao;

import model.Administrador;
import util.Conexao;
import java.sql.*;

public class AdministradorDAO {
    /** Retorna o administrador se nome/senha conferem (senha comparada via SHA-256), ou null. */
    public Administrador autenticar(String nome, String senha) throws SQLException {
        String sql = "SELECT id_admin, nome_admin FROM administrador WHERE nome_admin=? AND senha_admin=SHA2(?,256)";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Administrador(rs.getInt(1), rs.getString(2), null);
            }
        }
        return null;
    }
}
