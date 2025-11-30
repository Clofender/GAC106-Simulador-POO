import java.util.Random;

/**
 * Classe utilitária para fornecer uma instância única de Random.
 * Evita duplicação de instâncias Random em toda a aplicação.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class RandomGenerator {
    private static final Random rand = new Random();
    
    /**
     * Obtém a instância compartilhada de Random.
     *
     * @return A instância única de Random.
     */
    public static Random getRandom() {
        return rand;
    }
    
    /**
     * Gera um número inteiro aleatório no intervalo [0, bound).
     *
     * @param bound O limite superior (exclusivo).
     * @return Um número aleatório.
     */
    public static int nextInt(int bound) {
        return rand.nextInt(bound);
    }
    
    /**
     * Gera um número double aleatório entre 0.0 e 1.0.
     *
     * @return Um número double aleatório.
     */
    public static double nextDouble() {
        return rand.nextDouble();
    }
}