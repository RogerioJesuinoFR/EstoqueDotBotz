// services/ItemService.java - CORRIGIDO

package services;

import dao.BancoDados;
import dao.ItemDAO;
import entities.Item;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ItemService {

	public List<Item> buscarItensPorPerfil(String userProfile) throws SQLException, IOException {
        Connection conn = null;
        try {
            conn = BancoDados.conectar(); // ABRE A CONEXÃO AQUI
            ItemDAO dao = new ItemDAO(conn);// INJETA A CONEXÃO ATIVA
            
            return dao.buscarPorSetorEAmbos(userProfile);
            
        } finally {
            // FECHA A CONEXÃO AQUI, NO SERVICE
            BancoDados.desconectar(conn); 
        }
    }
}