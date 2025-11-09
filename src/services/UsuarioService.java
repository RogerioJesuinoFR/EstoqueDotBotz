package services; 

import dao.BancoDados;
import dao.UsuarioDAO; // Você precisará corrigir UsuarioDAO para receber Connection no construtor
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
            UsuarioDAO dao = new UsuarioDAO(conn); // Passa a conexão para o DAO
            
            // O DAO tentará encontrar o usuário e validar a senha
            Usuario usuario = dao.login(ra, senha);
            
            return usuario;
            
        } finally {
            BancoDados.desconectar(conn);
        }
    }

    // Serviço para cadastro
    public boolean cadastrarNovoUsuario(Usuario usuario) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            UsuarioDAO dao = new UsuarioDAO(conn);
            
            // Note: O método cadastrar no seu UsuarioDAO retorna int (linhas afetadas)
            int linhasAfetadas = dao.cadastrar(usuario);
            
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            // Se for um erro de chave duplicada (RA já existe)
            throw e; 
        } finally {
            BancoDados.desconectar(conn);
        }
    }
}