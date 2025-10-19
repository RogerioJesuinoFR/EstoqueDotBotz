package dao;

import service.Usuario;

public class UsuarioDAO {

	private Connection conn;
	
	public UsuarioDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public String cadastrar(Usuario usuario) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("insert into usuarios (nomeUsuario, RAUsuario, setorUsuario, senhaUsuario, dataCriacaoUsuario) values (?, ?, ?, ?, ?)");
			
			st.setString(1, usuario.getNomeUsuario());
			st.setString(2, usuario.getRAUsuario());
			st.setString(3, usuario.getSetorUsuario());
			st.setString(4, usuario.getSenhaUsuario());
			st.setString(5, usuario.getDataCriacaoUsuario());
			
			return st.executeUpdate();
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
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
