package services;

import dao.BancoDados;
import dao.CategoriaDAO;
import entities.Categoria;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoriaService {

    /**
     * Busca todas as categorias existentes no banco de dados.
     * @return Uma lista de objetos Categoria.
     */
    public List<Categoria> buscarTodasCategorias() throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            CategoriaDAO dao = new CategoriaDAO(conn);
            
            // O CategoriaDAO.buscarTodos() é responsável por executar a query SELECT *
            List<Categoria> categorias = dao.buscarTodos();
            
            return categorias;
            
        } finally {
            BancoDados.desconectar(conn);
        }
    }
}