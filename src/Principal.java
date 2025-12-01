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
     * Ponto único de entrada do programa.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        startWithMenu(); // Inicia aplicação com menu gráfico
    }

    /**
     * Inicia a aplicação com menu inicial.
     */
    private static void startWithMenu() {
        // Executa na EDT (Event Dispatch Thread) do Swing para segurança
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu(); // Cria e exibe o menu principal
            }
        });
    }
}