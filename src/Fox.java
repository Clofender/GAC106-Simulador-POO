/**
 * Um modelo simples de uma raposa.
 * Raposas são predadores que se alimentam de coelhos.
 * Envelhecem, se movem, comem coelhos e morrem.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2025
 */
public class Fox extends Predator {
    // --- Constantes Estáticas ---
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 90;
    private static final double BREEDING_PROBABILITY = 0.03;
    private static final int MAX_LITTER_SIZE = 4;
    private static final int FOOD_VALUE = 6;

    /**
     * Cria uma raposa. Pode ser recém-nascida (idade zero e sem fome)
     * ou com idade e fome aleatórias.
     * 
     * @param randomAge Se true, a raposa terá idade e fome aleatórias.
     */
    public Fox(boolean randomAge) {
        super(randomAge);
    }

    /**
     * Cria uma nova raposa jovem.
     * 
     * @param randomAge Se true, o jovem terá idade aleatória.
     * @param field O campo onde o jovem será colocado.
     * @param loc A localização do jovem.
     * @return A raposa jovem criada.
     */
    @Override
    protected Animal createYoung(boolean randomAge, Field field, Location loc) {
        Fox young = new Fox(randomAge);
        young.setLocation(loc);
        field.place(young, loc);
        return young;
    }

    /**
     * Verifica se este predador pode comer o animal especificado.
     * Raposas comem apenas coelhos.
     * 
     * @param animal O animal a verificar.
     * @return true se pode comer o animal, false caso contrário.
     */
    @Override
    protected boolean canEat(Object animal) {
        return animal instanceof Rabbit;
    }

    /**
     * Retorna o valor nutricional desta raposa quando for comida.
     * 
     * @return O valor nutricional da raposa.
     */
    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }

    /**
     * Retorna o valor máximo de comida que esta raposa pode armazenar.
     * 
     * @return A capacidade máxima do estômago da raposa.
     */
    @Override
    public int getMaxFoodValue() {
        return 12; // Reduzido de 15 (morre de fome mais rápido se não encontrar coelhos)
    }

    /**
     * Retorna a idade máxima que uma raposa pode atingir.
     * 
     * @return A idade máxima da raposa.
     */
    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Retorna a idade em que uma raposa pode começar a procriar.
     * 
     * @return A idade de procriação da raposa.
     */
    @Override
    protected int getBreedingAge() {
        return BREEDING_AGE;
    }

    /**
     * Retorna a probabilidade de uma raposa procriar em cada passo.
     * 
     * @return A probabilidade de procriação (0.0 a 1.0).
     */
    @Override
    protected double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * Retorna o tamanho máximo da ninhada de raposas.
     * 
     * @return O número máximo de filhotes por procriação.
     */
    @Override
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
}