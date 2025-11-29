import java.util.List;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, se movem, procriam e morrem.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public class Rabbit extends Herbivore {
    // --- Constantes Estáticas ---
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 50;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 5;
    private static final int FOOD_VALUE = 4;
    /**
     * Cria um novo coelho.
     * * @param randomAge Se true, o coelho terá idade aleatória.
     */
    public Rabbit(boolean randomAge) {
        super(randomAge); // Chama o construtor da superclasse Animal
    }

    /**
     * Implementação do método abstrato de Animal.
     * Isso é o que o coelho faz em cada passo da simulação.
     * * @param currentField O campo atual.
     * * @param updatedField O campo atualizado.
     * 
     * @param newAnimals Uma lista para adicionar novos animais.
     */

    /**
     * O que o coelho faz: corre. Às vezes procria ou morre de velhice.
     * * @param updatedField O campo atualizado.
     * * @param newRabbits Uma lista para adicionar novos coelhos.
     */

    @Override
    protected Animal createYoung(boolean randomAge, Field field, Location loc) {
        Rabbit young = new Rabbit(randomAge);
        young.setLocation(loc);
        field.place(young, loc); // Importante colocar no field
        return young;
    }

    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }
    
    /**
     * Avisa ao coelho que ele foi comido.
     */
    public void setEaten() {
        setDead(); // Usa o método da superclasse
    }

    // --- Implementação dos métodos abstratos de Animal ---

    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected int getBreedingAge() {
        return BREEDING_AGE;
    }

    @Override
    protected double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    @Override
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
}