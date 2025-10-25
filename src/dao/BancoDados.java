package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class BancoDados {
	
	public static Connection conectar() throws SQLException, IOException {
				
			Properties props = carregarPropriedades();
			String url = props.getProperty("dburl");
		
		return DriverManager.getConnection(url, props);
	}
	
	public static void desconectar(Connection conn) throws SQLException {
	    
	    if (conn != null) {
	        
	        conn.close();
	    }
	}
	
	private static Properties carregarPropriedades() throws IOException {
				
		try (FileInputStream propriedadesBanco = new FileInputStream("database.properties")) {
			
			Properties props = new Properties();
			props.load(propriedadesBanco);
			
			return props;
		}
	}
	
	public static void finalizarStatement(Statement st) throws SQLException {
		
		if (st != null) {
			st.close();
		}
	}
	
	public static void finalizarResultSet(ResultSet rs) throws SQLException {
		
		if (rs != null) {
			
			rs.close();
		}
	}
}
