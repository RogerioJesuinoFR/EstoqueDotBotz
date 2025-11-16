package services;

import dao.BancoDados;
import dao.PedidoDAO;
import entities.Pedido;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PedidoService {

    public boolean cadastrarNovoPedido(Pedido pedido) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false); 
            PedidoDAO dao = new PedidoDAO(conn);
            
            int idPedidoGerado = dao.cadastrarPedidoPrincipal(pedido);
            if (idPedidoGerado == 0) throw new SQLException("Falha ao criar o pedido principal.");
            
            dao.cadastrarItensDoPedido(idPedidoGerado, pedido.getItensDoPedido());
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    public List<Pedido> buscarTodosPedidos() throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            PedidoDAO dao = new PedidoDAO(conn);
            return dao.buscarTodos();
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    public boolean atualizarStatusPedido(String idPedido, String novoStatus) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            PedidoDAO dao = new PedidoDAO(conn);
            int linhas = dao.atualizarStatus(idPedido, novoStatus);
            
            if (linhas > 0) {
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
    
    public boolean excluirPedido(String idPedido) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            PedidoDAO dao = new PedidoDAO(conn);
            int linhas = dao.excluir(idPedido);
            
            if (linhas > 0) {
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
    
    // NOVO: Serviço de Busca por ID
    public Pedido buscarPedidoPorID(String idPedido) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            PedidoDAO dao = new PedidoDAO(conn);
            return dao.buscarPorID(idPedido);
        } finally {
            BancoDados.desconectar(conn);
        }
    }
    
    // NOVO: Serviço de Atualização (Wipe and Re-insert)
    public boolean atualizarPedido(Pedido pedido) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar();
            conn.setAutoCommit(false);
            
            PedidoDAO dao = new PedidoDAO(conn);
            
            // 1. Atualiza os dados principais (data, status)
            dao.atualizarPedidoPrincipal(pedido);
            
            // 2. Remove todos os itens antigos
            dao.excluirItensDoPedido(pedido.getIdPedido());
            
            // 3. Cadastra os novos itens
            dao.cadastrarItensDoPedido(Integer.parseInt(pedido.getIdPedido()), pedido.getItensDoPedido());
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            BancoDados.desconectar(conn);
        }
    }
}