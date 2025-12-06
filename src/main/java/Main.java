import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import telas.TelaLogin;

/**
 * Classe principal de inicialização.
 * Configura o LookAndFeel e abre a tela de Login.
 */
public class Main {
    public static void main(String[] args) {
        // Tenta deixar a interface com a cara do sistema operacional (Windows/Mac/Linux)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se falhar, usa o padrão do Java
        }

        // Inicia a aplicação na Thread de Eventos do Swing (Safe Thread)
        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}