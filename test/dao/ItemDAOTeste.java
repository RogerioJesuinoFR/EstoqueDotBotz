package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import entities.Categoria;
import entities.Item;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ItemDAOTeste {

	@Test
	void cadastrarItemTeste() throws SQLException, IOException {
		
		Item item = new Item();
		item.setNomeItem("Parafuso");
		item.setIdCategoria("1");
		item.setDescricaoItem("Parafusos de aço");
		item.setQuantidadeAtualItem(0);
		item.setQuantidadeMinimaItem(10);
		item.setUnidadeMedidaItem("Unidades");
		item.setValidadeItem("22/12/2060");
		item.setSetorItem("Autonomos");
		item.setDataCriacaoItem("22-10-2025");
		
		Connection conn = null;
		
		try {
			
			conn = BancoDados.conectar();
			int resultado = new ItemDAO(conn).cadastrar(item);
			
			assertEquals(1, resultado);
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
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
	
	@Test
	void buscarPorNomeItemTeste() throws SQLException, IOException {
		
		String nome = "Parafusos";
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			Item item = new ItemDAO(conn).buscarPorNome(nome);
			
			assertNotNull(item);
			assertEquals("Parafusos", item.getNomeItem());
			
		} finally {
			
			BancoDados.desconectar(conn);
		}
	}
	
	@Test
	void atualizarItemTeste() throws SQLException, IOException {
		
		Item item = new Item ();
		item.setIdItem("1");
		item.setNomeItem("Parafusos de 3/4");
		item.setIdCategoria("1");
		item.setDescricaoItem("Parafusos de tamanho 3/4");
		item.setQuantidadeAtualItem(10);
		item.setQuantidadeMinimaItem(10);
		item.setUnidadeMedidaItem("Unidades");
		item.setValidadeItem("25-10-2060");
		item.setSetorItem("Combate");
		item.setDataCriacaoItem("2-10-2025");
		
		Connection conn = null;
		try {
			
			conn = BancoDados.conectar();
			int resultado = new ItemDAO(conn).atualizar(item);
			
			assertEquals(1, resultado);
			
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
		} finally {
			
			BancoDados.desconectar(conn);
		}
		
	}
}
