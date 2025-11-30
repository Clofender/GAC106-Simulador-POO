/**
 * Representa um búfalo na simulação - um herbívoro grande.
 * Búfalos são animais herbívoros que se alimentam de plantas e
 * podem ser predados por leões e caçadores.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class Buffalo extends Herbivore {
    // --- Constantes Estáticas ---
    private static final int BREEDING_AGE = 15;
    private static final int MAX_AGE = 120;
    private static final double BREEDING_PROBABILITY = 0.08;
    private static final int MAX_LITTER_SIZE = 2;
    private static final int FOOD_VALUE = 20;

    /**
     * Cria um novo búfalo.
     * 
     * @param randomAge Se true, o búfalo terá idade aleatória.
     */
    public Buffalo(boolean randomAge) {
        super(randomAge);
    }

    /**
     * Cria um novo búfalo jovem.
     * 
     * @param randomAge Se true, o jovem terá idade aleatória.
     * @param field O campo onde o jovem será colocado.
     * @param loc A localização do jovem.
     * @return O búfalo jovem criado.
     */
    @Override
    public Animal createYoung(boolean randomAge, Field field, Location loc) {
        Buffalo young = new Buffalo(randomAge);
        young.setLocation(loc);
        field.place(young, loc);
        return young;
    }

    /**
     * Retorna o valor nutricional deste animal quando for comido.
     * 
     * @return O valor nutricional do búfalo.
     */
    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }

    /**
     * Retorna a idade máxima que um búfalo pode atingir.
     * 
     * @return A idade máxima do búfalo.
     */
    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Retorna a idade em que um búfalo pode começar a procriar.
     * 
     * @return A idade de procriação do búfalo.
     */
    @Override
    public int getBreedingAge() {
        return BREEDING_AGE;
    }

    /**
     * Retorna a probabilidade de um búfalo procriar em cada passo.
     * 
     * @return A probabilidade de procriação (0.0 a 1.0).
     */
    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * Retorna o tamanho máximo da ninhada de búfalos.
     * 
     * @return O número máximo de filhotes por procriação.
     */
    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
}