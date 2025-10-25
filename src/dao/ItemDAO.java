package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import entities.Item;

public class ItemDAO {

	private Connection conn;
	
	public ItemDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public int cadastrar(Item item) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("insert into itens (nome, id_categoria, descricao, quantidade_atual, quantidade_minima, unidade_medida, validade, setor, data_criacao) values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			st.setString(1, item.getNomeItem());
			st.setObject(2, item.getCategoria());
			st.setString(3, item.getDescricaoItem());
			st.setInt(4, item.getQuantidadeAtualItem());
			st.setInt(5, item.getQuantidadeMinimaItem());
			st.setString(6, item.getUnidadeMedidaItem());
			st.setDate(7, item.getValidadeItem());
			st.setString(8, item.getSetorItem());
			st.setDate(9, item.getDataCriacaoItem());
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public List<Item> buscarTodos() throws SQLException {
		
		return null;
	}
	
	public Item buscarPorNome(String nome) throws SQLException {
		
		return null;
	}
	
	public String atualizar(Item item) throws SQLException {
		
		return null;
	}
	
	public String excluir(String nome) throws SQLException {
		
		return 0;
	}
}
