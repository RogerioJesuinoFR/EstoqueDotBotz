package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import entities.Pedido;
import entities.PedidoItem;

public class PedidoDAO {
	private Connection conn;
	
	public PedidoDAO(Connection conn) {
		this.conn = conn;
	}
	
	// Cadastra o pedido principal (tabela 'pedidos') e retorna o ID gerado
	public int cadastrarPedidoPrincipal(Pedido pedido) throws SQLException {
		PreparedStatement st = null;
        ResultSet rs = null;
		String sql = "INSERT INTO pedidos (id_usuario, data_solicitacao, data_previsao_entrega, status) VALUES (?, ?, ?, ?)";
		
		try {
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, pedido.getIdUsuario());
            st.setString(2, pedido.getDataSolicitacaoPedido());
            
            if (pedido.getDataPrevisaoPedido() != null && !pedido.getDataPrevisaoPedido().trim().isEmpty()) {
                st.setString(3, pedido.getDataPrevisaoPedido());
            } else {
                st.setNull(3, Types.DATE);
            }
            st.setString(4, pedido.getStatusPedido());
			
			int linhasAfetadas = st.executeUpdate();
            
            if (linhasAfetadas > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Retorna o ID gerado
                }
            }
            return 0;
		} finally {
			BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
		}
	}
    
    // Cadastra os itens associados a um ID de pedido (tabela 'pedido_itens')
    public void cadastrarItensDoPedido(int idPedido, List<PedidoItem> itens) throws SQLException {
        PreparedStatement st = null;
        String sql = "INSERT INTO pedido_itens (id_pedido, id_item, quantidade_comprada) VALUES (?, ?, ?)";
        try {
            st = conn.prepareStatement(sql);
            for (PedidoItem item : itens) {
                st.setInt(1, idPedido);
                st.setString(2, item.getIdItem());
                st.setInt(3, item.getQuantidadeComprada());
                st.addBatch();
            }
            st.executeBatch();
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }
	
	// Busca todos os pedidos para a listagem principal
	public List<Pedido> buscarTodos() throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
        String sql = "SELECT p.id_pedido, p.data_solicitacao, p.status, u.nome " +
                     "FROM pedidos p JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "ORDER BY p.data_solicitacao DESC";
		try {
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			List<Pedido> listaPedidos = new ArrayList<>();
			while (rs.next()) {
				Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getString("id_pedido"));
                pedido.setDataSolicitacaoPedido(rs.getString("data_solicitacao"));
                pedido.setStatusPedido(rs.getString("status"));
                pedido.setNomeUsuario(rs.getString("nome"));
				listaPedidos.add(pedido);
			}
			return listaPedidos;
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
		}
	}
    
    // Atualiza apenas o status de um pedido
    public int atualizarStatus(String idPedido, String novoStatus) throws SQLException {
        PreparedStatement st = null;
        String sql = "UPDATE pedidos SET status = ? WHERE id_pedido = ?";
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, novoStatus);
            st.setString(2, idPedido);
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }
	
    // NOVO: Busca um pedido completo pelo ID (para Edição)
	public Pedido buscarPorID(String idPedido) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
        // 1. Busca o pedido principal
        String sqlPedido = "SELECT * FROM pedidos WHERE id_pedido = ?";
        
		try {
            st = conn.prepareStatement(sqlPedido);
            st.setString(1, idPedido);
            rs = st.executeQuery();
            
            Pedido pedido = null;
            if (rs.next()) {
                pedido = new Pedido(
                    rs.getString("id_pedido"),
                    rs.getString("id_usuario"),
                    rs.getString("data_solicitacao"),
                    rs.getString("data_previsao_entrega"),
                    rs.getString("status"),
                    rs.getString("data_criacao")
                );
            } else {
                return null; // Pedido não encontrado
            }
            
            // 2. Busca os itens associados (JOIN com a tabela 'itens' para pegar o nome)
            BancoDados.finalizarStatement(st);
            BancoDados.finalizarResultSet(rs);
            
            String sqlItens = "SELECT pi.id_item, i.nome, pi.quantidade_comprada " +
                              "FROM pedido_itens pi JOIN itens i ON pi.id_item = i.id_item " +
                              "WHERE pi.id_pedido = ?";
            st = conn.prepareStatement(sqlItens);
            st.setString(1, idPedido);
            rs = st.executeQuery();
            
            while (rs.next()) {
                PedidoItem item = new PedidoItem(
                    rs.getString("id_item"),
                    rs.getString("nome"),
                    rs.getInt("quantidade_comprada")
                );
                pedido.adicionarItem(item);
            }
            return pedido;
            
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
		}
	}
	
    // NOVO: Atualiza o pedido principal (sem os itens)
	public int atualizarPedidoPrincipal(Pedido pedido) throws SQLException {
		PreparedStatement st = null;
        String sql = "UPDATE pedidos SET data_solicitacao = ?, data_previsao_entrega = ?, status = ? " +
                     "WHERE id_pedido = ?";
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, pedido.getDataSolicitacaoPedido());
            if (pedido.getDataPrevisaoPedido() != null && !pedido.getDataPrevisaoPedido().trim().isEmpty()) {
                st.setString(2, pedido.getDataPrevisaoPedido());
            } else {
                st.setNull(2, Types.DATE);
            }
            st.setString(3, pedido.getStatusPedido());
            st.setString(4, pedido.getIdPedido());
            
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
        }
	}

    // NOVO: Exclui os itens antigos antes de atualizar
    public int excluirItensDoPedido(String idPedido) throws SQLException {
        PreparedStatement st = null;
        String sql = "DELETE FROM pedido_itens WHERE id_pedido = ?";
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, idPedido);
            return st.executeUpdate();
        } finally {
            BancoDados.finalizarStatement(st);
        }
    }
	
	public int excluir(String idPedido) throws SQLException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM pedidos WHERE id_pedido = ?");
			st.setString(1, idPedido);
			return st.executeUpdate();
		} finally {
			BancoDados.finalizarStatement(st);
		}
	}
}