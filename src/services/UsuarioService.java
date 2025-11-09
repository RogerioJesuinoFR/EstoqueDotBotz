package services; 

import dao.BancoDados;
import dao.UsuarioDAO;
import entities.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UsuarioService {
    
    // Serviço para login
    public Usuario fazerLogin(String ra, String senha) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            UsuarioDAO dao = new UsuarioDAO(conn); // Passa a conexão
            
            // Chama o método 'login' (que adicionaremos ao DAO)
            Usuario usuario = dao.login(ra, senha);
            
            return usuario;
            
        } finally {
            BancoDados.desconectar(conn); // O Serviço fecha a conexão
        }
    }

    // Serviço para cadastro
    public boolean cadastrarNovoUsuario(Usuario usuario) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            
            UsuarioDAO dao = new UsuarioDAO(conn);
            int linhasAfetadas = dao.cadastrar(usuario);
            
            if (linhasAfetadas > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
            
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e; 
        } finally {
            BancoDados.desconectar(conn);
        }
    }
}