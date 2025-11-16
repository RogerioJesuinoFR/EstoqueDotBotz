package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entities.Categoria;

public class CategoriaDAO {
	
	private Connection conn;
	
	public CategoriaDAO(Connection conn) {
		this.conn = conn;
	}
	
	public int cadastrar(Categoria categoria) throws SQLException {
		PreparedStatement st = null;
		try {
            // SQL CORRIGIDO: Adiciona 'setor', remove 'data_criacao'
			st = conn.prepareStatement("INSERT INTO categorias (nome, descricao, setor) VALUES (?, ?, ?)");
			
			st.setString(1, categoria.getNomeCategoria());
			st.setString(2, categoria.getDescricaoCategoria());
			st.setString(3, categoria.getSetor()); // Adicionado
			
			return st.executeUpdate();
		} finally {
			BancoDados.finalizarStatement(st);
			// REMOVIDO: Desconectar
		}
	}
	
	public List<Categoria> buscarTodos() throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
            // SQL CORRIGIDO: Adiciona 'setor'
			st = conn.prepareStatement("SELECT * FROM categorias ORDER BY nome");
			rs = st.executeQuery();
			List<Categoria> listaCategorias = new ArrayList<>();
			
			while (rs.next()) {
				Categoria categoria = new Categoria();
				categoria.setIdCategoria(rs.getString("id_categoria"));
				categoria.setNomeCategoria(rs.getString("nome"));
				categoria.setDescricaoCategoria(rs.getString("descricao"));
				categoria.setDataCriacaoCategoria(rs.getString("data_criacao"));
                categoria.setSetor(rs.getString("setor")); // Adicionado
				listaCategorias.add(categoria);
			}
			return listaCategorias;
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			// REMOVIDO: Desconectar
		}
	}

    // NOVO: Busca categorias por setor (lógica de filtro)
	public List<Categoria> buscarPorSetorEAmbos(String setorDoUsuario) throws SQLException {
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    String sql;
	    
	    if (setorDoUsuario.equals("AMBOS")) {
	        sql = "SELECT * FROM categorias ORDER BY nome";
	    } else {
	        sql = "SELECT * FROM categorias WHERE setor = ? OR setor = 'AMBOS' ORDER BY nome";
	    }
	    
	    try {
	        st = this.conn.prepareStatement(sql); 
	        if (!setorDoUsuario.equals("AMBOS")) {
	            st.setString(1, setorDoUsuario); 
	        }
	        rs = st.executeQuery();
	        
	        List<Categoria> listaCategorias = new ArrayList<>();
	        while (rs.next()) {
	            Categoria categoria = new Categoria();
				categoria.setIdCategoria(rs.getString("id_categoria"));
				categoria.setNomeCategoria(rs.getString("nome"));
				categoria.setDescricaoCategoria(rs.getString("descricao"));
				categoria.setDataCriacaoCategoria(rs.getString("data_criacao"));
                categoria.setSetor(rs.getString("setor"));
				listaCategorias.add(categoria);
	        }
	        return listaCategorias;
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	        // REMOVIDO: Desconectar
	    }
	}

    // NOVO: Busca por ID (para edição)
    public Categoria buscarPorID(String id) throws SQLException {
        PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM categorias WHERE id_categoria = ?");
			st.setString(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Categoria categoria = new Categoria();
				categoria.setIdCategoria(rs.getString("id_categoria"));
				categoria.setNomeCategoria(rs.getString("nome"));
				categoria.setDescricaoCategoria(rs.getString("descricao"));
				categoria.setDataCriacaoCategoria(rs.getString("data_criacao"));
                categoria.setSetor(rs.getString("setor"));
				return categoria;
			}
			return null;
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			// REMOVIDO: Desconectar
		}
    }
	
	public Categoria buscarPorNome(String nome) throws SQLException {
		// (Mantido, mas com desconectar removido)
        return null;
	}
	
	public int atualizar(Categoria categoria) throws SQLException {
		PreparedStatement st = null;
		try {
            // SQL CORRIGIDO: Atualiza 'setor', remove 'data_criacao'
			st = conn.prepareStatement("UPDATE categorias SET nome = ?, descricao = ?, setor = ? WHERE id_categoria = ?");
			
			st.setString(1, categoria.getNomeCategoria());
			st.setString(2, categoria.getDescricaoCategoria());
			st.setString(3, categoria.getSetor()); // Adicionado
			st.setString(4, categoria.getIdCategoria()); // WHERE
			
			return st.executeUpdate();
		} finally {
			BancoDados.finalizarStatement(st);
			// REMOVIDO: Desconectar
		}
	}
	
	public int excluir(String id) throws SQLException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from categorias where id_categoria = ?");
			st.setString(1, id);
			return st.executeUpdate();
		} finally {
			BancoDados.finalizarStatement(st);
			// REMOVIDO: Desconectar
		}
	}
}