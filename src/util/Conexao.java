package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Fabrica de conexões JDBC com o MySQL. Ajuste USUARIO/SENHA para o seu ambiente. */
public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/pizzaria"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root";
    private static final String SENHA = "14042024";

    private Conexao() { }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
