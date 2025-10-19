package dao;

public class PedidoDAO {
	private Connection conn;
	
	public PedidoDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public String cadastrar(Pedido pedido) throws SQLException {
		
		return null;
	}
	
	public List<Pedido> buscarTodos() throws SQLException {
		
		return null;
	}
	
	public Pedido buscarPorID(String idPedido) throws SQLException {
		
		return null;
	}
	
	public String atualizar(Pedido pedido) throws SQLException {
		
		return null;
	}
	
	public String excluir(String idPedido) throws SQLException {
		
		return 0;
	}
}
