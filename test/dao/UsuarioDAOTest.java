package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import entities.Usuario;

public class UsuarioDAOTest {

    @Test
    void cadastrarUsuarioTeste() throws SQLException, IOException {
        
        Usuario usuario = new Usuario(); 
        usuario.setNomeUsuario("João da Silva");
        usuario.setRAUsuario("123456");
        usuario.setSetorUsuario("Autonomos");
        usuario.setSenhaUsuario("!123Senha");
        usuario.setDataCriacaoUsuario("2025-10-19"); 
        
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            int resultado = new UsuarioDAO(conn).cadastrar(usuario); 
            
            assertEquals(1, resultado);
            
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    @Test
    void buscarTodosUsuariosTeste() throws SQLException, IOException {
    	
    	Connection conn = null;
    	
    	try {
    		
    		conn = BancoDados.conectar();
    		List<Usuario> listaUsuarios = new UsuarioDAO(conn).BuscarTodos();
        	
        	assertNotNull(listaUsuarios);
    	} finally {
    		
    		BancoDados.desconectar(conn);
    	}
    }
    
    @Test
	void buscarPorRAUsuarioTeste() throws SQLException, IOException {
		
		String ra = "123456";
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			Usuario usuario = new UsuarioDAO(conn).buscarPorRA(ra);
			
			assertNotNull(usuario);
			assertEquals("123456", usuario.getRAUsuario());
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
    
    @Test
	void atualizarUsuarioTeste() throws SQLException, IOException {
		
		Usuario usuario = new Usuario ();
		usuario.setIdUsuario("1");
		usuario.setNomeUsuario("Maria Aparecida");
		usuario.setRAUsuario("654321");
		usuario.setSenhaUsuario("@MinhaSenha123");
		usuario.setSetorUsuario("Combate");
		usuario.setDataCriacaoUsuario("23-10-2025");
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			int resultado = new UsuarioDAO(conn).atualizar(usuario);
			
			assertEquals(1, resultado);
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
    
    @Test
	void excluirUsuarioTeste() throws SQLException, IOException {
		
		String id_usuario = "1";
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			int resultado = new UsuarioDAO(conn).excluir(id_usuario);
			
			assertEquals(1, resultado);
		} finally {
			
			BancoDados.desconectar(conn);
		}
		
	}
}