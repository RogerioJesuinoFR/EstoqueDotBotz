package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import entities.Categoria;

public class CategoriaDAO {
	
	private Connection conn;
	
	public CategoriaDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public int cadastrar(Categoria categoria) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("Insert into categorias (nome, descricao, data_criacao values (?, ?, ?)");
			
			st.setString(1, categoria.getNomeCategoria());
			st.setString(2, categoria.getDescricaoCategoria());
			st.setDate(3, categoria.getDataCriacaoCategoria());
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public List<Categoria> buscarTodos() throws SQLException {
		
		return null;
	}
	
	public Categoria buscarPorNome(String nome) throws SQLException {
		
		return null;
	}
	
	public String atualizar(Categoria categoria) throws SQLException {
		
		return null;
	}
	
	public String excluir(String nome) throws SQLException {
		
		return 0;
	}
}
