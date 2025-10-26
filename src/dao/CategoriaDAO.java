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
			
			st = conn.prepareStatement("Insert into categorias (nome, descricao, data_criacao) values (?, ?, ?)");
			
			st.setString(1, categoria.getNomeCategoria());
			st.setString(2, categoria.getDescricaoCategoria());
			st.setString(3, categoria.getDataCriacaoCategoria());
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar(conn);
		}
	}
	
	public List<Categoria> buscarTodos() throws SQLException {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select * from categorias order by nome");
			
			rs = st.executeQuery();
			
			List<Categoria> listaCategorias = new ArrayList<>();
			
			while (rs.next()) {
				
				Categoria categoria = new Categoria();
				
				categoria.setNomeCategoria(rs.getString("nome"));
				categoria.setDescricaoCategoria(rs.getString("descricao"));
				categoria.setDataCriacaoCategoria(rs.getString("data_criacao"));
				
				listaCategorias.add(categoria);
			}
			
			return listaCategorias;
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar(conn);
		}
	}
	
	public Categoria buscarPorNome(String nome) throws SQLException {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select * from categorias where nome = ?");
			
			st.setString(1, nome);
			rs = st.executeQuery();
			
			if (rs.next()) {
				
				Categoria categoria = new Categoria();
				
				categoria.setNomeCategoria(rs.getString("nome"));
				categoria.setDescricaoCategoria(rs.getString("descricao"));
				categoria.setDataCriacaoCategoria(rs.getString("data_criacao"));
				
				return categoria;
			}
			
			return null;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar(conn);
		}
	}
	
	public int atualizar(Categoria categoria) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("update categorias set nome = ?, descricao = ?, data_criacao = ? where id_categoria = ?");
			
			st.setString(1, categoria.getNomeCategoria());
			st.setString(2, categoria.getDescricaoCategoria());
			st.setString(3, categoria.getDataCriacaoCategoria());
			st.setString(4, categoria.getIdCategoria());
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar(conn);
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
			BancoDados.desconectar(conn);
		}
	}
}
