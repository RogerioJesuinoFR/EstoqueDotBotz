package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import java.io.IOException;
import java.sql.SQLException;
import entities.Categoria;
import services.CategoriaService;

public class CategoriaEdicaoWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtNomeCategoria;
    private JTextArea txtDescricao;
    private JComboBox<String> cmbSetor;
    private MainCategoriasWindow parentWindow;
    private String userProfile;
    private Categoria categoriaOriginal; // Armazena os dados carregados

    public CategoriaEdicaoWindow(String id, String profile, MainCategoriasWindow parent) {
        this.parentWindow = parent;
        this.userProfile = profile.toUpperCase();
        
        setTitle("Edição da Categoria ID: " + id);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 450); 
        setLocationRelativeTo(null);
        
        // Carrega os dados ANTES de criar os componentes
        if (!carregarDadosExternos(id)) {
            dispose();
            return;
        }
        
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        Color CLR_LARANJA_EDICAO = new Color(255, 165, 0);
        Font FNT_PADRAO = new Font("Tahoma", Font.BOLD, 12);
        
        getContentPane().setBackground(CLR_FUNDO_ESCURO);
        getContentPane().setLayout(null);
        
        int yOffset = 30;
        
        // --- Componentes ---
        JLabel lblNomeCategoria = new JLabel("Nome da Categoria:"); lblNomeCategoria.setFont(FNT_PADRAO); lblNomeCategoria.setForeground(CLR_BRANCO_CLARO); lblNomeCategoria.setBounds(30, yOffset, 150, 20); getContentPane().add(lblNomeCategoria);
        txtNomeCategoria = new JTextField(); txtNomeCategoria.setBackground(CLR_BRANCO_CLARO); txtNomeCategoria.setBounds(190, yOffset, 220, 25); getContentPane().add(txtNomeCategoria);
        yOffset += 40;

        JLabel lblSetor = new JLabel("Setor de Acesso:"); lblSetor.setFont(FNT_PADRAO); lblSetor.setForeground(CLR_BRANCO_CLARO); lblSetor.setBounds(30, yOffset, 150, 20); getContentPane().add(lblSetor);
        cmbSetor = new JComboBox<>(); cmbSetor.setBackground(CLR_BRANCO_CLARO); cmbSetor.setBounds(190, yOffset, 220, 25);
        setupSetorComboBox(this.userProfile); // Configura as opções
        getContentPane().add(cmbSetor);
        yOffset += 50;

        JLabel lblDescricao = new JLabel("Descrição da Categoria:"); lblDescricao.setFont(FNT_PADRAO); lblDescricao.setForeground(CLR_BRANCO_CLARO); lblDescricao.setBounds(30, yOffset, 200, 20); getContentPane().add(lblDescricao);
        txtDescricao = new JTextArea(); txtDescricao.setWrapStyleWord(true); txtDescricao.setLineWrap(true); txtDescricao.setBackground(CLR_BRANCO_CLARO);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao); scrollDescricao.setBounds(30, yOffset + 25, 380, 100); getContentPane().add(scrollDescricao);
        yOffset += 150; 

        // Botão de Edição
        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14)); btnSalvar.setForeground(Color.WHITE); btnSalvar.setBackground(CLR_LARANJA_EDICAO); btnSalvar.setOpaque(true); btnSalvar.setContentAreaFilled(true);
        btnSalvar.setBounds(30, yOffset, 380, 35);
        btnSalvar.addActionListener(e -> {
            salvarAlteracoes();
        });
        getContentPane().add(btnSalvar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FNT_PADRAO); btnCancelar.setForeground(CLR_BRANCO_CLARO); btnCancelar.setBackground(CLR_FUNDO_ESCURO.brighter()); btnCancelar.setOpaque(true); btnCancelar.setContentAreaFilled(true);
        btnCancelar.setBounds(30, yOffset + 40, 380, 30);
        btnCancelar.addActionListener(e -> dispose());
        getContentPane().add(btnCancelar);
        
        // Popula os campos com os dados carregados
        populaCamposDaInterface();
    }
    
    private boolean carregarDadosExternos(String id) {
        CategoriaService service = new CategoriaService();
        try {
            this.categoriaOriginal = service.buscarPorID(id);
            if (this.categoriaOriginal == null) {
                 JOptionPane.showMessageDialog(this, "Categoria não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                 return false;
            }
            return true;
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados da categoria: " + e.getMessage(), "Erro de DB", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void populaCamposDaInterface() {
        if (categoriaOriginal == null) return;
        txtNomeCategoria.setText(categoriaOriginal.getNomeCategoria());
        txtDescricao.setText(categoriaOriginal.getDescricaoCategoria());
        cmbSetor.setSelectedItem(categoriaOriginal.getSetor());
    }

    private void salvarAlteracoes() {
        String nome = txtNomeCategoria.getText().trim();
        String descricao = txtDescricao.getText().trim();
        String setor = (String) cmbSetor.getSelectedItem();
        
        if (nome.isEmpty()) {
             JOptionPane.showMessageDialog(this, "O nome da categoria é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
             return;
        }

        // Monta o objeto atualizado
        Categoria categoriaAtualizada = new Categoria(
            categoriaOriginal.getIdCategoria(), // ID CHAVE
            nome,
            descricao,
            categoriaOriginal.getDataCriacaoCategoria(), // Mantém data original
            setor
        );
        
        CategoriaService service = new CategoriaService();
        try {
            if (service.atualizarCategoria(categoriaAtualizada)) {
                JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!");
                parentWindow.refreshTable(); // Atualiza a tabela principal
                dispose();
            } else {
                 JOptionPane.showMessageDialog(this, "Falha ao atualizar categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro de DB: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void setupSetorComboBox(String userProfile) {
        if ("AMBOS".equals(userProfile)) {
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {"AUTONOMOS", "COMBATE", "AMBOS"}));
        } else {
            // Se o usuário não for 'AMBOS', ele não pode alterar o setor
            cmbSetor.setModel(new DefaultComboBoxModel<>(new String[] {this.categoriaOriginal.getSetor()}));
            cmbSetor.setEnabled(false);
        }
    }
}