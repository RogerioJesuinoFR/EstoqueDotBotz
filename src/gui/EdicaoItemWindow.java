package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// NECESSÁRIO PARA INTEGRAÇÃO DB
import services.ItemService;
import entities.Item;
import services.CategoriaService;
import entities.Categoria;

public class EdicaoItemWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // CAMPOS DE ENTRADA (Variáveis de Instância)
    private JTextField txtNomeItem;
    private JTextField txtUnidadeMedida;
    private JTextField txtDataValidade;
    private JComboBox<String> cmbCategoria;
    private JComboBox<String> cmbSetor;
    private JTextArea txtDescricao;
    private JSpinner spinQtdAtual;
    private JSpinner spinQtdMinima;
    
    // VARIÁVEIS DE CONTROLE
    private String userProfile;
    private MainWindow parentWindow;
    private Item itemOriginal;
    private Map<String, String> categoriaMap = new HashMap<>(); // Nome -> ID
    private Map<String, String> categoriaIdToNameMap = new HashMap<>(); // ID -> Nome
    private int itemId;

    public EdicaoItemWindow(int id, String profile, MainWindow parentWindow) {
        this.itemId = id;
        this.userProfile = profile.toUpperCase();
        this.parentWindow = parentWindow;
        
        setTitle("Edição do Item ID: " + itemId + " (Perfil: " + this.userProfile + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        
        // --- Setup e Carregamento (ANTES DE INICIALIZAR COMPONENTES) ---
        try {
            if (!carregarDadosExternos()) {
                JOptionPane.showMessageDialog(this, "Item ID " + itemId + " não encontrado.", "Erro de Carga", JOptionPane.ERROR_MESSAGE);
                dispose(); 
                return;
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro crítico ao carregar dados: " + e.getMessage(), "Erro de DB", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // --- Inicialização de Componentes e Estilo ---
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        Color CLR_LARANJA_EDICAO = new Color(255, 165, 0);
        Font FNT_PADRAO = new Font("Tahoma", Font.BOLD, 12);
        
        getContentPane().setBackground(CLR_FUNDO_ESCURO);
        getContentPane().setLayout(null);
        
        int yOffset = 30;

        // --- INICIALIZAÇÃO E POSICIONAMENTO DOS COMPONENTES VISUAIS ---
        
        JLabel lblNomeItem = new JLabel("Nome do Item:"); lblNomeItem.setFont(FNT_PADRAO); lblNomeItem.setForeground(CLR_BRANCO_CLARO); lblNomeItem.setBounds(50, yOffset, 150, 20); getContentPane().add(lblNomeItem);
        txtNomeItem = new JTextField(); txtNomeItem.setBackground(CLR_BRANCO_CLARO); txtNomeItem.setBounds(250, yOffset, 200, 25); getContentPane().add(txtNomeItem);
        yOffset += 40;

        JLabel lblCategoria = new JLabel("Categoria:"); lblCategoria.setFont(FNT_PADRAO); lblCategoria.setForeground(CLR_BRANCO_CLARO); lblCategoria.setBounds(50, yOffset, 150, 20); getContentPane().add(lblCategoria);
        cmbCategoria = new JComboBox<>(); 
        popularComboBoxCategorias(); // Popula o JComboBox
        cmbCategoria.setBackground(CLR_BRANCO_CLARO); cmbCategoria.setBounds(250, yOffset, 200, 25); getContentPane().add(cmbCategoria);
        yOffset += 40;
        
        JLabel lblDescricao = new JLabel("Descrição (Opcional):"); lblDescricao.setFont(FNT_PADRAO); lblDescricao.setForeground(CLR_BRANCO_CLARO); lblDescricao.setBounds(50, yOffset, 200, 20); getContentPane().add(lblDescricao);
        txtDescricao = new JTextArea(); txtDescricao.setWrapStyleWord(true); txtDescricao.setLineWrap(true); txtDescricao.setBackground(CLR_BRANCO_CLARO);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao); scrollDescricao.setBounds(50, yOffset + 25, 400, 80); getContentPane().add(scrollDescricao);
        yOffset += 115;

        JLabel lblQtdAtual = new JLabel("Quantidade Atual:"); lblQtdAtual.setFont(FNT_PADRAO); lblQtdAtual.setForeground(CLR_BRANCO_CLARO); lblQtdAtual.setBounds(50, yOffset, 150, 20); getContentPane().add(lblQtdAtual);
        spinQtdAtual = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1)); spinQtdAtual.setBounds(250, yOffset, 60, 25); getContentPane().add(spinQtdAtual);
        yOffset += 40;

        JLabel lblQtdMinima = new JLabel("Quantidade Mínima:"); lblQtdMinima.setFont(FNT_PADRAO); lblQtdMinima.setForeground(CLR_BRANCO_CLARO); lblQtdMinima.setBounds(50, yOffset, 150, 20); getContentPane().add(lblQtdMinima);
        spinQtdMinima = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1)); spinQtdMinima.setBounds(250, yOffset, 60, 25); getContentPane().add(spinQtdMinima);
        yOffset += 40;

        JLabel lblUnidadeMedida = new JLabel("Unidade de Medida:"); lblUnidadeMedida.setFont(FNT_PADRAO); lblUnidadeMedida.setForeground(CLR_BRANCO_CLARO); lblUnidadeMedida.setBounds(50, yOffset, 150, 20); getContentPane().add(lblUnidadeMedida);
        txtUnidadeMedida = new JTextField(); txtUnidadeMedida.setBackground(CLR_BRANCO_CLARO); txtUnidadeMedida.setBounds(250, yOffset, 200, 25); getContentPane().add(txtUnidadeMedida);
        yOffset += 40;

        JLabel lblDataValidade = new JLabel("Data de Validade (AAAA-MM-DD):"); lblDataValidade.setFont(FNT_PADRAO); lblDataValidade.setForeground(CLR_BRANCO_CLARO); lblDataValidade.setBounds(50, yOffset, 200, 20); getContentPane().add(lblDataValidade);
        txtDataValidade = new JTextField(); txtDataValidade.setBackground(CLR_BRANCO_CLARO); txtDataValidade.setBounds(250, yOffset, 200, 25); getContentPane().add(txtDataValidade);
        yOffset += 40;

        JLabel lblSetor = new JLabel("Setor:"); lblSetor.setFont(FNT_PADRAO); lblSetor.setForeground(CLR_BRANCO_CLARO); lblSetor.setBounds(50, yOffset, 150, 20); getContentPane().add(lblSetor);
        cmbSetor = new JComboBox<>(); cmbSetor.setBackground(CLR_BRANCO_CLARO); cmbSetor.setBounds(250, yOffset, 200, 25); setupSetorComboBox(this.userProfile); getContentPane().add(cmbSetor);
        yOffset += 60;
        
        // Botão de Edição (Salvar)
        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14)); btnSalvar.setForeground(Color.WHITE); btnSalvar.setBackground(CLR_LARANJA_EDICAO); btnSalvar.setOpaque(true); btnSalvar.setContentAreaFilled(true);
        btnSalvar.setBounds(50, 500, 400, 35);
        btnSalvar.addActionListener(e -> {
            salvarAlteracoes(); // Chama a lógica de UPDATE
        });
        getContentPane().add(btnSalvar);
        
        // Botão Cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FNT_PADRAO); btnCancelar.setForeground(CLR_BRANCO_CLARO); btnCancelar.setBackground(CLR_FUNDO_ESCURO.brighter()); btnCancelar.setOpaque(true); btnCancelar.setContentAreaFilled(true);
        btnCancelar.setBounds(50, 545, 400, 30);
        btnCancelar.addActionListener(e -> dispose());
        getContentPane().add(btnCancelar);

        // ** POPULA OS CAMPOS APÓS INICIALIZAÇÃO **
        populaCamposDaInterface();
    }
    
    // --- Lógica de Carregamento e Persistência ---

    private boolean carregarDadosExternos() throws SQLException, IOException {
        CategoriaService serviceCat = new CategoriaService();
        ItemService serviceItem = new ItemService();
        
        // 1. Carrega Categorias
        List<Categoria> categorias = serviceCat.buscarTodasCategorias();
        for (Categoria cat : categorias) {
            String nomeCompleto = cat.getNomeCategoria();
            categoriaMap.put(nomeCompleto, cat.getIdCategoria()); // Nome -> ID
            categoriaIdToNameMap.put(cat.getIdCategoria(), nomeCompleto); // ID -> Nome
        }
        
        // 2. Carrega Item Original
        this.itemOriginal = serviceItem.buscarItemPorID(String.valueOf(itemId));
        
        return this.itemOriginal != null;
    }

    private void popularComboBoxCategorias() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        for (String nome : categoriaMap.keySet()) {
            model.addElement(nome);
        }
        
        if (model.getSize() == 0) {
             model.addElement("Nenhuma categoria cadastrada");
             cmbCategoria.setEnabled(false);
        }
        
        cmbCategoria.setModel(model);
    }
    
    // ** CORRIGIDO: POPULAÇÃO COMPLETA DOS CAMPOS **
    private void populaCamposDaInterface() {
        if (itemOriginal == null) return;
        
        txtNomeItem.setText(itemOriginal.getNomeItem());
        txtUnidadeMedida.setText(itemOriginal.getUnidadeMedidaItem());
        
        String validade = itemOriginal.getValidadeItem();
        txtDataValidade.setText(validade != null ? validade : ""); 
        
        txtDescricao.setText(itemOriginal.getDescricaoItem());
        
        spinQtdAtual.setValue(itemOriginal.getQuantidadeAtualItem());
        spinQtdMinima.setValue(itemOriginal.getQuantidadeMinimaItem());
        
        String idCategoria = itemOriginal.getCategoria();
        String nomeCategoria = categoriaIdToNameMap.get(idCategoria); 
        
        if (nomeCategoria != null && cmbCategoria != null) {
            cmbCategoria.setSelectedItem(nomeCategoria);
        }
        
        if (cmbSetor != null) {
            cmbSetor.setSelectedItem(itemOriginal.getSetorItem());
        }
    }

    // ** CORRIGIDO: COLETA COMPLETA DOS DADOS PARA SALVAR **
    private void salvarAlteracoes() {
        if (itemOriginal == null) return;

        // 1. Coleta e Mapeamento
        String nome = txtNomeItem.getText().trim();
        String unidade = txtUnidadeMedida.getText().trim();
        String validade = txtDataValidade.getText().trim(); 
        String descricao = txtDescricao.getText().trim();
        
        String nomeCategoriaSelecionada = (String) cmbCategoria.getSelectedItem();
        String setor = (String) cmbSetor.getSelectedItem();
        int qtdAtual = (Integer) spinQtdAtual.getValue();
        int qtdMinima = (Integer) spinQtdMinima.getValue();
        
        String idCategoria = categoriaMap.get(nomeCategoriaSelecionada); 
        
        if (nome.isEmpty() || idCategoria == null || unidade.isEmpty() || setor == null || qtdAtual < 0) {
             JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
             return;
        }

        // 2. Cria a Entidade Atualizada
        Item itemAtualizado = new Item();
        
        itemAtualizado.setIdItem(itemOriginal.getIdItem()); // ID CHAVE
        itemAtualizado.setNomeItem(nome);
        itemAtualizado.setDescricaoItem(descricao);
        itemAtualizado.setQuantidadeAtualItem(qtdAtual);
        itemAtualizado.setQuantidadeMinimaItem(qtdMinima);
        itemAtualizado.setUnidadeMedidaItem(unidade);
        itemAtualizado.setValidadeItem(validade);
        itemAtualizado.setSetorItem(setor);
        itemAtualizado.setDataCriacaoItem(itemOriginal.getDataCriacaoItem()); // Mantém a data original
        
        itemAtualizado.setCategoria(idCategoria); // Seta o ID da Categoria
        
        // 3. Serviço de Atualização
        ItemService service = new ItemService();
        try {
            if (service.atualizarItem(itemAtualizado)) {
                JOptionPane.showMessageDialog(this, "Item ID " + itemId + " atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.parentWindow.refreshTable(); // Atualiza a tabela principal
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar item.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro de DB: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void setupSetorComboBox(String userProfile) {
        if ("AMBOS".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"AUTONOMOS", "COMBATE", "AMBOS"}));
        } else if ("AUTONOMOS".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"AUTONOMOS", "AMBOS"}));
            cmbSetor.setEnabled(false);
        } else if ("COMBATE".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"COMBATE", "AMBOS"}));
            cmbSetor.setEnabled(false);
        }
    }
}