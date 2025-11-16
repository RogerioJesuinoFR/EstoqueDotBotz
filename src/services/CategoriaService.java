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
     * Busca todas as categorias (respeitando o perfil do usuário).
     * Nota: O CategoriaDAO foi ajustado para ter o método buscarPorSetorEAmbos.
     */
    public List<Categoria> buscarCategoriasPorPerfil(String userProfile) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            CategoriaDAO dao = new CategoriaDAO(conn);
            
            // Reutiliza a mesma lógica de filtro dos Itens
            return dao.buscarPorSetorEAmbos(userProfile);
            
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    /**
     * Busca UMA categoria pelo ID.
     */
    public Categoria buscarPorID(String id) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            CategoriaDAO dao = new CategoriaDAO(conn);
            return dao.buscarPorID(id);
        } finally {
            BancoDados.desconectar(conn);
        }
    }

    /**
     * Cadastra uma nova categoria.
     */
    public boolean cadastrarCategoria(Categoria categoria) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            
            CategoriaDAO dao = new CategoriaDAO(conn);
            int linhasAfetadas = dao.cadastrar(categoria);
            
            if (linhasAfetadas > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    /**
     * Atualiza uma categoria existente.
     */
    public boolean atualizarCategoria(Categoria categoria) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            
            CategoriaDAO dao = new CategoriaDAO(conn);
            int linhasAfetadas = dao.atualizar(categoria);
            
            if (linhasAfetadas > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    /**
     * Remove uma categoria.
     */
    public boolean excluirCategoria(String id) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            
            CategoriaDAO dao = new CategoriaDAO(conn);
            int linhasAfetadas = dao.excluir(id);
            
            if (linhasAfetadas > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    /**
     * (Mantido do seu código, mas atualizado para fechar conexão no service)
     */
    public List<Categoria> buscarTodasCategorias() throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            CategoriaDAO dao = new CategoriaDAO(conn);
            return dao.buscarTodos();
        } finally {
            BancoDados.desconectar(conn);
        }
    }
}