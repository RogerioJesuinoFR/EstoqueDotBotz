package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import entities.Usuario;
import dao.BancoDados; 

public class UsuarioDAOTest {

    @Test
    void cadastrarCursoTeste() throws SQLException, IOException {
        
        Usuario usuario = new Usuario(); 
        usuario.setNomeUsuario("João da Silva");
        usuario.setRAUsuario("123456");
        usuario.setSetorUsuario("Autonomos");
        usuario.setSenhaUsuario("!123Senha");
        usuario.setDataCriacaoUsuario("2025-10-19"); 
        
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            int resultado = new UsuarioDAO(conn).cadastrar(usuario); 
            
            assertEquals(1, resultado);
            
        } finally {
            BancoDados.desconectar(conn);
        }
    }
}