package dao;

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
		item.setDataCriacaoItem("22/10/2025");
		
		Connection conn = BancoDados.conectar();
		int resultado = new ItemDAO(conn).cadastrar(item);
		
		assertEquals(1, resultado);
	}
}
