package services;

import dao.BancoDados;
import dao.ItemDAO;
import entities.Item;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ItemService {

    // 1. Serviço de Busca por Perfil
	public List<Item> buscarItensPorPerfil(String userProfile) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar(); 
            ItemDAO dao = new ItemDAO(conn);
            return dao.buscarPorSetorEAmbos(userProfile); 
        } finally {
            BancoDados.desconectar(conn); 
        }
    }
	
    // 2. Serviço de Cadastro
	public boolean cadastrarNovoItem(Item item) throws SQLException, IOException {
	    Connection conn = null;
	    try {
	        conn = BancoDados.conectar();
	        conn.setAutoCommit(false); 
	        ItemDAO dao = new ItemDAO(conn);
	        int linhasAfetadas = dao.cadastrar(item);
	        
	        if (linhasAfetadas > 0) {
	            conn.commit();
	            return true;
	        }
            conn.rollback();
	        return false;
	    } catch (SQLException e) {
	        if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
	        throw e;
	    } finally {
	        BancoDados.desconectar(conn);
	    }
	}
    
    // 3. Serviço de Busca por ID
    public Item buscarItemPorID(String itemId) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            ItemDAO dao = new ItemDAO(conn);
            return dao.buscarPorID(itemId);
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    // 4. Serviço de Atualização (Item Completo)
    public boolean atualizarItem(Item item) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false); 
            ItemDAO dao = new ItemDAO(conn);
            int linhasAfetadas = dao.atualizar(item);
            
            if (linhasAfetadas > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }

    // 5. Serviço de Atualização de Quantidade
    public boolean atualizarQuantidadeItem(String idItem, int novaQuantidade) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            ItemDAO dao = new ItemDAO(conn);
            int linhasAfetadas = dao.atualizarQuantidade(idItem, novaQuantidade);
            
            if (linhasAfetadas > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    // ** 6. NOVO: SERVIÇO DE REMOÇÃO DE ITEM **
    public boolean removerItem(String idItem) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            
            ItemDAO dao = new ItemDAO(conn);
            int linhasAfetadas = dao.excluir(idItem);
            
            if (linhasAfetadas > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }
}