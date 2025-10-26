package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import entities.Categoria;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CategoriaDAOTeste {

	@Test void cadastrarCategoriaTeste() throws SQLException, IOException {
		
		Categoria categoria = new Categoria();
		categoria.setNomeCategoria("Itens pequenos");
		categoria.setDescricaoCategoria("Setor de autonomos da DotBotz");
		categoria.setDataCriacaoCategoria("22-10-2025");
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new CategoriaDAO(conn).cadastrar(categoria);
			
			assertEquals(1, resultado);
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
	
	@Test void buscarTodasCategoriasTeste() throws SQLException, IOException {
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			List<Categoria> listaCategorias = new CategoriaDAO(conn).buscarTodos();
			
			assertNotNull(listaCategorias);
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
	
	@Test void buscarPorNomeCategoriaTeste() throws SQLException, IOException {
		
		String nome = "Itens pequenos";
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			Categoria categoria = new CategoriaDAO(conn).buscarPorNome(nome);
			
			assertNotNull(categoria);
			assertEquals("Itens pequenos", categoria.getNomeCategoria());
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
	
	@Test void atualizarCategoriaTeste() throws SQLException, IOException {
		
		Categoria categoria = new Categoria ();
		categoria.setIdCategoria("1");
		categoria.setNomeCategoria("Itens médios");
		categoria.setDescricaoCategoria("Itens de tamanho médio");
		categoria.setDataCriacaoCategoria("23-10-2025");
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new CategoriaDAO(conn).atualizar(categoria);
			
			assertEquals(1, resultado);
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
	
	@Test void excluirCategoriaTeste() throws SQLException, IOException {
		
		String id_categoria = "1";
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new CategoriaDAO(conn).excluir(id_categoria);
			
			assertEquals(1, resultado);
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
}
