package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.sql.SQLException;

// NECESSÁRIO PARA INTEGRAÇÃO DB
import services.UsuarioService;
import entities.Usuario;

public class LoginWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtfNome;
    private JTextField txtfRA;
    private JPasswordField pswfSenha;

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception e) {}
        EventQueue.invokeLater(() -> {
            try { new LoginWindow().setVisible(true); } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public LoginWindow() {
        setTitle("Sistema de Login - EstoqueDotBotz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        Color CLR_FUNDO_ESCURO = new Color(51, 51, 51);
        Color CLR_BRANCO_CLARO = new Color(241, 241, 243);
        Color CLR_AZUL_LOGIN = new Color(51, 153, 255);
        Color CLR_VERDE_CADASTRO = new Color(102, 187, 106);
        Font FNT_PADRAO = new Font("Tahoma", Font.BOLD, 12);
        
        getContentPane().setBackground(CLR_FUNDO_ESCURO);
        getContentPane().setLayout(null);
        
        // Componentes (Mantidos do código anterior)
        JLabel lblNomeLabel = new JLabel("Nome:"); lblNomeLabel.setFont(FNT_PADRAO); lblNomeLabel.setForeground(CLR_BRANCO_CLARO); lblNomeLabel.setBounds(50, 40, 100, 20); getContentPane().add(lblNomeLabel);
        JLabel lblRALabel = new JLabel("RA:"); lblRALabel.setFont(FNT_PADRAO); lblRALabel.setForeground(CLR_BRANCO_CLARO); lblRALabel.setBounds(50, 80, 100, 20); getContentPane().add(lblRALabel);
        JLabel lblSenhaLabel = new JLabel("Senha:"); lblSenhaLabel.setFont(FNT_PADRAO); lblSenhaLabel.setForeground(CLR_BRANCO_CLARO); lblSenhaLabel.setBounds(50, 120, 100, 20); getContentPane().add(lblSenhaLabel);
        
        txtfNome = new JTextField(); txtfNome.setBackground(CLR_BRANCO_CLARO); txtfNome.setBounds(150, 40, 180, 25); getContentPane().add(txtfNome); txtfNome.setColumns(10);
        txtfRA = new JTextField(); txtfRA.setBackground(CLR_BRANCO_CLARO); txtfRA.setBounds(150, 80, 180, 25); getContentPane().add(txtfRA); txtfRA.setColumns(10);
        pswfSenha = new JPasswordField(); pswfSenha.setBackground(CLR_BRANCO_CLARO); pswfSenha.setBounds(150, 120, 180, 25); getContentPane().add(pswfSenha);
        
        JButton btnLogar = new JButton("Login");
        btnLogar.setFont(FNT_PADRAO); btnLogar.setForeground(Color.WHITE); btnLogar.setBackground(CLR_AZUL_LOGIN); btnLogar.setOpaque(true); btnLogar.setContentAreaFilled(true);
        btnLogar.setBounds(50, 180, 280, 30);
        
        // **LIGAÇÃO: LOGIN -> MAIN (ITENS) com LÓGICA DB**
        btnLogar.addActionListener(e -> {
            String ra = txtfRA.getText().trim();
            String senha = new String(pswfSenha.getPassword());
            
            if (ra.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            UsuarioService service = new UsuarioService();
            
            try {
                // 1. Busca o objeto Usuario completo
                Usuario usuarioLogado = service.fazerLogin(ra, senha);
                
                if (usuarioLogado != null) {
                    // Login BEM SUCEDIDO
                    String[] categoriasExemplo = {"Ferramentas", "Armas", "Componentes"}; 
                    
                    // 2. **CORREÇÃO: Passa o objeto Usuario completo (não apenas o profile)**
                    MainWindow mainFrame = new MainWindow(categoriasExemplo, usuarioLogado);
                    mainFrame.setVisible(true);
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, "RA ou Senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro de conexão com o banco de dados.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        getContentPane().add(btnLogar);
        
        JButton btnNovoUsuario = new JButton("Novo Usuário (Cadastro)");
        btnNovoUsuario.setFont(FNT_PADRAO); btnNovoUsuario.setForeground(Color.WHITE); btnNovoUsuario.setBackground(CLR_VERDE_CADASTRO); btnNovoUsuario.setOpaque(true); btnNovoUsuario.setContentAreaFilled(true);
        btnNovoUsuario.setBounds(50, 220, 280, 30);

        // LIGAÇÃO: LOGIN -> CADASTRO
        btnNovoUsuario.addActionListener(e -> {
            new CadastroUsuarioWindow().setVisible(true);
        });
        getContentPane().add(btnNovoUsuario);
    }
}