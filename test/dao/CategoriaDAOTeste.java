package dao;

import entities.Categoria;

public class CategoriaDAOTeste {

	@Test
	void cadastrarCategoriaTeste() throws SQLException, IOException {
		
		Categoria categoria = new Categoria();
		categoria.setNomeCategoria("Itens pequenos");
		categoria.setDescricaoCategoria("Setor de autonomos da DotBotz");
		categoria.setDataCriacaoCategoria("22/10/2025");
		
		Connection conn = BancoDados.conectar();
		int resultado = new CategoriaDAO(conn).cadastrar(categoria);
		
		assertEquals(1, resultado);
	}
}
