package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import entities.Categoria;
import entities.Usuario;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CategoriaDAOTeste {

	@Test void cadastrarCategoriaTeste() throws SQLException, IOException {
		
		Categoria categoria = new Categoria();
		categoria.setNomeCategoria("Itens médios");
		categoria.setDescricaoCategoria("Setor de autonomos da DotBotz");
		categoria.setDataCriacaoCategoria("2025-10-22");
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new CategoriaDAO(conn).cadastrar(categoria);
			
			assertEquals(1, resultado);
			
			System.out.println("\nCategoria cadastrada com sucesso!\n");
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
			
			System.out.println("\n--- LISTA DE CATEGORIAS ---");
        	
        	for (Categoria categoria : listaCategorias) {
        		System.out.println("ID: " + categoria.getIdCategoria() +
        				", Nome: " + categoria.getNomeCategoria() +
        				", Descrição: " + categoria.getDescricaoCategoria() +
        				", Data de criação: " + categoria.getDataCriacaoCategoria());
        	}
        	
        	System.out.println("---------------------------------------------------\n");
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
			
			System.out.println("\nCategoria:\nID: " + categoria.getIdCategoria() +
    				", Nome: " + categoria.getNomeCategoria() +
    				", Descricao: " + categoria.getDescricaoCategoria() +
    				", Data de criação: " + categoria.getDataCriacaoCategoria());
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}

	@Test void atualizarCategoriaTeste() throws SQLException, IOException {
		
		Categoria categoria = new Categoria ();
		categoria.setIdCategoria("1");
		categoria.setNomeCategoria("Itens médios");
		categoria.setDescricaoCategoria("Itens de tamanho médio");
		categoria.setDataCriacaoCategoria("2025-10-23");
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new CategoriaDAO(conn).atualizar(categoria);
			
			assertEquals(1, resultado);
			
			System.out.println("\nCategoria atualizada com sucesso!\n");
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}

	@Test void excluirCategoriaTeste() throws SQLException, IOException {
		
		String id_categoria = "3";
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new CategoriaDAO(conn).excluir(id_categoria);
			
			assertEquals(1, resultado);
			
			System.out.println("\nCategoria excluída com sucesso!\n");
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
}
