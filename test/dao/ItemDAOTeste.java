package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import entities.Categoria;
import entities.Item;
import entities.Usuario;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ItemDAOTeste {

	@Test
	void cadastrarItemTeste() throws SQLException, IOException {
		
		Item item = new Item();
		item.setNomeItem("Parafuso");
		item.setCategoria("1");
		item.setDescricaoItem("Parafusos de aço");
		item.setQuantidadeAtualItem(0);
		item.setQuantidadeMinimaItem(10);
		item.setUnidadeMedidaItem("Unidades");
		item.setValidadeItem("2060-10-22");
		item.setSetorItem("Autonomos");
		item.setDataCriacaoItem("2025-10-22");
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			int resultado = new ItemDAO(conn).cadastrar(item);
			
			assertEquals(1, resultado);
			
			System.out.println("\nItem cadastrado com sucesso!\n");
			
		} finally {
			
			BancoDados.desconectar(conn);
		}		
	}

	@Test
	void buscarTodosItensTeste() throws SQLException, IOException {
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			List<Item> listaItens = new ItemDAO(conn).buscarTodos();
			
			assertNotNull(listaItens);
			
			System.out.println("\n--- LISTA DE ITENS ---");
        	
        	for (Item item : listaItens) {
        		System.out.println("ID: " + item.getIdItem() +
        				", Nome: " + item.getNomeItem() +
        				", Categoria: " + item.getCategoria() +
        				", Descrição: " + item.getDescricaoItem() +
        				", Quantidade atual: " + item.getQuantidadeAtualItem() +
        				", Quantidade mínima: " + item.getQuantidadeMinimaItem() +
        				", Unidade de medida: " + item.getUnidadeMedidaItem() +
        				", Validade: " + item.getValidadeItem() +
        				", Setor: " + item.getSetorItem() +
        				", Data de criação: " + item.getDataCriacaoItem());
        	}
        	
        	System.out.println("---------------------------------------------------\n");
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
	
	@Test
	void buscarPorNomeItemTeste() throws SQLException, IOException {
		
		String nome = "Parafuso";
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			Item item = new ItemDAO(conn).buscarPorNome(nome);
			
			assertNotNull(item);
			assertEquals("Parafuso", item.getNomeItem());
			
			System.out.println("\nItem encontrado:\nID: " + item.getIdItem() +
    				", Nome: " + item.getNomeItem() +
    				", Categoria: " + item.getCategoria() +
    				", Descrição: " + item.getDescricaoItem() +
    				", Quantidade atual: " + item.getQuantidadeAtualItem() +
    				", Quantidade mínima: " + item.getQuantidadeMinimaItem() +
    				", Unidade de medida: " + item.getUnidadeMedidaItem() +
    				", Validade: " + item.getValidadeItem() +
    				", Setor: " + item.getSetorItem() +
    				", Data de criação: " + item.getDataCriacaoItem());
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
	
	@Test
	void atualizarItemTeste() throws SQLException, IOException {
		
		Item item = new Item ();
		item.setIdItem("1");
		item.setNomeItem("Parafusos de 3/4");
		item.setCategoria("1");
		item.setDescricaoItem("Parafusos de tamanho 3/4");
		item.setQuantidadeAtualItem(10);
		item.setQuantidadeMinimaItem(10);
		item.setUnidadeMedidaItem("Unidades");
		item.setValidadeItem("2060-10-25");
		item.setSetorItem("Combate");
		item.setDataCriacaoItem("2025-10-25");
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new ItemDAO(conn).atualizar(item);
			
			assertEquals(1, resultado);
			
			System.out.println("\nItem atualizado com sucesso!\n");
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
		
	}
	
	@Test
	void excluirItemTeste() throws SQLException, IOException {
		
		String id_item = "1";
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new ItemDAO(conn).excluir(id_item);
			
			assertEquals(1, resultado);
			
			System.out.println("\nItem excluído com sucesso!\n");
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
		
	}
}
