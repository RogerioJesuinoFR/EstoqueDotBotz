package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import entities.Categoria;
import entities.Item;

public class ItemDAOTeste {

	@Test
	void cadastrarItemTeste() throws SQLException, IOException {
		
		Item item = new Item();
		item.setNomeItem("Parafuso");
		item.setCategoria("Itens pequenos");
		item.setDescricaoItem("Parafusos de aço");
		item.setQuantidadeAtualItem(0);
		item.setQuantidadeMinimaItem(10);
		item.setUnidadeMedidaItem("Unidades");
		item.setValidadeItem("22/12/2060");
		item.setSetorItem("Autonomos");
		item.setDataCriacaoItem("22-10-2025");
		
		Connection conn = BancoDados.conectar();
		int resultado = new ItemDAO(conn).cadastrar(item);
		
		assertEquals(1, resultado);
	}
	
	@Test
	void buscarTodosItensTeste() throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		List<Item> listaItens = new ItemDAO(conn).buscarTodos();
		
		assertNotNull(listaItens);
	}
	
	@Test
	void buscarPorNomeItemTeste() throws SQLException, IOException {
		
		String nome = "Parafusos";
		
		Connection conn = BancoDados.conectar();
		Item item = new ItemDAO(conn).buscarPorNome(nome);
		
		assertNotNull(item);
		assertEquals("Parafusos", item.getNomeItem());
	}
	
	@Test
	void atualizarItemTeste() throws SQLException, IOException {
		
		Item item = new Item ();
		item.setNomeItem("");
		item.setCategoria("Itens médios");
		item.setDescricaoItem("");
		item.setQuantidadeAtualItem(10);
		item.setQuantidadeMinimaItem(10);
		item.setUnidadeMedidaItem("Unidades");
		item.setValidadeItem("25-10-2060");
		item.setSetorItem("Combate");
		item.setDataCriacaoItem("2-10-2025");
		
		Connection conn = BancoDados.conectar();
		int resultado = new ItemDAO(conn).atualizar(item);
		
		assertEquals(1, resultado);
	}
	
	@Test
	void excluirItemTeste() throws SQLException, IOException {
		
		String id_item = "1";
		
		Connection conn = BancoDados.conectar();
		int resultado = new ItemDAO(conn).excluir(id_item);
		
		assertEquals(1, resultado);
	}
}
