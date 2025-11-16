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
import javax.swing.table.DefaultTableModel;

// Imports de Serviço e Entidade
import services.CategoriaService;
import entities.Categoria;

public class MainCategoriasWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable categoriaTable;
    private String userProfile;

    public MainCategoriasWindow(String profile) {
        this.userProfile = profile;
        
        setTitle("EstoqueDotBotz - Gerenciar Categorias (Perfil: " + userProfile + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setExtendedState(JFrame.MAXIMIZED_BOTH); // TELA CHEIA
        
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        
        setJMenuBar(createMenuBar());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10)); 
        mainPanel.setBackground(CLR_FUNDO_ESCURO);
        
        mainPanel.add(createCategoriaPanel(CLR_FUNDO_ESCURO), BorderLayout.CENTER);
        mainPanel.add(createActionButtonsPanel(CLR_FUNDO_ESCURO), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); menuBar.setBackground(new Color(30, 30, 30)); 
        JMenu menuNav = new JMenu("Navegação"); menuNav.setForeground(Color.WHITE); menuNav.setFont(new Font("Tahoma", Font.BOLD, 12));
        JMenu menuSair = new JMenu("Sair"); menuSair.setForeground(Color.WHITE);
        
        // LIGAÇÃO: CATEGORIAS -> MAIN (ITENS)
        JMenuItem itemItens = new JMenuItem("Voltar para Itens do Estoque");
        itemItens.addActionListener(e -> {
            MainWindow mainFrame = new MainWindow(new String[]{"Ferramentas", "Armas"}, userProfile);
            mainFrame.setVisible(true);
            dispose();
        });
        menuNav.add(itemItens);

        // LIGAÇÃO: LOGOUT
        JMenuItem itemSair = new JMenuItem("Logout");
        itemSair.addActionListener(e -> { new LoginWindow().setVisible(true); dispose(); });
        menuSair.add(itemSair);
        
        menuBar.add(menuNav);
        menuBar.add(menuSair);
        return menuBar;
    }

    private JPanel createCategoriaPanel(Color fundo) {
        JPanel panel = new JPanel(new BorderLayout()); panel.setBackground(fundo);
        
        String[] columnNames = {"ID", "Nome da Categoria", "Setor de Acesso"};
        
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return String.class; // ID agora é String (do DAO)
                return super.getColumnClass(columnIndex);
            }
        };
        categoriaTable = new JTable(model);
        
        // ** CARREGA DADOS REAIS **
        refreshTable(); 
        
        categoriaTable.setBackground(new Color(60, 60, 60)); categoriaTable.setForeground(Color.WHITE); 
        categoriaTable.getTableHeader().setBackground(new Color(40, 40, 40)); categoriaTable.getTableHeader().setForeground(Color.WHITE);
        
        panel.add(new JScrollPane(categoriaTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActionButtonsPanel(Color fundo) {
        JPanel panel = new JPanel(); panel.setBackground(fundo);
        
        Color CLR_ADICIONAR = new Color(51, 153, 255);
        Color CLR_EDITAR = new Color(255, 165, 0);
        Color CLR_REMOVER = new Color(200, 50, 50);
        Font FNT_BUTTON = new Font("Tahoma", Font.BOLD, 12);
        
        JButton btnAdicionar = createActionButton("Adicionar Nova Categoria", CLR_ADICIONAR, Color.WHITE, FNT_BUTTON);
        JButton btnEditar = createActionButton("Editar Categoria", CLR_EDITAR, Color.WHITE, FNT_BUTTON);
        JButton btnRemover = createActionButton("Remover Categoria", CLR_REMOVER, Color.WHITE, FNT_BUTTON);
        
        // 5. LIGAÇÃO: MAIN -> CADASTRO DE CATEGORIAS
        btnAdicionar.addActionListener(e -> {
            new CategoriaCadastroWindow(userProfile, this).setVisible(true);
        });

        // 6. LIGAÇÃO: MAIN -> EDIÇÃO DE CATEGORIAS
        btnEditar.addActionListener(e -> {
            int selectedRow = categoriaTable.getSelectedRow();
            if (selectedRow != -1) {
                // ID agora é String (coluna 0)
                String categoriaId = (String) categoriaTable.getModel().getValueAt(selectedRow, 0);
                new CategoriaEdicaoWindow(categoriaId, userProfile, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 7. AÇÃO: REMOÇÃO DE CATEGORIAS
        btnRemover.addActionListener(e -> {
            int selectedRow = categoriaTable.getSelectedRow();
            if (selectedRow != -1) {
                String categoriaId = (String) categoriaTable.getModel().getValueAt(selectedRow, 0);
                
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover a Categoria ID: " + categoriaId + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        CategoriaService service = new CategoriaService();
                        if (service.excluirCategoria(categoriaId)) {
                            JOptionPane.showMessageDialog(this, "Categoria removida com sucesso!");
                            refreshTable(); // Atualiza a tabela
                        } else {
                            JOptionPane.showMessageDialog(this, "Falha ao remover categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException | IOException ex) {
                        JOptionPane.showMessageDialog(this, "Erro de DB: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma categoria para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(btnAdicionar); panel.add(btnEditar); panel.add(btnRemover);
        return panel;
    }
    
    // MÉTODO DE ATUALIZAÇÃO DA TABELA
    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) categoriaTable.getModel();
        model.setRowCount(0); 
        
        try {
            CategoriaService service = new CategoriaService();
            List<Categoria> categorias = service.buscarCategoriasPorPerfil(userProfile);
            
            for (Categoria cat : categorias) {
                model.addRow(new Object[] {
                    cat.getIdCategoria(),
                    cat.getNomeCategoria(),
                    cat.getSetor() // Exibe o setor
                });
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage(), "Erro de DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private JButton createActionButton(String text, Color background, Color foreground, Font font) {
        JButton button = new JButton(text);
        button.setFont(font); button.setForeground(foreground); button.setBackground(background);
        button.setOpaque(true); button.setContentAreaFilled(true);
        return button;
    }
}