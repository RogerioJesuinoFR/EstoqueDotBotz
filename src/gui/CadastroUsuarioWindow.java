package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.sql.SQLException;

// NECESSÁRIO PARA INTEGRAÇÃO DB
import services.UsuarioService;
import entities.Usuario;

public class CadastroUsuarioWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtfNome;
    private JTextField txtfRA;
    private JPasswordField pswfSenha;
    private JPasswordField pswfConfirmaSenha;
    private JComboBox<String> cmbOpcoes;

    public CadastroUsuarioWindow() {
        setTitle("Cadastro de Novo Usuário");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 420);
        setLocationRelativeTo(null);
        
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        Color CLR_VERDE_CADASTRO = new Color(102, 187, 106);
        Font FNT_PADRAO = new Font("Tahoma", Font.BOLD, 12);
        
        getContentPane().setBackground(CLR_FUNDO_ESCURO);
        getContentPane().setLayout(null);
        
        // --- Rótulos e Campos ---
        JLabel lblNomeLabel = new JLabel("Nome:"); lblNomeLabel.setFont(FNT_PADRAO); lblNomeLabel.setForeground(CLR_BRANCO_CLARO); lblNomeLabel.setBounds(50, 30, 150, 20); getContentPane().add(lblNomeLabel);
        JLabel lblRALabel = new JLabel("RA:"); lblRALabel.setFont(FNT_PADRAO); lblRALabel.setForeground(CLR_BRANCO_CLARO); lblRALabel.setBounds(50, 70, 150, 20); getContentPane().add(lblRALabel);
        JLabel lblSenhaLabel = new JLabel("Senha:"); lblSenhaLabel.setFont(FNT_PADRAO); lblSenhaLabel.setForeground(CLR_BRANCO_CLARO); lblSenhaLabel.setBounds(50, 110, 150, 20); getContentPane().add(lblSenhaLabel);
        JLabel lblConfirmaSenha = new JLabel("Confirmar Senha:"); lblConfirmaSenha.setFont(FNT_PADRAO); lblConfirmaSenha.setForeground(CLR_BRANCO_CLARO); lblConfirmaSenha.setBounds(50, 150, 150, 20); getContentPane().add(lblConfirmaSenha);
        JLabel lblOpcoes = new JLabel("Perfil de Acesso:"); lblOpcoes.setFont(FNT_PADRAO); lblOpcoes.setForeground(CLR_BRANCO_CLARO); lblOpcoes.setBounds(50, 190, 150, 20); getContentPane().add(lblOpcoes);

        txtfNome = new JTextField(); txtfNome.setBackground(CLR_BRANCO_CLARO); txtfNome.setBounds(190, 30, 180, 25); getContentPane().add(txtfNome); txtfNome.setColumns(10);
        txtfRA = new JTextField(); txtfRA.setBackground(CLR_BRANCO_CLARO); txtfRA.setBounds(190, 70, 180, 25); getContentPane().add(txtfRA); txtfRA.setColumns(10);
        pswfSenha = new JPasswordField(); pswfSenha.setBackground(CLR_BRANCO_CLARO); pswfSenha.setBounds(190, 110, 180, 25); getContentPane().add(pswfSenha);
        pswfConfirmaSenha = new JPasswordField(); pswfConfirmaSenha.setBackground(CLR_BRANCO_CLARO); pswfConfirmaSenha.setBounds(190, 150, 180, 25); getContentPane().add(pswfConfirmaSenha);

        cmbOpcoes = new JComboBox<>();
        cmbOpcoes.setBackground(CLR_BRANCO_CLARO); cmbOpcoes.setFont(FNT_PADRAO);
        // Agora aceita AMBOS, já que o banco foi corrigido
        cmbOpcoes.setModel(new DefaultComboBoxModel<>(new String[] {"AUTONOMOS", "COMBATE", "AMBOS"})); 
        cmbOpcoes.setBounds(190, 190, 180, 25); getContentPane().add(cmbOpcoes);
        
        // --- Botões ---
        JButton btnCadastrarFinal = new JButton("Finalizar Cadastro");
        btnCadastrarFinal.setFont(FNT_PADRAO); btnCadastrarFinal.setForeground(Color.WHITE); btnCadastrarFinal.setBackground(CLR_VERDE_CADASTRO); btnCadastrarFinal.setOpaque(true); btnCadastrarFinal.setContentAreaFilled(true);
        btnCadastrarFinal.setBounds(50, 280, 320, 35);
        
        btnCadastrarFinal.addActionListener(e -> {
            efetuarCadastro();
        });
        getContentPane().add(btnCadastrarFinal);
        
        JButton btnVoltar = new JButton("Voltar para Login");
        btnVoltar.setFont(FNT_PADRAO); btnVoltar.setForeground(CLR_BRANCO_CLARO); btnVoltar.setBackground(CLR_FUNDO_ESCURO); btnVoltar.setOpaque(true); btnVoltar.setContentAreaFilled(true);
        btnVoltar.setBounds(50, 330, 320, 30);
        
        btnVoltar.addActionListener(e -> { dispose(); });
        getContentPane().add(btnVoltar);
    }
    
    // Método de persistência
    private void efetuarCadastro() {
        String nome = txtfNome.getText().trim();
        String ra = txtfRA.getText().trim();
        String senha = new String(pswfSenha.getPassword());
        String confirmaSenha = new String(pswfConfirmaSenha.getPassword());
        String setorUsuarioDb = (String) cmbOpcoes.getSelectedItem();
        
        if (nome.isEmpty() || ra.isEmpty() || senha.isEmpty() || setorUsuarioDb == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!senha.equals(confirmaSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cria a entidade Usuario.
        Usuario novoUsuario = new Usuario(null, nome, ra, setorUsuarioDb, senha, null); 
        UsuarioService service = new UsuarioService();
        
        try {
            if (service.cadastrarNovoUsuario(novoUsuario)) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso! Use seu RA e senha para logar.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar. Verifique se o RA já está em uso.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro: O RA já está em uso ou houve um erro de DB.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception e) {}
        EventQueue.invokeLater(() -> {
            try { new CadastroUsuarioWindow().setVisible(true); } catch (Exception e) { e.printStackTrace(); }
        });
    }
}