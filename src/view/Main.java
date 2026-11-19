package view;

import javax.swing.*;

/** Ponto de entrada do sistema. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new LoginFrame().setVisible(true);
        });
    }
}
