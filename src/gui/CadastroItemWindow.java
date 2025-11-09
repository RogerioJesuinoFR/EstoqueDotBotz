package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;

public class CadastroItemWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtNomeItem;
    private JTextField txtUnidadeMedida;
    private JTextField txtDataValidade;
    private JComboBox<String> cmbCategoria;
    private JComboBox<String> cmbSetor;
    private JTextArea txtDescricao;
    private JSpinner spinQtdAtual;
    private JSpinner spinQtdMinima;
    
    private String[] categoriasExemplo = {"Ferramentas", "Componentes", "Armamento Leve", "Medicamentos"};

    public CadastroItemWindow(String profile) {
        setTitle("Cadastro de Novo Item (Perfil: " + profile + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600); // Tamanho ajustado
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
        // Nome do Item
        JLabel lblNomeItem = new JLabel("Nome do Item:"); lblNomeItem.setFont(FNT_PADRAO); lblNomeItem.setForeground(CLR_BRANCO_CLARO); lblNomeItem.setBounds(50, yOffset, 150, 20); getContentPane().add(lblNomeItem);
        txtNomeItem = new JTextField(); txtNomeItem.setBackground(CLR_BRANCO_CLARO); txtNomeItem.setBounds(250, yOffset, 200, 25); getContentPane().add(txtNomeItem);
        yOffset += 40;

        // Categoria
        JLabel lblCategoria = new JLabel("Categoria:"); lblCategoria.setFont(FNT_PADRAO); lblCategoria.setForeground(CLR_BRANCO_CLARO); lblCategoria.setBounds(50, yOffset, 150, 20); getContentPane().add(lblCategoria);
        cmbCategoria = new JComboBox<>(); cmbCategoria.setModel(new DefaultComboBoxModel<>(categoriasExemplo)); cmbCategoria.setBackground(CLR_BRANCO_CLARO); cmbCategoria.setBounds(250, yOffset, 200, 25); getContentPane().add(cmbCategoria);
        yOffset += 40;

        // Descrição
        JLabel lblDescricao = new JLabel("Descrição (Opcional):"); lblDescricao.setFont(FNT_PADRAO); lblDescricao.setForeground(CLR_BRANCO_CLARO); lblDescricao.setBounds(50, yOffset, 200, 20); getContentPane().add(lblDescricao);
        txtDescricao = new JTextArea(); txtDescricao.setWrapStyleWord(true); txtDescricao.setLineWrap(true); txtDescricao.setBackground(CLR_BRANCO_CLARO);
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
        JLabel lblDataValidade = new JLabel("Data de Validade (DD/MM/AAAA):"); lblDataValidade.setFont(FNT_PADRAO); lblDataValidade.setForeground(CLR_BRANCO_CLARO); lblDataValidade.setBounds(50, yOffset, 200, 20); getContentPane().add(lblDataValidade);
        txtDataValidade = new JTextField(); txtDataValidade.setBackground(CLR_BRANCO_CLARO); txtDataValidade.setBounds(250, yOffset, 200, 25); getContentPane().add(txtDataValidade);
        yOffset += 40;

        // Setor (Lógica Condicional)
        JLabel lblSetor = new JLabel("Setor:"); lblSetor.setFont(FNT_PADRAO); lblSetor.setForeground(CLR_BRANCO_CLARO); lblSetor.setBounds(50, yOffset, 150, 20); getContentPane().add(lblSetor);
        cmbSetor = new JComboBox<>(); cmbSetor.setBackground(CLR_BRANCO_CLARO); cmbSetor.setBounds(250, yOffset, 200, 25); setupSetorComboBox(profile); getContentPane().add(cmbSetor);
        yOffset += 60;

        // Botão de Cadastro
        JButton btnCadastrar = new JButton("Adicionar Item ao Estoque");
        btnCadastrar.setFont(FNT_PADRAO); btnCadastrar.setForeground(Color.WHITE); btnCadastrar.setBackground(CLR_AZUL_CADASTRO); btnCadastrar.setOpaque(true); btnCadastrar.setContentAreaFilled(true);
        btnCadastrar.setBounds(50, yOffset, 400, 35);
        btnCadastrar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Item cadastrado com sucesso (Simulação).", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        getContentPane().add(btnCadastrar);
    }
    
    private void setupSetorComboBox(String userProfile) {
        if ("Ambos".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"Autônomos", "Combate", "Ambos"}));
            cmbSetor.setEnabled(true);
        } else if ("Autônomos".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"Autônomos"}));
            cmbSetor.setEnabled(false);
        } else if ("Combate".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"Combate"}));
            cmbSetor.setEnabled(false);
        }
    }
}