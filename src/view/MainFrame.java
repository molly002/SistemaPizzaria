package view;

import dao.ClienteDAO;
import dao.FuncionarioDAO;
import model.Cliente;
import model.Funcionario;
import util.Sessao;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Pizzaria - Sistema de Gestão (admin: " + Sessao.admin.getNome() + ")");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Pedidos", new PedidoPanel());
        abas.addTab("Clientes", new PessoaPanel<Cliente>("Cliente", new ClienteDAO(), Cliente::new));
        abas.addTab("Cozinha", new PessoaPanel<Funcionario>("Funcionário", new FuncionarioDAO(), Funcionario::new));
        abas.addTab("Sabores", new SaborPanel());
        abas.addTab("Tamanhos", new TamanhoPanel());
        abas.addTab("Preços", new PrecoPanel());
        abas.addTab("Avaliações", new AvaliacaoPanel());
        abas.addTab("Relatório", new RelatorioPanel());

        // Recarrega os dados toda vez que o usuário muda de aba
        abas.addChangeListener(e -> {
            if (abas.getSelectedComponent() instanceof Atualizavel) {
                ((Atualizavel) abas.getSelectedComponent()).atualizar();
            }
        });
        add(abas);

        JMenuBar menu = new JMenuBar();
        JMenu arq = new JMenu("Arquivo");
        JMenuItem sair = new JMenuItem("Sair (logout)");
        sair.addActionListener(e -> {
            Sessao.admin = null;
            new LoginFrame().setVisible(true);
            dispose();
        });
        arq.add(sair);
        menu.add(arq);
        setJMenuBar(menu);
    }
}
