package dao;

public class ItemDAO {

	private Connection conn;
	
	public ItemDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public String cadastrar(Item item) throws SQLException {
		
		return null;
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
