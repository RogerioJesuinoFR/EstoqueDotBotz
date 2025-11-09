package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entities.Item;

public class ItemDAO {

	private Connection conn;
	
	public ItemDAO(Connection conn) {
		this.conn = conn;
	}
	
	public int cadastrar(Item item) throws SQLException {
	    PreparedStatement st = null;
	    
	    // SQL Corrigido: Remove id_item e data_criacao (AUTO_INCREMENT/DEFAULT)
	    String sql = "INSERT INTO itens (nome, id_categoria, descricao, quantidade_atual, quantidade_minima, unidade_medida, validade, setor) " +
	                 "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    try {
	        st = conn.prepareStatement(sql);
	        
	        // CUIDADO: Assumimos que item.getCategoria() retorna o ID (String)
	        st.setString(1, item.getNomeItem());
	        st.setString(2, item.getCategoria()); 
	        st.setString(3, item.getDescricaoItem());
	        st.setInt(4, item.getQuantidadeAtualItem());
	        st.setInt(5, item.getQuantidadeMinimaItem());
	        st.setString(6, item.getUnidadeMedidaItem());
	        st.setString(7, item.getValidadeItem()); // Ajuste o formato da data para 'YYYY-MM-DD' no front-end
	        st.setString(8, item.getSetorItem());
	        
	        return st.executeUpdate();
	        
	    } finally {
	        BancoDados.finalizarStatement(st);
	    }
	}
	
	public List<Item> buscarTodos() throws SQLException {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select * from itens order by nome");
			
			rs = st.executeQuery();
			
			List<Item> listaItens = new ArrayList<>();
			
			while (rs.next()) {
				
				Item item = new Item();
				
				item.setIdItem(rs.getString("id_item"));
				item.setNomeItem(rs.getString("nome"));
				item.setCategoria(rs.getString("id_categoria"));
				item.setDescricaoItem(rs.getString("descricao"));
				item.setQuantidadeAtualItem(rs.getInt("quantidade_atual"));
				item.setQuantidadeMinimaItem(rs.getInt("quantidade_minima"));
				item.setUnidadeMedidaItem(rs.getString("unidade_medida"));
				item.setValidadeItem(rs.getString("validade"));
				item.setSetorItem(rs.getString("setor"));
				item.setDataCriacaoItem(rs.getString("data_criacao"));
				
				listaItens.add(item);
			}
			
			return listaItens;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
		}
	}
	
	public List<Item> buscarPorSetorEAmbos(String setorDoUsuario) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    
	    String sql;
	    if (setorDoUsuario.equals("AMBOS")) {
	        sql = "SELECT * FROM itens ORDER BY nome";
	    } else {
	        sql = "SELECT * FROM itens WHERE setor = ? OR setor = 'AMBOS' ORDER BY nome";
	    }
	    
	    try {
	        // *** USA A CONEXÃO INJETADA (this.conn) ***
	        st = this.conn.prepareStatement(sql); 
	        
	        if (!setorDoUsuario.equals("AMBOS")) {
	            st.setString(1, setorDoUsuario); 
	        }
	        
	        rs = st.executeQuery();
	        
	        List<Item> listaItens = new ArrayList<>();
	        
	        while (rs.next()) {
	            Item item = new Item();
	            item.setIdItem(rs.getString("id_item"));
	            item.setNomeItem(rs.getString("nome"));
	            item.setCategoria(rs.getString("id_categoria")); 
	            item.setDescricaoItem(rs.getString("descricao"));
	            item.setQuantidadeAtualItem(rs.getInt("quantidade_atual"));
	            item.setQuantidadeMinimaItem(rs.getInt("quantidade_minima"));
	            item.setUnidadeMedidaItem(rs.getString("unidade_medida"));
	            item.setValidadeItem(rs.getString("validade"));
	            item.setSetorItem(rs.getString("setor"));
	            item.setDataCriacaoItem(rs.getString("data_criacao"));
	            
	            listaItens.add(item);
	        }
	        
	        return listaItens;
	        
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	    }
	}
	
	public Item buscarPorID(String id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            st = conn.prepareStatement("SELECT * FROM itens WHERE id_item = ?");
            st.setString(1, id);
            rs = st.executeQuery();
            
            if (rs.next()) {
                Item item = new Item();
                item.setIdItem(rs.getString("id_item"));
                item.setNomeItem(rs.getString("nome"));
                item.setCategoria(rs.getString("id_categoria"));
                item.setDescricaoItem(rs.getString("descricao"));
                item.setQuantidadeAtualItem(rs.getInt("quantidade_atual"));
                item.setQuantidadeMinimaItem(rs.getInt("quantidade_minima"));
                item.setUnidadeMedidaItem(rs.getString("unidade_medida"));
                item.setValidadeItem(rs.getString("validade"));
                item.setSetorItem(rs.getString("setor"));
                item.setDataCriacaoItem(rs.getString("data_criacao"));
                return item;
            }
            return null;
            
        } finally {
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
        }
    }
    
	// ** MÉTODO ATUALIZAR (UPDATE) **
	public int atualizar(Item item) throws SQLException {
		PreparedStatement st = null;
		
		// O campo 'data_criacao' não deve ser atualizado.
		String sql = "UPDATE itens SET nome = ?, id_categoria = ?, descricao = ?, quantidade_atual = ?, " +
		             "quantidade_minima = ?, unidade_medida = ?, validade = ?, setor = ? WHERE id_item = ?";
		
		try {
			st = conn.prepareStatement(sql);
			
			st.setString(1, item.getNomeItem());
			st.setString(2, item.getCategoria());
			st.setString(3, item.getDescricaoItem());
			st.setInt(4, item.getQuantidadeAtualItem());
			st.setInt(5, item.getQuantidadeMinimaItem());
			st.setString(6, item.getUnidadeMedidaItem());
			st.setString(7, item.getValidadeItem());
			st.setString(8, item.getSetorItem());
			st.setString(9, item.getIdItem()); // WHERE id_item
			
			return st.executeUpdate();
			
		} finally {
			BancoDados.finalizarStatement(st);
		}
	}
	
	public int excluir(String id) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("delete from itens where id_item = ?");
			
			st.setString(1, id);
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar(conn);
		}
	}
}
