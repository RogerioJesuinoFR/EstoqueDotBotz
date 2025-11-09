package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import java.io.IOException;
import java.sql.SQLException;
import services.ItemService;
import entities.Item;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import services.CategoriaService;
import entities.Categoria;

public class CadastroItemWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtNomeItem;
    private JTextField txtUnidadeMedida;
    private JTextField txtDataValidade;
    private JComboBox<String> cmbCategoria;
    private JComboBox<String> cmbSetor;
    private JTextArea txtDescricao; // VARIÁVEL DE INSTÂNCIA
    private JSpinner spinQtdAtual;
    private JSpinner spinQtdMinima;
    
    private MainWindow parentWindow;
    private String userProfile;
    private Map<String, String> categoriaMap = new HashMap<>(); 

    public CadastroItemWindow(String profile, MainWindow parentWindow) {
        this.userProfile = profile.toUpperCase();
        this.parentWindow = parentWindow;
        
        setTitle("Cadastro de Novo Item (Perfil: " + this.userProfile + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        
        // --- Estilo ---
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        Color CLR_AZUL_CADASTRO = new Color(51, 153, 255);
        Font FNT_PADRAO = new Font("Tahoma", Font.BOLD, 12);
        
        getContentPane().setBackground(CLR_FUNDO_ESCURO);
        getContentPane().setLayout(null);
        
        int yOffset = 30;
        
        // --- Componentes ---
        JLabel lblNomeItem = new JLabel("Nome do Item:"); lblNomeItem.setFont(FNT_PADRAO); lblNomeItem.setForeground(CLR_BRANCO_CLARO); lblNomeItem.setBounds(50, yOffset, 150, 20); getContentPane().add(lblNomeItem);
        txtNomeItem = new JTextField(); txtNomeItem.setBackground(CLR_BRANCO_CLARO); txtNomeItem.setBounds(250, yOffset, 200, 25); getContentPane().add(txtNomeItem);
        yOffset += 40;

        JLabel lblCategoria = new JLabel("Categoria:"); lblCategoria.setFont(FNT_PADRAO); lblCategoria.setForeground(CLR_BRANCO_CLARO); lblCategoria.setBounds(50, yOffset, 150, 20); getContentPane().add(lblCategoria);
        cmbCategoria = new JComboBox<>(); cmbCategoria.setBackground(CLR_BRANCO_CLARO); cmbCategoria.setBounds(250, yOffset, 200, 25); getContentPane().add(cmbCategoria);
        carregarCategorias();
        yOffset += 40;

        // Descrição - CORREÇÃO CRÍTICA DE INICIALIZAÇÃO
        JLabel lblDescricao = new JLabel("Descrição (Opcional):"); lblDescricao.setFont(FNT_PADRAO); lblDescricao.setForeground(CLR_BRANCO_CLARO); lblDescricao.setBounds(50, yOffset, 200, 20); getContentPane().add(lblDescricao);
        txtDescricao = new JTextArea(); // Inicializa a variável de instância
        txtDescricao.setWrapStyleWord(true); txtDescricao.setLineWrap(true); txtDescricao.setBackground(CLR_BRANCO_CLARO);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao); scrollDescricao.setBounds(50, yOffset + 25, 400, 80); getContentPane().add(scrollDescricao);
        yOffset += 115;

        // Qtd Atual e Mínima
        JLabel lblQtdAtual = new JLabel("Quantidade Atual:"); lblQtdAtual.setFont(FNT_PADRAO); lblQtdAtual.setForeground(CLR_BRANCO_CLARO); lblQtdAtual.setBounds(50, yOffset, 150, 20); getContentPane().add(lblQtdAtual);
        spinQtdAtual = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1)); spinQtdAtual.setBounds(250, yOffset, 60, 25); getContentPane().add(spinQtdAtual);
        yOffset += 40;

        JLabel lblQtdMinima = new JLabel("Quantidade Mínima:"); lblQtdMinima.setFont(FNT_PADRAO); lblQtdMinima.setForeground(CLR_BRANCO_CLARO); lblQtdMinima.setBounds(50, yOffset, 150, 20); getContentPane().add(lblQtdMinima);
        spinQtdMinima = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1)); spinQtdMinima.setBounds(250, yOffset, 60, 25); getContentPane().add(spinQtdMinima);
        yOffset += 40;

        // Unidade de Medida
        JLabel lblUnidadeMedida = new JLabel("Unidade de Medida:"); lblUnidadeMedida.setFont(FNT_PADRAO); lblUnidadeMedida.setForeground(CLR_BRANCO_CLARO); lblUnidadeMedida.setBounds(50, yOffset, 150, 20); getContentPane().add(lblUnidadeMedida);
        txtUnidadeMedida = new JTextField(); txtUnidadeMedida.setBackground(CLR_BRANCO_CLARO); txtUnidadeMedida.setBounds(250, yOffset, 200, 25); getContentPane().add(txtUnidadeMedida);
        yOffset += 40;

        // Data de Validade
        JLabel lblDataValidade = new JLabel("Data de Validade (AAAA-MM-DD):"); lblDataValidade.setFont(FNT_PADRAO); lblDataValidade.setForeground(CLR_BRANCO_CLARO); lblDataValidade.setBounds(50, yOffset, 200, 20); getContentPane().add(lblDataValidade);
        txtDataValidade = new JTextField(); txtDataValidade.setBackground(CLR_BRANCO_CLARO); txtDataValidade.setBounds(250, yOffset, 200, 25); getContentPane().add(txtDataValidade);
        yOffset += 40;

        // Setor (Lógica Condicional)
        JLabel lblSetor = new JLabel("Setor:"); lblSetor.setFont(FNT_PADRAO); lblSetor.setForeground(CLR_BRANCO_CLARO); lblSetor.setBounds(50, yOffset, 150, 20); getContentPane().add(lblSetor);
        cmbSetor = new JComboBox<>(); cmbSetor.setBackground(CLR_BRANCO_CLARO); cmbSetor.setBounds(250, yOffset, 200, 25); setupSetorComboBox(this.userProfile); getContentPane().add(cmbSetor);
        yOffset += 60;

        // Botão de Cadastro
        JButton btnCadastrar = new JButton("Adicionar Item ao Estoque");
        btnCadastrar.setFont(FNT_PADRAO); btnCadastrar.setForeground(Color.WHITE); btnCadastrar.setBackground(CLR_AZUL_CADASTRO); btnCadastrar.setOpaque(true); btnCadastrar.setContentAreaFilled(true);
        btnCadastrar.setBounds(50, yOffset, 400, 35);
        
        btnCadastrar.addActionListener(e -> {
            efetuarCadastroItem();
        });
        getContentPane().add(btnCadastrar);
    }
    
    // Método para carregar categorias (mantido)
    private void carregarCategorias() {
        CategoriaService service = new CategoriaService();
        try {
            List<Categoria> categorias = service.buscarTodasCategorias();
            
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            
            if (categorias.isEmpty()) {
                model.addElement("Nenhuma categoria cadastrada");
            } else {
                for (Categoria cat : categorias) {
                    String nomeCompleto = cat.getNomeCategoria();
                    model.addElement(nomeCompleto);
                    categoriaMap.put(nomeCompleto, cat.getIdCategoria()); 
                }
            }
            cmbCategoria.setModel(model);
            
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage(), "Erro de DB", JOptionPane.ERROR_MESSAGE);
            cmbCategoria.setModel(new DefaultComboBoxModel<>(new String[] {"Erro ao carregar"}));
        }
    }

    // Configura o ComboBox de Setor (mantido)
    private void setupSetorComboBox(String userProfile) {
        if ("AMBOS".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"AUTONOMOS", "COMBATE", "AMBOS"}));
            cmbSetor.setEnabled(true);
        } else if ("AUTONOMOS".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"AUTONOMOS", "AMBOS"}));
            cmbSetor.setEnabled(false);
        } else if ("COMBATE".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"COMBATE", "AMBOS"}));
            cmbSetor.setEnabled(false);
        }
    }
    
    // Método de persistência do item (mantido)
    private void efetuarCadastroItem() {
        String nome = txtNomeItem.getText().trim();
        String unidade = txtUnidadeMedida.getText().trim();
        String validade = txtDataValidade.getText().trim(); 
        String descricao = txtDescricao.getText().trim(); // NULO ESTÁ CORRIGIDO
        
        String categoriaBruta = (String) cmbCategoria.getSelectedItem();
        String setor = (String) cmbSetor.getSelectedItem();
        
        // Coleta de Spinners com tratamento de cast
        int qtdAtual;
        int qtdMinima;
        try {
            qtdAtual = (Integer) spinQtdAtual.getValue();
            qtdMinima = (Integer) spinQtdMinima.getValue();
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(this, "Erro: Quantidade inválida.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idCategoria = categoriaMap.get(categoriaBruta); 

        if (nome.isEmpty() || unidade.isEmpty() || setor == null || qtdAtual <= 0) {
            JOptionPane.showMessageDialog(this, "Nome, Unidade e Quantidade (> 0) são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (idCategoria == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria válida.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Criação da Entidade
        Item novoItem = new Item();
        novoItem.setNomeItem(nome);
        novoItem.setDescricaoItem(descricao);
        novoItem.setQuantidadeAtualItem(qtdAtual);
        novoItem.setQuantidadeMinimaItem(qtdMinima);
        novoItem.setUnidadeMedidaItem(unidade);
        novoItem.setValidadeItem(validade);
        novoItem.setSetorItem(setor);
        novoItem.setCategoria(idCategoria); 
        
        ItemService service = new ItemService();
        
        // Persistência
        try {
            if (service.cadastrarNovoItem(novoItem)) {
                JOptionPane.showMessageDialog(this, "Item cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.parentWindow.refreshTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao cadastrar item. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro de DB (Chave Estrangeira ou Formato): " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}