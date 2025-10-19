package dao;

public class CategoriaDAO {
	
	private Connection conn;
	
	public CategoriaDAO(Connection conn) {
		
		this.conn = conn;
	}
	
	public String cadastrar(Categoria categoria) throws SQLException {
		
		return null;
	}
	
	public List<Categoria> buscarTodos() throws SQLException {
		
		return null;
	}
	
	public Categoria buscarPorNome(String nome) throws SQLException {
		
		return null;
	}
	
	public String atualizar(Categoria categoria) throws SQLException {
		
		return null;
	}
	
	public String excluir(String nome) throws SQLException {
		
		return 0;
	}
}
