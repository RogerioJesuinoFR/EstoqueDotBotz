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
import javax.swing.table.TableColumn;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultCellEditor; 
import javax.swing.JComboBox;

// Imports
import entities.Usuario;
import services.PedidoService;
import entities.Pedido;

public class MainPedidosWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable pedidosTable;
    private Usuario usuarioLogado;
    private DefaultTableModel tableModel;

    public MainPedidosWindow(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        setTitle("EstoqueDotBotz - Gerenciar Pedidos (Perfil: " + usuarioLogado.getSetorUsuario() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        
        setJMenuBar(createMenuBar());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10)); 
        mainPanel.setBackground(CLR_FUNDO_ESCURO);
        
        mainPanel.add(createPedidosPanel(CLR_FUNDO_ESCURO), BorderLayout.CENTER);
        mainPanel.add(createActionButtonsPanel(CLR_FUNDO_ESCURO), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); menuBar.setBackground(new Color(30, 30, 30)); 
        JMenu menuNav = new JMenu("Navegação"); menuNav.setForeground(new Color(0, 0, 0)); menuNav.setFont(new Font("Tahoma", Font.PLAIN, 12));
        JMenu menuSair = new JMenu("Sair"); menuSair.setForeground(new Color(0, 0, 0));
        
        JMenuItem itemItens = new JMenuItem("Ir para Itens do Estoque");
        itemItens.setBackground(Color.WHITE); itemItens.setForeground(Color.BLACK);
        itemItens.addActionListener(e -> {
            MainWindow mainFrame = new MainWindow(new String[]{}, usuarioLogado);
            mainFrame.setVisible(true);
            dispose();
        });
        menuNav.add(itemItens);
        
        JMenuItem itemSairMenu = new JMenuItem("Logout");
        itemSairMenu.setBackground(Color.WHITE); itemSairMenu.setForeground(Color.BLACK);
        itemSairMenu.addActionListener(e -> { new LoginWindow().setVisible(true); dispose(); });
        menuSair.add(itemSairMenu);

        menuBar.add(menuNav);
        menuBar.add(menuSair);
        return menuBar;
    }

    private JPanel createPedidosPanel(Color fundo) {
        JPanel panel = new JPanel(new BorderLayout()); panel.setBackground(fundo);
        
        String[] columnNames = {"ID Pedido", "Data Solicitação", "Status", "Solicitante"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { 
                return column == 2; 
            }
        };
        pedidosTable = new JTable(tableModel);
        
        TableColumn statusColumn = pedidosTable.getColumnModel().getColumn(2);
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"ABERTO", "CONCLUIDO", "CANCELADO", "ATRASADO"});
        statusColumn.setCellEditor(new DefaultCellEditor(cmbStatus));
        
        refreshTable(); 
        
        // Estilização
        pedidosTable.setBackground(new Color(60, 60, 60)); 
        pedidosTable.setForeground(Color.WHITE); 
        pedidosTable.getTableHeader().setBackground(new Color(40, 40, 40));
        pedidosTable.getTableHeader().setForeground(Color.BLACK);
        
        panel.add(new JScrollPane(pedidosTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActionButtonsPanel(Color fundo) {
        JPanel panel = new JPanel(); 
        panel.setBackground(fundo);
        panel.setBorder(new EmptyBorder(5, 10, 5, 10)); 
        
        JButton btnAdicionar = createActionButton("Novo Pedido", new Color(51, 153, 255));
        JButton btnEditar = createActionButton("Editar Pedido", new Color(255, 165, 0));
        JButton btnSalvarStatus = createActionButton("Salvar Status", new Color(102, 187, 106));
        JButton btnRemover = createActionButton("Remover Pedido", new Color(200, 50, 50));
        
        // LIGAÇÃO: NOVO PEDIDO
        btnAdicionar.addActionListener(e -> {
            CadastroPedidoWindow cadastroPedido = new CadastroPedidoWindow(usuarioLogado, this);
            cadastroPedido.setVisible(true);
        });

        // LIGAÇÃO: EDITAR PEDIDO (com lógica de status)
        btnEditar.addActionListener(e -> {
            int selectedRow = pedidosTable.getSelectedRow();
            if (selectedRow != -1) {
                String status = (String) tableModel.getValueAt(selectedRow, 2);
                if (status.equals("CONCLUIDO") || status.equals("CANCELADO")) {
                    JOptionPane.showMessageDialog(this, "Não é possível editar um pedido Concluído ou Cancelado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    String idPedido = (String) tableModel.getValueAt(selectedRow, 0);
                    // ** LIGAÇÃO ATIVADA **
                    EdicaoPedidoWindow edicaoPedido = new EdicaoPedidoWindow(idPedido, usuarioLogado, this);
                    edicaoPedido.setVisible(true);
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Selecione um pedido para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // NOVO: Salvar Status
        btnSalvarStatus.addActionListener(e -> {
            if (pedidosTable.isEditing()) {
                pedidosTable.getCellEditor().stopCellEditing();
            }
            int selectedRow = pedidosTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um pedido na tabela para salvar o status.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String idPedido = (String) tableModel.getValueAt(selectedRow, 0);
            String novoStatus = (String) tableModel.getValueAt(selectedRow, 2);
            
            try {
                PedidoService service = new PedidoService();
                if (service.atualizarStatusPedido(idPedido, novoStatus)) {
                    JOptionPane.showMessageDialog(this, "Status do pedido " + idPedido + " atualizado!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao atualizar o status.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro de DB: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // LIGAÇÃO: REMOVER PEDIDO
        btnRemover.addActionListener(e -> {
            int selectedRow = pedidosTable.getSelectedRow();
            if (selectedRow != -1) {
                String status = (String) tableModel.getValueAt(selectedRow, 2);
                String idPedido = (String) tableModel.getValueAt(selectedRow, 0);
                
                if (!status.equals("CANCELADO")) {
                    JOptionPane.showMessageDialog(this, "Apenas pedidos com status 'CANCELADO' podem ser removidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(this, "Remover Pedido ID: " + idPedido + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                         try {
                            PedidoService service = new PedidoService();
                            if (service.excluirPedido(idPedido)) {
                                JOptionPane.showMessageDialog(this, "Pedido removido.");
                                refreshTable();
                            } else {
                                JOptionPane.showMessageDialog(this, "Falha ao remover o pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException | IOException ex) {
                            JOptionPane.showMessageDialog(this, "Erro de DB: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um pedido para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(btnAdicionar); 
        panel.add(btnEditar); 
        panel.add(btnSalvarStatus);
        panel.add(btnRemover);
        return panel;
    }
    
    // Método de atualização da tabela
    public void refreshTable() {
        tableModel.setRowCount(0); 
        try {
            PedidoService service = new PedidoService();
            List<Pedido> pedidos = service.buscarTodosPedidos();
            for (Pedido p : pedidos) {
                tableModel.addRow(new Object[] {
                    p.getIdPedido(),
                    p.getDataSolicitacaoPedido(),
                    p.getStatusPedido(),
                    p.getNomeUsuario()
                });
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar pedidos: " + e.getMessage(), "Erro de DB", JOptionPane.ERROR_MESSAGE);
        }
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
}