

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, se movem, comem coelhos e morrem.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public class Fox extends Predator {
    // --- Constantes Estáticas ---
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 150;
    private static final double BREEDING_PROBABILITY = 0.09;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int FOOD_VALUE = 6;

    // --- Campos de Instância ---

    // Nível de fome da raposa, que aumenta comendo coelhos.

    /**
     * Cria uma raposa. Pode ser recém-nascida (idade zero e sem fome)
     * ou com idade e fome aleatórias.
     * * @param randomAge Se true, a raposa terá idade e fome aleatórias.
     */
    public Fox(boolean randomAge) {
        super(randomAge); // Chama o construtor da superclasse Animal
    }

    /**
     * Implementação do método abstrato de Animal.
     * Isso é o que a raposa faz em cada passo da simulação.
     * * @param currentField O campo atual.
     * * @param updatedField O campo atualizado.
     * 
     * @param newAnimals Uma lista para adicionar novos animais.
     */
    /**
     * Isso é o que a raposa faz na maior parte do tempo: caça coelhos.
     * No processo, ela pode procriar, morrer de fome ou de velhice.
     * * @param currentField O campo atual.
     * * @param updatedField O campo atualizado.
     * 
     * @param newFoxes Uma lista para adicionar novas raposas.
     */

    @Override
    protected Animal createYoung(boolean randomAge, Field field, Location loc) {
        Fox young = new Fox(randomAge);
        young.setLocation(loc);
        field.place(young, loc);
        return young;
    }

    /**
     * Deixa a raposa com mais fome. Pode resultar na morte da raposa.
     */

    /**
     * Procura por coelhos adjacentes à localização atual.
     * * @param field O campo onde deve procurar.
     * * @param location Onde a raposa está.
     * 
     * @return A localização da comida, ou null se não houver.
     */

    @Override
    protected boolean canEat(Object animal) {
        return animal instanceof Rabbit;
    }

    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }

    @Override
    public int getMaxFoodValue() {
        return 15;
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