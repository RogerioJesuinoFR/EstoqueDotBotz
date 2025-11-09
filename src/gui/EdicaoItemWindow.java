package gui;

import javax.swing.*;
import java.awt.*;

public class EdicaoItemWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtNomeItem;
    private JComboBox<String> cmbCategoria;
    private JComboBox<String> cmbSetor;
    private JTextArea txtDescricao;
    private JSpinner spinQtdAtual;
    private JSpinner spinQtdMinima;
    private int itemId; 
    
    private String[] categoriasExemplo = {"Ferramentas", "Componentes", "Armamento Leve", "Medicamentos"};

    public EdicaoItemWindow(int id, String profile) {
        this.itemId = id;
        
        setTitle("Edição do Item ID: " + id + " (Perfil: " + profile + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650); // Ajuste de tamanho
        setLocationRelativeTo(null);
        
        // --- Estilo ---
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        Color CLR_LARANJA_EDICAO = new Color(255, 165, 0);
        Font FNT_PADRAO = new Font("Tahoma", Font.BOLD, 12);
        
        getContentPane().setBackground(CLR_FUNDO_ESCURO);
        getContentPane().setLayout(null);
        
        // Simulação de carregamento de dados (preenche os campos)
        loadItemData(id); 

        int yOffset = 30;
        
        // --- Componentes (Campos de formulário completo, omitidos para concisão visual) ---
        // ... (Implementação dos componentes é a mesma do ItemCadastroWindow)
        
        JLabel lblNomeItem = new JLabel("Nome do Item:"); lblNomeItem.setFont(FNT_PADRAO); lblNomeItem.setForeground(CLR_BRANCO_CLARO); lblNomeItem.setBounds(50, yOffset, 150, 20); getContentPane().add(lblNomeItem);
        txtNomeItem = new JTextField("Nome Atual - ID " + id); txtNomeItem.setBackground(CLR_BRANCO_CLARO); txtNomeItem.setBounds(250, yOffset, 200, 25); getContentPane().add(txtNomeItem);
        yOffset += 40;

        // Botão de Edição (Salvar)
        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14)); btnSalvar.setForeground(Color.WHITE); btnSalvar.setBackground(CLR_LARANJA_EDICAO); btnSalvar.setOpaque(true); btnSalvar.setContentAreaFilled(true);
        btnSalvar.setBounds(50, 500, 400, 35);
        btnSalvar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Item ID " + id + " editado com sucesso (Simulação).", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        getContentPane().add(btnSalvar);
        
        // Botão Cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FNT_PADRAO); btnCancelar.setForeground(CLR_BRANCO_CLARO); btnCancelar.setBackground(CLR_FUNDO_ESCURO.brighter()); btnCancelar.setOpaque(true); btnCancelar.setContentAreaFilled(true);
        btnCancelar.setBounds(50, 545, 400, 30);
        btnCancelar.addActionListener(e -> dispose());
        getContentPane().add(btnCancelar);
    }
    
    private void loadItemData(int id) {
        // Lógica real de SELECT do MariaDB para preencher os campos
        System.out.println("DEBUG: Carregando dados do Item ID: " + id);
    }
}