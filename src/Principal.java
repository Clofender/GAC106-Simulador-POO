import javax.swing.SwingUtilities;

/**
 * Classe principal que inicia o simulador de ecossistema.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class Principal {

    /**
     * Método principal da aplicação.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        startWithMenu();
    }

    /**
     * Inicia a aplicação com menu inicial.
     */
    private static void startWithMenu() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu();
            }
        });
    }
}