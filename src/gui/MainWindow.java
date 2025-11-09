package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

// NECESSÁRIO PARA INTEGRAÇÃO DB
import services.ItemService;
import entities.Item;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable itemTable;
    private String userProfile;

    public MainWindow(String[] categories, String profile) {
        this.userProfile = profile;
        
        setTitle("EstoqueDotBotz - Dashboard Principal (Perfil: " + userProfile + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // TELA CHEIA
        
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        
        setJMenuBar(createMenuBar());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(CLR_FUNDO_ESCURO);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(CLR_FUNDO_ESCURO); tabbedPane.setForeground(CLR_BRANCO_CLARO);

        tabbedPane.addTab("Estoque Principal (" + userProfile + ")", createStockPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(createActionButtonsPanel(CLR_FUNDO_ESCURO), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); menuBar.setBackground(new Color(30, 30, 30));
        JMenu menuEstoque = new JMenu("Estoque"); menuEstoque.setForeground(Color.WHITE);
        JMenu menuSair = new JMenu("Sair"); menuSair.setForeground(Color.WHITE);
        
        // LIGAÇÃO: MAIN (ITENS) -> MAIN (CATEGORIAS)
        JMenuItem itemGerenciarCategorias = new JMenuItem("Gerenciar Categorias");
        itemGerenciarCategorias.addActionListener(e -> {
            MainCategoriasWindow categoriaFrame = new MainCategoriasWindow(userProfile);
            categoriaFrame.setVisible(true);
            dispose();
        });
        menuEstoque.add(itemGerenciarCategorias);
        
        // LIGAÇÃO: LOGOUT
        JMenuItem itemSair = new JMenuItem("Logout");
        itemSair.addActionListener(e -> { new LoginWindow().setVisible(true); dispose(); });
        menuSair.add(itemSair);
        
        menuEstoque.add(new JMenuItem("Gerenciar Pedidos"));
        menuBar.add(menuEstoque);
        menuBar.add(menuSair);
        return menuBar;
    }

    private JPanel createStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "Nome do Item", "Perfil", "Quantidade", "Validade"};
        
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return column == 3; }
        };

        // --- LÓGICA DE CARREGAMENTO DE DADOS REAIS ---
        try {
            ItemService itemService = new ItemService();
            List<Item> itens = itemService.buscarItensPorPerfil(userProfile);
            
            for (Item item : itens) {
                model.addRow(new Object[] {
                    item.getIdItem(),
                    item.getNomeItem(),
                    item.getSetorItem(),
                    item.getQuantidadeAtualItem(),
                    item.getValidadeItem() 
                });
            }
            
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar itens do estoque: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        // ---------------------------------------------

        itemTable = new JTable(model);
        panel.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActionButtonsPanel(Color fundo) {
        // ... (Corpo dos botões e listeners omitidos por já serem conhecidos e ativos)
        JPanel panel = new JPanel(); panel.setBackground(fundo);
        // ... (Lógica de botões)
        return panel;
    }
    
    private JButton createActionButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setOpaque(true); button.setContentAreaFilled(true);
        return button;
    }
}