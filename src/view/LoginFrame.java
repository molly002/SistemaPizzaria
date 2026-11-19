package view;

import dao.AdministradorDAO;
import model.Administrador;
import util.Sessao;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final JTextField txtUsuario = new JTextField(18);
    private final JPasswordField txtSenha = new JPasswordField(18);

    public LoginFrame() {
        super("Pizzaria - Login do administrador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Usuário:"), g);
        g.gridx = 1; p.add(txtUsuario, g);
        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Senha:"), g);
        g.gridx = 1; p.add(txtSenha, g);
        JButton btEntrar = new JButton("Entrar");
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.fill = GridBagConstraints.HORIZONTAL;
        p.add(btEntrar, g);
        add(p);
        getRootPane().setDefaultButton(btEntrar);
        btEntrar.addActionListener(e -> entrar());
        pack();
        setLocationRelativeTo(null);
    }

    private void entrar() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());
        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe usuário e senha.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Administrador adm = new AdministradorDAO().autenticar(usuario, senha);
            if (adm == null) {
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.", "Login", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Sessao.admin = adm;
            new MainFrame().setVisible(true);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível conectar ao MySQL.\nVerifique se o banco 'pizzaria' foi criado e se usuário/senha em util/Conexao.java estão corretos.\n\n"
                            + ex.getMessage(), "Erro de conexão", JOptionPane.ERROR_MESSAGE);
        }
    }
}
