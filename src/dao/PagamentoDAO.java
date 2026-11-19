package dao;

import model.Pagamento;
import util.Conexao;
import java.sql.*;

public class PagamentoDAO {
    public Pagamento buscarPorPedido(int idPedido) throws SQLException {
        String sql = "SELECT id_pagamento, status_pagamento, metodo_pagamento FROM pagamento WHERE id_pedido=?";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Pagamento(rs.getInt(1), idPedido, rs.getString(2), rs.getString(3));
            }
        }
        return null;
    }

    /** Insere ou atualiza o pagamento do pedido (id_pedido é UNIQUE). */
    public void salvar(Pagamento p) throws SQLException {
        String sql = "INSERT INTO pagamento (id_pedido, status_pagamento, metodo_pagamento) VALUES (?,?,?) "
                + "ON DUPLICATE KEY UPDATE status_pagamento=VALUES(status_pagamento), metodo_pagamento=VALUES(metodo_pagamento)";
        try (Connection cn = Conexao.getConexao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdPedido());
            ps.setString(2, p.getStatus());
            ps.setString(3, p.getMetodo());
            ps.executeUpdate();
        }
    }
}
