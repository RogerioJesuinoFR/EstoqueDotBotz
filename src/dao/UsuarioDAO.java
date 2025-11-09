package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import entities.Usuario;

public class UsuarioDAO {

	private Connection conn;
	
	public UsuarioDAO(Connection conn) {
		this.conn = conn;
	}
	
	// Método de CADASTRO CORRIGIDO (Omite ID e Data, usa SHA2)
	public int cadastrar(Usuario usuario) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			// Omitimos id_usuario e data_criacao (AUTO_INCREMENT/DEFAULT)
			String sql = "INSERT INTO usuarios (nome, ra, setor, senha) VALUES (?, ?, ?, SHA2(?, 256))";
			
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, usuario.getNomeUsuario());
			st.setString(2, usuario.getRAUsuario());
			st.setString(3, usuario.getSetorUsuario());
			st.setString(4, usuario.getSenhaUsuario());
			
			int linhasAfetadas = st.executeUpdate();			
			return linhasAfetadas;
			
		} finally {
			BancoDados.finalizarStatement(st);
			// A conexão é fechada pelo UsuarioService
		}
	}
	
    // **MÉTODO DE LOGIN ADICIONADO**
	public Usuario login(String ra, String senha) throws SQLException {
	    String sql = "SELECT id_usuario, nome, ra, setor, data_criacao FROM usuarios WHERE ra = ? AND senha = SHA2(?, 256)";
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    Usuario usuario = null;
	    
	    try {
	        st = conn.prepareStatement(sql);
	        
	        st.setString(1, ra);
	        st.setString(2, senha);
	        
	        rs = st.executeQuery();
	        
	        if (rs.next()) {
	            usuario = new Usuario();
	            usuario.setIdUsuario(rs.getString("id_usuario"));
	            usuario.setNomeUsuario(rs.getString("nome"));
	            usuario.setRAUsuario(rs.getString("ra"));
	            usuario.setSetorUsuario(rs.getString("setor"));
	            usuario.setDataCriacaoUsuario(rs.getString("data_criacao"));
                usuario.setSenhaUsuario(""); // Não retorna a senha
	        }
	        return usuario;
	        
	    } finally {
	        BancoDados.finalizarStatement(st);
	        BancoDados.finalizarResultSet(rs);
	        // A conexão é fechada pelo UsuarioService
	    }
	}
	
	// Métodos que podem ser chamados sozinhos (mantêm o fechar conexão)
	
	public List<Usuario> BuscarTodos() throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select * from usuarios order by nome");
			rs = st.executeQuery();
			List<Usuario> listaUsuarios = new ArrayList<>();
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(rs.getString("id_usuario"));
				usuario.setNomeUsuario(rs.getString("nome"));
				usuario.setRAUsuario(rs.getString("ra"));
				usuario.setSetorUsuario(rs.getString("setor"));
				usuario.setSenhaUsuario(rs.getString("senha"));
				usuario.setDataCriacaoUsuario(rs.getString("data_criacao"));
				listaUsuarios.add(usuario);
			}
			return listaUsuarios;
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar(conn); // Fechar aqui
		}
	}
	
	public Usuario buscarPorRA(String ra) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select * from usuarios where ra = ?");
			st.setString(1, ra);
			rs = st.executeQuery();
			if (rs.next()) {
				Usuario usuario = new Usuario();
                // ... (preencher usuário)
				return usuario;
			}
			return null;
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar(conn); // Fechar aqui
		}
	}
	
	public int atualizar(Usuario usuario) throws SQLException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update usuarios set nome = ?, ra = ?, setor = ?, data_criacao = ? where id_usuario = ?");
			// ... (setar strings)
			return st.executeUpdate();
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar(conn); // Fechar aqui
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
			BancoDados.desconectar(conn); // Fechar aqui
		}
	}
}