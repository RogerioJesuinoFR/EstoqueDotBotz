package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
import javax.swing.UIManager;

// NECESSÁRIO PARA INTEGRAÇÃO DB
import services.ItemService;
import entities.Item;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable itemTable;
    private String userProfile;

    // Construtor principal
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
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Object.class; 
                if (columnIndex == 3) return Integer.class;
                return super.getColumnClass(columnIndex);
            }
        };

        // --- LÓGICA DE CARREGAMENTO DE DADOS INICIAL ---
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
        JPanel panel = new JPanel(); panel.setBackground(fundo);
        
        Color CLR_ADICIONAR = new Color(51, 153, 255);
        Color CLR_EDITAR = new Color(255, 165, 0);
        Color CLR_SALVAR_QTD = new Color(102, 187, 106);
        Color CLR_REMOVER = new Color(200, 50, 50);

        JButton btnAdicionar = createActionButton("Adicionar Novo Item", CLR_ADICIONAR);
        JButton btnEditar = createActionButton("Editar Item Selecionado", CLR_EDITAR);
        JButton btnAlterarQtd = createActionButton("Salvar Quantidade", CLR_SALVAR_QTD);
        JButton btnRemover = createActionButton("Remover Item", CLR_REMOVER);
        
        // 1. LIGAÇÃO: MAIN -> CADASTRO DE ITENS (Passando 'this' para o refresh)
        btnAdicionar.addActionListener(e -> {
            CadastroItemWindow cadastroItem = new CadastroItemWindow(userProfile, this); 
            cadastroItem.setVisible(true);
        });
        
        // 2. LIGAÇÃO: MAIN -> EDIÇÃO DE ITENS
        btnEditar.addActionListener(e -> {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow != -1) {
                String itemIdStr = String.valueOf(itemTable.getModel().getValueAt(selectedRow, 0));
                
                try {
                    int itemId = Integer.parseInt(itemIdStr);
                    EdicaoItemWindow edicaoItem = new EdicaoItemWindow(itemId, userProfile, this); 
                    edicaoItem.setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "O ID selecionado é inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um item para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // 3. AÇÃO: EDIÇÃO DE QUANTIDADE
        btnAlterarQtd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Implementar lógica de UPDATE de quantidade (Simulação).", "Função Pendente", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // 4. AÇÃO: REMOÇÃO DE ITENS
        btnRemover.addActionListener(e -> {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Item removido (Simulação).", "Remoção", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(btnAdicionar); panel.add(btnEditar); panel.add(btnAlterarQtd); panel.add(btnRemover);
        return panel;
    }
    
    // ** CORREÇÃO CRÍTICA: Método que estava retornando 'null' **
    private JButton createActionButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setOpaque(true); 
        button.setContentAreaFilled(true);
        return button; // Retorna o botão criado
    }
    
    // MÉTODO ESSENCIAL: Recarrega a tabela do banco
    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        model.setRowCount(0); 

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
            JOptionPane.showMessageDialog(this, "Erro ao atualizar listagem: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}