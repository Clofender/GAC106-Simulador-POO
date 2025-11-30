import javax.swing.SwingUtilities;

/**
 * Classe principal que inicia o simulador de ecossistema.
 * Pode iniciar diretamente a simulação ou abrir o menu inicial.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class Principal {

    /**
     * Método principal da aplicação.
     * Pode iniciar em modo direto ou com menu.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        // Modo com menu inicial (recomendado)
        startWithMenu();
        
        // Ou modo direto (para testes)
        // startDirectSimulation();
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