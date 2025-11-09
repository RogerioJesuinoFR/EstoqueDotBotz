package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.DefaultComboBoxModel;

public class CategoriaEdicaoWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    public CategoriaEdicaoWindow(int id, String profile) {
        setTitle("Edição da Categoria ID: " + id + " (Perfil: " + profile + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 450); 
        setLocationRelativeTo(null);
        
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        Color CLR_LARANJA_EDICAO = new Color(255, 165, 0);
        Font FNT_PADRAO = new Font("Tahoma", Font.BOLD, 12);
        
        getContentPane().setBackground(CLR_FUNDO_ESCURO);
        getContentPane().setLayout(null);
        
        // Simulação de carregamento de dados
        String nomeAtual = "Nome Categoria ID " + id;
        String setorAtual = "Ambos"; // Simulação: valor vindo do DB
        String descricaoAtual = "Descrição atual da categoria.";

        int yOffset = 30;
        
        // --- Componentes ---
        // Nome da Categoria
        JLabel lblNomeCategoria = new JLabel("Nome da Categoria:"); lblNomeCategoria.setFont(FNT_PADRAO); lblNomeCategoria.setForeground(CLR_BRANCO_CLARO); lblNomeCategoria.setBounds(30, yOffset, 150, 20); getContentPane().add(lblNomeCategoria);
        JTextField txtNomeCategoria = new JTextField(nomeAtual); txtNomeCategoria.setBackground(CLR_BRANCO_CLARO); txtNomeCategoria.setBounds(190, yOffset, 220, 25); getContentPane().add(txtNomeCategoria);
        yOffset += 40;

        // Setor de Acesso
        JLabel lblSetor = new JLabel("Setor de Acesso:"); lblSetor.setFont(FNT_PADRAO); lblSetor.setForeground(CLR_BRANCO_CLARO); lblSetor.setBounds(30, yOffset, 150, 20); getContentPane().add(lblSetor);
        JComboBox<String> cmbSetor = new JComboBox<>(); cmbSetor.setBackground(CLR_BRANCO_CLARO); cmbSetor.setBounds(190, yOffset, 220, 25);
        if ("Ambos".equals(profile)) { cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"Autônomos", "Combate", "Ambos"})); cmbSetor.setSelectedItem(setorAtual); cmbSetor.setEnabled(true); } 
        else { cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {profile})); cmbSetor.setSelectedItem(profile); cmbSetor.setEnabled(false); }
        getContentPane().add(cmbSetor);
        yOffset += 50;

        // Descrição
        JLabel lblDescricao = new JLabel("Descrição da Categoria:"); lblDescricao.setFont(FNT_PADRAO); lblDescricao.setForeground(CLR_BRANCO_CLARO); lblDescricao.setBounds(30, yOffset, 200, 20); getContentPane().add(lblDescricao);
        JTextArea txtDescricao = new JTextArea(descricaoAtual); txtDescricao.setWrapStyleWord(true); txtDescricao.setLineWrap(true); txtDescricao.setBackground(CLR_BRANCO_CLARO);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao); scrollDescricao.setBounds(30, yOffset + 25, 380, 100); getContentPane().add(scrollDescricao);
        yOffset += 150; 

        // Botão de Edição
        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14)); btnSalvar.setForeground(Color.WHITE); btnSalvar.setBackground(CLR_LARANJA_EDICAO); btnSalvar.setOpaque(true); btnSalvar.setContentAreaFilled(true);
        btnSalvar.setBounds(30, yOffset, 380, 35);
        btnSalvar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Categoria ID " + id + " editada com sucesso (Simulação).", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        getContentPane().add(btnSalvar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FNT_PADRAO); btnCancelar.setForeground(CLR_BRANCO_CLARO); btnCancelar.setBackground(CLR_FUNDO_ESCURO.brighter()); btnCancelar.setOpaque(true); btnCancelar.setContentAreaFilled(true);
        btnCancelar.setBounds(30, yOffset + 40, 380, 30);
        btnCancelar.addActionListener(e -> dispose());
        getContentPane().add(btnCancelar);
    }
}