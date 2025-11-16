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
import javax.swing.border.EmptyBorder;

// NECESSÁRIO PARA INTEGRAÇÃO DB
import services.ItemService;
import entities.Item;
import entities.Usuario; // IMPORTAR USUARIO

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable itemTable;
    private Usuario usuarioLogado; // CORREÇÃO: Armazena o objeto Usuario
    private String userProfile; // Mantém o setor (String) para facilitar

    // **CONSTRUTOR ATUALIZADO**
    public MainWindow(String[] categories, Usuario usuario) {
        this.usuarioLogado = usuario;
        this.userProfile = usuario.getSetorUsuario(); // Extrai o setor (String)
        
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
        
        // **CORREÇÃO VISUAL: Cor da fonte do Menu (Preto -> Branco)**
        JMenu menuEstoque = new JMenu("Estoque"); menuEstoque.setForeground(new Color(0, 0, 0));
        JMenu menuSair = new JMenu("Sair"); menuSair.setForeground(new Color(0, 0, 0));
        
        // LIGAÇÃO: MAIN (ITENS) -> MAIN (CATEGORIAS)
        JMenuItem itemGerenciarCategorias = new JMenuItem("Gerenciar Categorias");
        itemGerenciarCategorias.setBackground(Color.WHITE);
        itemGerenciarCategorias.setForeground(Color.BLACK);
        
        itemGerenciarCategorias.addActionListener(e -> {
            MainCategoriasWindow categoriaFrame = new MainCategoriasWindow(usuarioLogado); // Passa o Usuario
            categoriaFrame.setVisible(true);
            dispose();
        });
        menuEstoque.add(itemGerenciarCategorias);
        
        // LIGAÇÃO: MAIN (ITENS) -> MAIN (PEDIDOS)
        JMenuItem itemGerenciarPedidos = new JMenuItem("Gerenciar Pedidos");
        itemGerenciarPedidos.setBackground(Color.WHITE);
        itemGerenciarPedidos.setForeground(Color.BLACK);
        
        itemGerenciarPedidos.addActionListener(e -> {
            // ** CORREÇÃO DO ERRO: Passa o objeto 'usuarioLogado' (que agora existe) **
            MainPedidosWindow pedidosFrame = new MainPedidosWindow(usuarioLogado); 
            pedidosFrame.setVisible(true);
            dispose(); // Fecha a MainWindow
        });
        menuEstoque.add(itemGerenciarPedidos);
        
        // LIGAÇÃO: LOGOUT
        JMenuItem itemSairMenu = new JMenuItem("Logout"); // Renomeado para evitar conflito de nome
        itemSairMenu.setBackground(Color.WHITE);
        itemSairMenu.setForeground(Color.BLACK);
        
        itemSairMenu.addActionListener(e -> { new LoginWindow().setVisible(true); dispose(); });
        menuSair.add(itemSairMenu);
        
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
            // Usa o userProfile (String) para filtrar
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
        JPanel panel = new JPanel(); 
        panel.setBackground(fundo);
        panel.setBorder(new EmptyBorder(5, 10, 5, 10)); 
        
        Color CLR_ADICIONAR = new Color(51, 153, 255);
        Color CLR_EDITAR = new Color(255, 165, 0);
        Color CLR_SALVAR_QTD = new Color(102, 187, 106);
        Color CLR_REMOVER = new Color(200, 50, 50);

        JButton btnAdicionar = createActionButton("Adicionar Novo Item", CLR_ADICIONAR);
        JButton btnEditar = createActionButton("Editar Item Selecionado", CLR_EDITAR);
        JButton btnAlterarQtd = createActionButton("Salvar Quantidade", CLR_SALVAR_QTD);
        JButton btnRemover = createActionButton("Remover Item", CLR_REMOVER);
        
        // 1. LIGAÇÃO: MAIN -> CADASTRO DE ITENS
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
            if (itemTable.isEditing()) {
                itemTable.getCellEditor().stopCellEditing();
            }
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String itemId = String.valueOf(itemTable.getModel().getValueAt(selectedRow, 0));
                    int novaQuantidade = (Integer) itemTable.getModel().getValueAt(selectedRow, 3);

                    ItemService service = new ItemService();
                    if (service.atualizarQuantidadeItem(itemId, novaQuantidade)) {
                        JOptionPane.showMessageDialog(this, "Quantidade atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        refreshTable(); 
                    } else {
                        JOptionPane.showMessageDialog(this, "Falha ao atualizar a quantidade no banco.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ClassCastException cce) {
                     JOptionPane.showMessageDialog(this, "Erro: A quantidade inserida não é um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro de DB: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um item na tabela e altere a quantidade na célula antes de salvar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // 4. AÇÃO: REMOÇÃO DE ITENS
        btnRemover.addActionListener(e -> {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow != -1) {
                String itemId = String.valueOf(itemTable.getModel().getValueAt(selectedRow, 0));
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o Item ID: " + itemId + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        ItemService service = new ItemService();
                        if(service.removerItem(itemId)) {
                            JOptionPane.showMessageDialog(this, "Item removido com sucesso!", "Remoção", JOptionPane.INFORMATION_MESSAGE);
                            refreshTable();
                        } else {
                             JOptionPane.showMessageDialog(this, "Falha ao remover item.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException | IOException ex) {
                         JOptionPane.showMessageDialog(this, "Erro de DB: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(btnAdicionar); panel.add(btnEditar); panel.add(btnAlterarQtd); panel.add(btnRemover);
        return panel;
    }
    
    private JButton createActionButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setOpaque(true); 
        button.setContentAreaFilled(true);
        return button;
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