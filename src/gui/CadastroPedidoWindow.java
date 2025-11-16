package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer; // Import necessário
import javax.swing.DefaultCellEditor; // Import necessário
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import entities.Item;
import entities.Pedido;
import entities.PedidoItem;
import entities.Usuario;
import services.ItemService;
import services.PedidoService;

public class CadastroPedidoWindow extends JFrame {

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
    private List<Item> itensDisponiveis; // Lista completa de itens do DB

    public CadastroPedidoWindow(Usuario usuario, MainPedidosWindow parent) {
        this.usuarioLogado = usuario;
        this.parentWindow = parent;
        
        setTitle("Cadastro de Novo Pedido");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        
        // --- Painel Superior: Dados do Pedido ---
        JPanel painelDados = new JPanel(new GridLayout(4, 2, 10, 10));
        painelDados.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        txtDataSolicitacao = new JTextField();
        txtDataSolicitacao.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        painelDados.add(new JLabel("Data Solicitação (AAAA-MM-DD):"));
        painelDados.add(txtDataSolicitacao);
        
        txtDataPrevisao = new JTextField();
        painelDados.add(new JLabel("Data Previsão (AAAA-MM-DD):"));
        painelDados.add(txtDataPrevisao);
        
        cmbStatus = new JComboBox<>(new String[] {"ABERTO", "CONCLUIDO", "CANCELADO", "ATRASADO"});
        cmbStatus.setSelectedItem("ABERTO");
        painelDados.add(new JLabel("Status:"));
        painelDados.add(cmbStatus);
        
        getContentPane().add(painelDados, BorderLayout.NORTH);

        // --- Painel Central: Adicionar Itens ---
        JPanel painelAdicionar = new JPanel();
        painelAdicionar.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        cmbItens = new JComboBox<>();
        carregarItensDisponiveis(usuario.getSetorUsuario()); 
        
        spinQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAdicionarItem = new JButton("Adicionar Item");
        
        painelAdicionar.add(new JLabel("Item:"));
        painelAdicionar.add(cmbItens);
        painelAdicionar.add(new JLabel("Qtd:"));
        painelAdicionar.add(spinQuantidade);
        painelAdicionar.add(btnAdicionarItem);
        // O botão "Remover" global foi removido
        
        getContentPane().add(painelAdicionar, BorderLayout.CENTER);

        // --- Painel Inferior (Layout Corrigido) ---
        JPanel southPanel = new JPanel(new BorderLayout());

        // ** REQUISITO 1: Adicionada coluna "Remover" **
        String[] colunasItens = {"ID", "Nome do Item", "Quantidade", "Remover"};
        tableModel = new DefaultTableModel(colunasItens, 0) {
            // Apenas a coluna 2 (Qtd) é editável
            public boolean isCellEditable(int row, int column) { 
                return column == 3; // Permite que a coluna "Remover" (botão) seja clicável
            }
        };
        tabelaItensAdicionados = new JTable(tableModel);
        
        // ** REQUISITO 1 (Implementação): Adiciona o renderizador e editor do botão **
        tabelaItensAdicionados.getColumn("Remover").setCellRenderer(new ButtonRenderer());
        tabelaItensAdicionados.getColumn("Remover").setCellEditor(
            new ButtonEditor(new JCheckBox(), "Remover")
        );
        
        JScrollPane scrollPane = new JScrollPane(tabelaItensAdicionados);
        scrollPane.setPreferredSize(new Dimension(0, 150)); 
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnSalvarPedido = new JButton("Salvar Pedido Completo");
        southPanel.add(btnSalvarPedido, BorderLayout.SOUTH);

        getContentPane().add(southPanel, BorderLayout.SOUTH);
        
        // --- Ações (Listeners) ---
        
        btnAdicionarItem.addActionListener(e -> {
            Item itemSelecionado = (Item) cmbItens.getSelectedItem();
            int quantidade = (Integer) spinQuantidade.getValue();
            
            if (itemSelecionado != null) {
                // Adiciona na tabela (incluindo o texto do botão)
                tableModel.addRow(new Object[]{
                    itemSelecionado.getIdItem(),
                    itemSelecionado.getNomeItem(),
                    quantidade,
                    "Remover" // Texto para o botão
                });
                
                // ** REQUISITO 2: Remove o item do ComboBox **
                ((DefaultComboBoxModel<Item>)cmbItens.getModel()).removeElement(itemSelecionado);
            }
        });
        
        btnSalvarPedido.addActionListener(e -> {
            salvarPedido();
        });
    }

    private void carregarItensDisponiveis(String userProfile) {
        ItemService service = new ItemService();
        try {
            this.itensDisponiveis = service.buscarItensPorPerfil(userProfile);
            
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
            
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar itens disponíveis: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void salvarPedido() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item ao pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Pedido novoPedido = new Pedido();
        novoPedido.setIdUsuario(usuarioLogado.getIdUsuario());
        novoPedido.setDataSolicitacaoPedido(txtDataSolicitacao.getText());
        novoPedido.setDataPrevisaoPedido(txtDataPrevisao.getText());
        novoPedido.setStatusPedido((String) cmbStatus.getSelectedItem());
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String idItem = (String) tableModel.getValueAt(i, 0);
            String nomeItem = (String) tableModel.getValueAt(i, 1);
            int quantidade = (Integer) tableModel.getValueAt(i, 2);
            
            novoPedido.adicionarItem(new PedidoItem(idItem, nomeItem, quantidade));
        }
        
        PedidoService service = new PedidoService();
        try {
            if (service.cadastrarNovoPedido(novoPedido)) {
                JOptionPane.showMessageDialog(this, "Pedido cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                parentWindow.refreshTable();
                dispose();
            } else {
                 JOptionPane.showMessageDialog(this, "Falha ao cadastrar o pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro de DB: " + e.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // --- CLASSES INTERNAS PARA O BOTÃO NA TABELA ---

    /**
     * Renderizador do Botão (Aparência).
     */
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(new Color(200, 50, 50)); // Vermelho
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
     * Editor do Botão (Ação de Clique).
     */
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

        // Este método é chamado quando o botão é clicado
        public Object getCellEditorValue() {
            if (isPushed) {
                int selectedRow = tabelaItensAdicionados.getEditingRow();
                String idItem = (String) tableModel.getValueAt(selectedRow, 0);
                
                // ** REQUISITO 2: Adiciona o item de volta ao ComboBox **
                // Encontra o item na lista original
                for (Item item : itensDisponiveis) {
                    if (item.getIdItem().equals(idItem)) {
                        ((DefaultComboBoxModel<Item>)cmbItens.getModel()).addElement(item);
                        break;
                    }
                }
                
                // Remove a linha da tabela
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