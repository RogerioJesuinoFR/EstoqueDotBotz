package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultCellEditor;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import entities.Item;
import entities.Pedido;
import entities.PedidoItem;
import entities.Usuario;
import services.ItemService;
import services.PedidoService;

public class EdicaoPedidoWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JComboBox<Item> cmbItens; 
    private JSpinner spinQuantidade;
    private JTable tabelaItensAdicionados;
    private DefaultTableModel tableModel;
    private JTextField txtDataSolicitacao;
    private JTextField txtDataPrevisao;
    private JComboBox<String> cmbStatus;
    
    private Usuario usuarioLogado;
    private MainPedidosWindow parentWindow;
    private List<Item> itensDisponiveis;
    private Pedido pedidoOriginal; 

    public EdicaoPedidoWindow(String idPedido, Usuario usuario, MainPedidosWindow parent) {
        this.usuarioLogado = usuario;
        this.parentWindow = parent;
        
        setTitle("Edição do Pedido ID: " + idPedido);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        
        // --- Carregar Dados ---
        carregarItensDisponiveis(usuario.getSetorUsuario()); 
        if (!carregarDadosPedido(idPedido)) {
            dispose();
            return;
        }

        // --- Painel Superior: Dados do Pedido ---
        JPanel painelDados = new JPanel(new GridLayout(4, 2, 10, 10));
        painelDados.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        txtDataSolicitacao = new JTextField();
        painelDados.add(new JLabel("Data Solicitação (AAAA-MM-DD):"));
        painelDados.add(txtDataSolicitacao);
        
        txtDataPrevisao = new JTextField();
        painelDados.add(new JLabel("Data Previsão (AAAA-MM-DD):"));
        painelDados.add(txtDataPrevisao);
        
        cmbStatus = new JComboBox<>(new String[] {"ABERTO", "CONCLUIDO", "CANCELADO", "ATRASADO"});
        painelDados.add(new JLabel("Status:"));
        painelDados.add(cmbStatus);
        
        getContentPane().add(painelDados, BorderLayout.NORTH);

        // --- Painel Central: Adicionar Itens ---
        JPanel painelAdicionar = new JPanel();
        painelAdicionar.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        cmbItens = new JComboBox<>();
        popularComboBoxItens(); // Popula o JComboBox
        
        spinQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAdicionarItem = new JButton("Adicionar Item");
        
        // ** CORREÇÃO: Botão "Remover Item" global removido deste painel **
        
        painelAdicionar.add(new JLabel("Item:"));
        painelAdicionar.add(cmbItens);
        painelAdicionar.add(new JLabel("Qtd:"));
        painelAdicionar.add(spinQuantidade);
        painelAdicionar.add(btnAdicionarItem);
        
        getContentPane().add(painelAdicionar, BorderLayout.CENTER);

        // --- Painel Inferior (Layout Corrigido) ---
        JPanel southPanel = new JPanel(new BorderLayout());
        
        String[] colunasItens = {"ID", "Nome do Item", "Quantidade", "Remover"};
        tableModel = new DefaultTableModel(colunasItens, 0) {
            public boolean isCellEditable(int row, int column) { 
                return column == 3; 
            }
        };
        tabelaItensAdicionados = new JTable(tableModel);
        
        tabelaItensAdicionados.getColumn("Remover").setCellRenderer(new ButtonRenderer());
        tabelaItensAdicionados.getColumn("Remover").setCellEditor(
            new ButtonEditor(new JCheckBox(), "Remover")
        );
        
        JScrollPane scrollPane = new JScrollPane(tabelaItensAdicionados);
        scrollPane.setPreferredSize(new Dimension(0, 150)); 
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnSalvarPedido = new JButton("Salvar Alterações");
        btnSalvarPedido.setBackground(new Color(255, 165, 0)); // Cor Laranja
        btnSalvarPedido.setForeground(Color.WHITE);
        southPanel.add(btnSalvarPedido, BorderLayout.SOUTH);

        getContentPane().add(southPanel, BorderLayout.SOUTH);
        
        // --- Popula os campos com os dados carregados ---
        populaCamposDaInterface();
        
        // --- Ações (Listeners) ---
        
        btnAdicionarItem.addActionListener(e -> {
            Item itemSelecionado = (Item) cmbItens.getSelectedItem();
            int quantidade = (Integer) spinQuantidade.getValue();
            if (itemSelecionado != null) {
                tableModel.addRow(new Object[]{
                    itemSelecionado.getIdItem(),
                    itemSelecionado.getNomeItem(),
                    quantidade,
                    "Remover"
                });
                ((DefaultComboBoxModel<Item>)cmbItens.getModel()).removeElement(itemSelecionado);
            }
        });
        
        btnSalvarPedido.addActionListener(e -> {
            salvarAlteracoesPedido();
        });
    }

    private boolean carregarDadosPedido(String idPedido) {
        PedidoService service = new PedidoService();
        try {
            this.pedidoOriginal = service.buscarPedidoPorID(idPedido);
            if (this.pedidoOriginal == null) {
                JOptionPane.showMessageDialog(this, "Pedido não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do pedido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void populaCamposDaInterface() {
        if (pedidoOriginal == null) return;
        
        txtDataSolicitacao.setText(pedidoOriginal.getDataSolicitacaoPedido());
        txtDataPrevisao.setText(pedidoOriginal.getDataPrevisaoPedido());
        cmbStatus.setSelectedItem(pedidoOriginal.getStatusPedido());
        
        for (PedidoItem item : pedidoOriginal.getItensDoPedido()) {
             tableModel.addRow(new Object[]{
                item.getIdItem(),
                item.getNomeItem(),
                item.getQuantidadeComprada(),
                "Remover"
            });
             
            for (int i = 0; i < cmbItens.getItemCount(); i++) {
                Item itemDisponivel = cmbItens.getItemAt(i);
                if (itemDisponivel.getIdItem().equals(item.getIdItem())) {
                    cmbItens.removeItemAt(i);
                    break;
                }
            }
        }
    }

    private void carregarItensDisponiveis(String userProfile) {
        ItemService service = new ItemService();
        try {
            this.itensDisponiveis = service.buscarItensPorPerfil(userProfile);
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar itens disponíveis: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            this.itensDisponiveis = new ArrayList<>();
        }
    }
    
    private void popularComboBoxItens() {
        DefaultComboBoxModel<Item> model = new DefaultComboBoxModel<>();
        for (Item item : itensDisponiveis) {
            model.addElement(item); 
        }
        cmbItens.setModel(model);
        
        cmbItens.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Item) {
                    setText(((Item) value).getNomeItem());
                }
                return this;
            }
        });
    }
    
    private void salvarAlteracoesPedido() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item ao pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        pedidoOriginal.setDataSolicitacaoPedido(txtDataSolicitacao.getText());
        pedidoOriginal.setDataPrevisaoPedido(txtDataPrevisao.getText());
        pedidoOriginal.setStatusPedido((String) cmbStatus.getSelectedItem());
        
        pedidoOriginal.getItensDoPedido().clear();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String idItem = (String) tableModel.getValueAt(i, 0);
            String nomeItem = (String) tableModel.getValueAt(i, 1);
            int quantidade = (Integer) tableModel.getValueAt(i, 2);
            pedidoOriginal.adicionarItem(new PedidoItem(idItem, nomeItem, quantidade));
        }
        
        PedidoService service = new PedidoService();
        try {
            if (service.atualizarPedido(pedidoOriginal)) {
                JOptionPane.showMessageDialog(this, "Pedido atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                parentWindow.refreshTable();
                dispose();
            } else {
                 JOptionPane.showMessageDialog(this, "Falha ao atualizar o pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro de DB: " + e.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // --- CLASSES INTERNAS PARA O BOTÃO NA TABELA ---

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(new Color(200, 50, 50));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, String text) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(200, 50, 50));
            button.setText(text);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int selectedRow = tabelaItensAdicionados.getEditingRow();
                String idItem = (String) tableModel.getValueAt(selectedRow, 0);
                
                // REQUISITO 2: Adiciona o item de volta ao ComboBox
                for (Item item : itensDisponiveis) {
                    if (item.getIdItem().equals(idItem)) {
                        ((DefaultComboBoxModel<Item>)cmbItens.getModel()).addElement(item);
                        break;
                    }
                }
                
                tableModel.removeRow(selectedRow);
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}