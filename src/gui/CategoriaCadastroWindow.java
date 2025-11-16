package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import java.io.IOException;
import java.sql.SQLException;
import entities.Categoria;
import services.CategoriaService;

public class CategoriaCadastroWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea txtDescricao;
    private JTextField txtNomeCategoria;
    private JComboBox<String> cmbSetor;
    private MainCategoriasWindow parentWindow; // Referência para refresh

    public CategoriaCadastroWindow(String profile, MainCategoriasWindow parent) {
        this.parentWindow = parent;
        String userProfile = profile.toUpperCase();
        
        setTitle("Cadastro de Nova Categoria (Perfil: " + userProfile + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400); 
        setLocationRelativeTo(null);
        
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        Color CLR_AZUL_CADASTRO = new Color(51, 153, 255);
        Font FNT_PADRAO = new Font("Tahoma", Font.BOLD, 12);
        
        getContentPane().setBackground(CLR_FUNDO_ESCURO);
        getContentPane().setLayout(null);
        
        int yOffset = 30;
        
        // Nome da Categoria
        JLabel lblNomeCategoria = new JLabel("Nome da Categoria:"); lblNomeCategoria.setFont(FNT_PADRAO); lblNomeCategoria.setForeground(CLR_BRANCO_CLARO); lblNomeCategoria.setBounds(30, yOffset, 150, 20); getContentPane().add(lblNomeCategoria);
        txtNomeCategoria = new JTextField(); txtNomeCategoria.setBackground(CLR_BRANCO_CLARO); txtNomeCategoria.setBounds(190, yOffset, 220, 25); getContentPane().add(txtNomeCategoria);
        yOffset += 40;

        // Setor de Acesso (Condicional)
        JLabel lblSetor = new JLabel("Setor de Acesso:"); lblSetor.setFont(FNT_PADRAO); lblSetor.setForeground(CLR_BRANCO_CLARO); lblSetor.setBounds(30, yOffset, 150, 20); getContentPane().add(lblSetor);
        cmbSetor = new JComboBox<>(); cmbSetor.setBackground(CLR_BRANCO_CLARO); cmbSetor.setBounds(190, yOffset, 220, 25);
        
        // Lógica de Setor (ENUMS MAIÚSCULOS)
        if ("AMBOS".equals(userProfile)) { 
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"AUTONOMOS", "COMBATE", "AMBOS"}));
            cmbSetor.setEnabled(true); 
        } else { 
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {userProfile})); // AUTONOMOS ou COMBATE
            cmbSetor.setEnabled(false); 
        }
        getContentPane().add(cmbSetor);
        yOffset += 50;

        // Descrição
        JLabel lblDescricao = new JLabel("Descrição da Categoria:"); lblDescricao.setFont(FNT_PADRAO); lblDescricao.setForeground(CLR_BRANCO_CLARO); lblDescricao.setBounds(30, yOffset, 200, 20); getContentPane().add(lblDescricao);
        txtDescricao = new JTextArea(); txtDescricao.setWrapStyleWord(true); txtDescricao.setLineWrap(true); txtDescricao.setBackground(CLR_BRANCO_CLARO);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao); scrollDescricao.setBounds(30, yOffset + 25, 380, 100); getContentPane().add(scrollDescricao);
        yOffset += 140; 

        // Botão de Cadastro
        JButton btnCadastrar = new JButton("Salvar Nova Categoria");
        btnCadastrar.setFont(FNT_PADRAO); btnCadastrar.setForeground(Color.WHITE); btnCadastrar.setBackground(CLR_AZUL_CADASTRO); btnCadastrar.setOpaque(true); btnCadastrar.setContentAreaFilled(true);
        btnCadastrar.setBounds(30, yOffset, 380, 35);
        
        // AÇÃO: PERSISTÊNCIA E REFRESH
        btnCadastrar.addActionListener(e -> {
            efetuarCadastroCategoria();
        });
        getContentPane().add(btnCadastrar);
    }
    
    private void efetuarCadastroCategoria() {
        String nome = txtNomeCategoria.getText().trim();
        String descricao = txtDescricao.getText().trim();
        String setor = (String) cmbSetor.getSelectedItem();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da categoria é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ID e Data de Criação são nulos (gerados pelo DB)
        Categoria novaCategoria = new Categoria(null, nome, descricao, null, setor);
        
        CategoriaService service = new CategoriaService();
        try {
            if (service.cadastrarCategoria(novaCategoria)) {
                JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso!");
                parentWindow.refreshTable(); // Atualiza a tabela principal
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao cadastrar categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro de DB: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}