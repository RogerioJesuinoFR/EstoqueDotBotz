package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List; 

import entities.Usuario;

public class UsuarioDAO {

	private Connection conn;
	
	public UsuarioDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public int cadastrar(Usuario usuario) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("insert into usuarios (nomeUsuario, RAUsuario, setorUsuario, senhaUsuario, dataCriacaoUsuario) values (?, ?, ?, ?, ?)");
			
			st.setString(1, usuario.getNomeUsuario());
			st.setString(2, usuario.getRAUsuario());
			st.setString(3, usuario.getSetorUsuario());
			st.setString(4, usuario.getSenhaUsuario());
			st.setString(5, usuario.getDataCriacaoUsuario());
			
			int linhasAfetadas = st.executeUpdate();			
			return linhasAfetadas;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
		}
	}
	
	public List<Usuario> BuscarTodos() throws SQLException {
		
		return null;
	}
	
	public Usuario buscarPorRA(int ra) throws SQLException {
		
		return null;
	}
	
	public String atualizar(Usuario usuario) throws SQLException {
		
		return null;
	}
	
	public String excluir(int ra) throws SQLException {
		
		return null;
	}
}
