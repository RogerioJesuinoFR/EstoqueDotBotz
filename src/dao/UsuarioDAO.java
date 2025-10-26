package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Item;
import entities.Usuario;

public class UsuarioDAO {

	private Connection conn;
	
	public UsuarioDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public int cadastrar(Usuario usuario) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("insert into usuarios (id_usuario, nome, ra, setor, senha, data_criacao) values (?, ?, ?, ?, ?, ?)");
			
			st.setString(1, usuario.getIdUsuario());
			st.setString(2, usuario.getNomeUsuario());
			st.setString(3, usuario.getRAUsuario());
			st.setString(4, usuario.getSetorUsuario());
			st.setString(5, usuario.getSenhaUsuario());
			st.setString(6, usuario.getDataCriacaoUsuario());
			
			int linhasAfetadas = st.executeUpdate();			
			return linhasAfetadas;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
		}
	}
	
	public List<Usuario> BuscarTodos() throws SQLException {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select * from usuarios order by nomeUsuario");
			
			rs = st.executeQuery();
			
			List<Usuario> listaUsuarios = new ArrayList<>();
			
			while (rs.next()) {
				
				Usuario usuario = new Usuario();
				
				usuario.setNomeUsuario(rs.getString("nomeUsuario"));
				usuario.setRAUsuario(rs.getString("RAUsuario"));
				usuario.setSetorUsuario(rs.getString("setorUsuario"));
				usuario.setSenhaUsuario(rs.getString("senhaUsuario"));
				usuario.setDataCriacaoUsuario(rs.getString("dataCriacaoUsuario"));
				
				listaUsuarios.add(usuario);
			}
			
			return listaUsuarios;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	
	public Usuario buscarPorRA(String ra) throws SQLException {
		
		PrepareStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select * from usuarios where RAUsuario = ?");
			
			st.setString(1, ra);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				
				Usuario usuario = new Usuario();
				
				usuario.setNomeUsuario(rs.getString("nomeUsuario"));
				usuario.setRAUsuario(rs.getString("RAUsuario"));
				usuario.setSetorUsuario(rs.getString("setorUsuario"));
				usuario.setSenhaUsuario(rs.getString("senhaUsuario"));
				usuario.setDataCriacaoUsuario(rs.getString("dataCriacaoUsuario"));
				
				return usuario;
			}
			
			return null;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	
	public String atualizar(Usuario usuario) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("update usuarios set nome = ?, ra = ?, setor = ?, data_criacao = ? where id_usuario = ?");
			
			st.setString(1, usuario.getNomeUsuario());
			st.setString(2, usuario.getRAUsuario());
			st.setString(3, usuario.getSetorUsuario());
			st.setString(4, usuario.getDataCriacaoUsuario());
			st.setString(5, usuario.getIdUsuario());
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public int excluir(String id) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("delete from usuarios where id_usuario = ?");
			
			st.setString(1, id);
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
}
