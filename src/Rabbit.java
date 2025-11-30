/**
 * Um modelo simples de um coelho.
 * Coelhos são herbívoros pequenos que servem de presa para
 * raposas, leões e caçadores. Envelhecem, se movem, procriam e morrem.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2025
 */
public class Rabbit extends Herbivore {
    // --- Constantes Estáticas ---
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 25;
    private static final double BREEDING_PROBABILITY = 0.01;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int FOOD_VALUE = 4;

    /**
     * Cria um novo coelho.
     * 
     * @param randomAge Se true, o coelho terá idade aleatória.
     */
    public Rabbit(boolean randomAge) {
        super(randomAge);
    }

    /**
     * Cria um novo coelho jovem.
     * 
     * @param randomAge Se true, o jovem terá idade aleatória.
     * @param field O campo onde o jovem será colocado.
     * @param loc A localização do jovem.
     * @return O coelho jovem criado.
     */
    @Override
    protected Animal createYoung(boolean randomAge, Field field, Location loc) {
        Rabbit young = new Rabbit(randomAge);
        young.setLocation(loc);
        field.place(young, loc);
        return young;
    }

    /**
     * Retorna o valor nutricional deste animal quando for comido.
     * 
     * @return O valor nutricional do coelho.
     */
    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }

    /**
     * Marca o coelho como comido (morto por um predador).
     */
    public void setEaten() {
        setDead();
    }

    /**
     * Retorna a idade máxima que um coelho pode atingir.
     * 
     * @return A idade máxima do coelho.
     */
    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Retorna a idade em que um coelho pode começar a procriar.
     * 
     * @return A idade de procriação do coelho.
     */
    @Override
    protected int getBreedingAge() {
        return BREEDING_AGE;
    }

    /**
     * Retorna a probabilidade de um coelho procriar em cada passo.
     * 
     * @return A probabilidade de procriação (0.0 a 1.0).
     */
    @Override
    protected double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * Retorna o tamanho máximo da ninhada de coelhos.
     * 
     * @return O número máximo de filhotes por procriação.
     */
    @Override
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
}