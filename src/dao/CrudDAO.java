package dao;

import java.sql.SQLException;
import java.util.List;

/** Contrato genérico de CRUD para os DAOs. */
public interface CrudDAO<T> {
    void inserir(T obj) throws SQLException;
    void atualizar(T obj) throws SQLException;
    void excluir(int id) throws SQLException;
    List<T> listar() throws SQLException;
}
