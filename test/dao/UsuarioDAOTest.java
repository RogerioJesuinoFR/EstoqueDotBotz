package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import entities.Usuario;

public class UsuarioDAOTest {

  @Test
    void cadastrarUsuarioTeste() throws SQLException, IOException {
        
        Usuario usuario = new Usuario(); 
        usuario.setNomeUsuario("Maria dos Santos");
        usuario.setRAUsuario("654321");
        usuario.setSetorUsuario("Combate");
        usuario.setSenhaUsuario("@MinhaSenha123");
        usuario.setDataCriacaoUsuario("2025-10-19"); 
        
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            int resultado = new UsuarioDAO(conn).cadastrar(usuario); 
            
            assertEquals(1, resultado);
            
            System.out.println("\nUsuário cadastrado com sucesso!\n");
            
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
        	
        	System.out.println("\n--- LISTA DE USUÁRIOS ---");
        	
        	for (Usuario usuario : listaUsuarios) {
        		System.out.println("ID: " + usuario.getIdUsuario() +
        				", Nome: " + usuario.getNomeUsuario() +
        				", RA: " + usuario.getRAUsuario() +
        				", Setor: " + usuario.getSetorUsuario());
        	}
        	
        	System.out.println("---------------------------------------------------\n");
        	
    	} finally {
    		
    		BancoDados.desconectar(conn);
    	}
    }
 
    @Test
	void buscarPorRAUsuarioTeste() throws SQLException, IOException {
		
		String ra = "654321";
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			Usuario usuario = new UsuarioDAO(conn).buscarPorRA(ra);
			
			assertNotNull(usuario);
			assertEquals("654321", usuario.getRAUsuario());
			
			System.out.println("\nUsuário:\nID: " + usuario.getIdUsuario() +
        				", Nome: " + usuario.getNomeUsuario() +
        				", RA: " + usuario.getRAUsuario() +
        				", Setor: " + usuario.getSetorUsuario());
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
  
    @Test
	void atualizarUsuarioTeste() throws SQLException, IOException {
		
		Usuario usuario = new Usuario ();
		usuario.setNomeUsuario("José da Silva");
		usuario.setRAUsuario("123456");
		usuario.setSenhaUsuario("!123Senha");
		usuario.setSetorUsuario("Combate");
		usuario.setDataCriacaoUsuario("2025-10-19");
		usuario.setIdUsuario("1");
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			int resultado = new UsuarioDAO(conn).atualizar(usuario);
			
			assertEquals(1, resultado);
			
			System.out.println("\nUsuário atualizado com sucesso!\n");
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}

    @Test
	void excluirUsuarioTeste() throws SQLException, IOException {
		
		String id_usuario = "2";
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			int resultado = new UsuarioDAO(conn).excluir(id_usuario);
			
			assertEquals(1, resultado);
			
			System.out.println("\nUsuário excluído com sucesso!\n");
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
}