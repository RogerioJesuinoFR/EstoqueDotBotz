package dao;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class BancoDadosTest {

	@Test
	public void conectarTest() throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		assertNotNull(conn);
		
	BancoDados.desconectar(conn);
	}
	
	@Test
	public void desconectarTest() throws SQLException, IOException {
		
		Connection conn = BancoDados.conectar();
		BancoDados.desconectar(conn);
		
		conn = null;
		
		assertNull(conn);
	}
}
