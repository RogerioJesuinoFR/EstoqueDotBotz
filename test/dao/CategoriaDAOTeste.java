package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import entities.Categoria;

public class CategoriaDAOTeste {

	@Test
	void cadastrarCategoriaTeste() throws SQLException, IOException {
		
		Categoria categoria = new Categoria();
		categoria.setNomeCategoria("Itens pequenos");
		categoria.setDescricaoCategoria("Setor de autonomos da DotBotz");
		categoria.setDataCriacaoCategoria("22-10-2025");
		
		Connection conn = BancoDados.conectar();
		int resultado = new CategoriaDAO(conn).cadastrar(categoria);
		
		assertEquals(1, resultado);
	}
	
	@Test
	void buscarTodasCategoriasTeste() throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		List<Categoria> listaCategorias = new CategoriaDAO(conn).buscarTodos();
		
		assertNotNull(listaCategorias);
	}
	
	@Test
	void buscarPorNomeCategoriaTeste() throws SQLException, IOException {
		
		String nome = "Itens pequenos";
		
		Connection conn = BancoDados.conectar();
		Categoria categoria = new CategoriaDAO(conn).buscarPorNome(nome);
		
		assertNotNull(categoria);
		asserEquals("Itens pequenos", categoria.getNomeCategoria());
	}
	
	@Test
	void atualizarCategoriaTeste() throws SQLException, IOException {
		
		Categoria categoria = new Categoria ();
		categoria.setNomeCategoria("Itens médios");
		categoria.setDescricaoCategoria("Itens de tamanho médio");
		categoria.setDataCriacaoCategoria("23-10-2025");
		
		Connection conn = BancoDados.conectar();
		int resultado = new CategoriaDAO(conn).atualizar(categoria);
		
		assertEquals(1, resultado);
	}
	
	@Test
	void excluirCategoriaTeste() throws SQLException, IOException {
		
		String id_categoria = "1";
		
		Connection conn = BancoDados.conectar();
		int resultado = new CategoriaDAO(conn).excluir(id_categoria);
		
		assertEquals(1, resultado);
	}
}
