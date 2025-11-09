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
	        return false;
	        
	    } catch (SQLException e) {
	        if (conn != null) {
	            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
	        }
	        throw e;
	    } finally {
	        BancoDados.desconectar(conn);
	    }
	}
    
    // 3. (NOVO) Serviço de Busca por ID para Edição
    public Item buscarItemPorID(String itemId) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            ItemDAO dao = new ItemDAO(conn);
            return dao.buscarPorID(itemId); // Chama o método do DAO
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    // 4. (NOVO) Serviço de Atualização (UPDATE)
    public boolean atualizarItem(Item item) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false); 
            
            ItemDAO dao = new ItemDAO(conn);
            int linhasAfetadas = dao.atualizar(item); // Chama o método UPDATE
            
            if (linhasAfetadas > 0) {
                conn.commit();
                return true;
            }
            return false;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }
}