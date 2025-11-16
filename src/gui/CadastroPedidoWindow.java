package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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
    private List<Item> itensDisponiveis;

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
        
        getContentPane().add(painelAdicionar, BorderLayout.CENTER);

        // --- Painel Inferior (Layout Corrigido) ---
        JPanel southPanel = new JPanel(new BorderLayout());

        // Tabela de Itens Adicionados
        String[] colunasItens = {"ID", "Nome do Item", "Quantidade"};
        tableModel = new DefaultTableModel(colunasItens, 0);
        tabelaItensAdicionados = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(tabelaItensAdicionados);
        
        // ** CORREÇÃO DE LAYOUT: Define uma altura preferida para a tabela **
        // Define a altura preferida do scroll pane (150 pixels)
        scrollPane.setPreferredSize(new Dimension(0, 150)); 
        
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Botão Salvar Pedido
        JButton btnSalvarPedido = new JButton("Salvar Pedido Completo");
        southPanel.add(btnSalvarPedido, BorderLayout.SOUTH);

        getContentPane().add(southPanel, BorderLayout.SOUTH);
        
        // --- Ações (Listeners) ---
        
        btnAdicionarItem.addActionListener(e -> {
            Item itemSelecionado = (Item) cmbItens.getSelectedItem();
            int quantidade = (Integer) spinQuantidade.getValue();
            
            if (itemSelecionado != null) {
                tableModel.addRow(new Object[]{
                    itemSelecionado.getIdItem(),
                    itemSelecionado.getNomeItem(),
                    quantidade
                });
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
            
            // Customiza como o item aparece no ComboBox (mostra o nome)
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
        
        // 1. Criar o objeto Pedido
        Pedido novoPedido = new Pedido();
        novoPedido.setIdUsuario(usuarioLogado.getIdUsuario());
        novoPedido.setDataSolicitacaoPedido(txtDataSolicitacao.getText());
        novoPedido.setDataPrevisaoPedido(txtDataPrevisao.getText());
        novoPedido.setStatusPedido((String) cmbStatus.getSelectedItem());
        
        // 2. Adicionar os itens da tabela ao objeto Pedido
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String idItem = (String) tableModel.getValueAt(i, 0);
            String nomeItem = (String) tableModel.getValueAt(i, 1);
            int quantidade = (Integer) tableModel.getValueAt(i, 2);
            
            novoPedido.adicionarItem(new PedidoItem(idItem, nomeItem, quantidade));
        }
        
        // 3. Chamar o serviço de persistência
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
}