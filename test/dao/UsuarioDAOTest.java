package dao;

import service.Usuario;

public class UsuarioDAOTest {

	@Test
	
	void cadastrarCursoTeste() throws SQLException, IOException {
		
		Usuario usuario = new Usuario();
		usuario.setNomeUsuario("João da Silva");
		usuario.setRAUsuario("123456");
		usuario.setSetorUsuario("Autonomos");
		usuario.setSenhaUsuario("!123Senha");
		usuario.setDataCriacaoUsuario("19/10/2025");
		
		Connection conn = BancoDados.conectar();
		int resultado = new UsuarioDAO(conn).cadastrar(usuario);
		
		assertEquals(1, resultado);
	}
}
