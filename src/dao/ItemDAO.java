package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Item;

public class ItemDAO {

	private Connection conn;
	
	public ItemDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public int cadastrar(Item item) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("insert into itens (id_item, nome, id_categoria, descricao, quantidade_atual, quantidade_minima, unidade_medida, validade, setor, data_criacao) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			st.setString(1, item.getIdItem());
			st.setString(2, item.getNomeItem());
			st.setObject(3, item.getCategoria());
			st.setString(4, item.getDescricaoItem());
			st.setInt(5, item.getQuantidadeAtualItem());
			st.setInt(6, item.getQuantidadeMinimaItem());
			st.setString(7, item.getUnidadeMedidaItem());
			st.setString(8, item.getValidadeItem());
			st.setString(9, item.getSetorItem());
			st.setString(10, item.getDataCriacaoItem());
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public List<Item> buscarTodos() throws SQLException {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select * from itens order by nome");
			
			rs = st.executeQuery();
			
			List<Item> listaItens = new ArrayList<>();
			
			while (rs.next()) {
				
				Item item = new Item();
				
				item.setNomeItem(rs.getString("nome"));
				item.setCategoria(rs.getObject("id_categoria"));
				item.setDescricaoItem(rs.getString("descricao"));
				item.setQuantidadeAtualItem(rs.getInt("quantidade_atual"));
				item.setQuantidadeMinimaItem(rs.getInt("quantidade_minima"));
				item.setUnidadeMedidaItem(rs.getString("unidade_medida"));
				item.setValidadeItem(rs.getString("validade"));
				item.setSetorItem(rs.getString("setor"));
				item.setDataCriacaoItem(rs.getString("data_criacao"));
				
				listaItens.add(item);
			}
			
			return listaItens;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	
	public Item buscarPorNome(String nome) throws SQLException {
		
		PrepareStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("select * from itens where nome = ?");
			
			st.setString(1, nome);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				
				Item item = new Item();
				
				item.setNomeItem(rs.getString("nome"));
				item.setCategoria(rs.getObject("id_categoria"));
				item.setDescricaoItem(rs.getString("descricao"));
				item.setQuantidadeAtualItem(rs.getInt("quantidade_atual"));
				item.setQuantidadeMinimaItem(rs.getInt("quantidade_minima"));
				item.setUnidadeMedidaItem(rs.getString("unidade_medida"));
				item.setValidadeItem(rs.getString("validade"));
				item.setSetorItem(rs.getString("setor"));
				item.setDataCriacaoItem(rs.getString("data_Criacao"));
				
				return item;
			}
			
			return null;
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	
	public int atualizar(Item item) throws SQLException {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("update itens set nome = ?, id_categoria = ?, descricao = ?, quantidade_atual = ?, quantidade_minima = ?, unidade_medida = ?, validade = ?, setor = ?, data_criacao = ? where id_item = ?");
						
			st.setString(1, item.getNomeItem());
			st.setObject(2, item.getCategoria());
			st.setString(3, item.getDescricaoItem());
			st.setInt(4, item.getQuantidadeAtualItem());
			st.setInt(5, item.getQuantidadeMinimaItem());
			st.setString(6, item.getUnidadeMedidaItem());
			st.setString(7, item.getValidadeItem());
			st.setString(8, item.getSetorItem());
			st.setString(9, item.getDataCriacaoItem());
			st.setString(10, item.getIdItem());
			
			return st.executeUpdate();
			
		} finally {
			
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public String excluir(String nome) throws SQLException {
		
		return 0;
	}
}
