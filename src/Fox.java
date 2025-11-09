import java.util.List;
import java.util.Iterator;

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, se movem, comem coelhos e morrem.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public class Fox extends Animal {
    // --- Constantes Estáticas ---
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 150;
    private static final double BREEDING_PROBABILITY = 0.09;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int RABBIT_FOOD_VALUE = 4;

    // --- Campos de Instância ---

    // Nível de fome da raposa, que aumenta comendo coelhos.
    private int foodLevel;

    /**
     * Cria uma raposa. Pode ser recém-nascida (idade zero e sem fome)
     * ou com idade e fome aleatórias.
     * * @param randomAge Se true, a raposa terá idade e fome aleatórias.
     */
    public Fox(boolean randomAge) {
        super(randomAge); // Chama o construtor da superclasse Animal
        if (randomAge) {
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        } else {
            // raposa recém-nascida
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * Implementação do método abstrato de Animal.
     * Isso é o que a raposa faz em cada passo da simulação.
     * * @param currentField O campo atual.
     * * @param updatedField O campo atualizado.
     * 
     * @param newAnimals Uma lista para adicionar novos animais.
     */
    @Override
    public void act(Field currentField, Field updatedField, List<Animal> newAnimals) {
        hunt(currentField, updatedField, newAnimals);
    }

    /**
     * Isso é o que a raposa faz na maior parte do tempo: caça coelhos.
     * No processo, ela pode procriar, morrer de fome ou de velhice.
     * * @param currentField O campo atual.
     * * @param updatedField O campo atualizado.
     * 
     * @param newFoxes Uma lista para adicionar novas raposas.
     */
    private void hunt(Field currentField, Field updatedField, List<Animal> newFoxes) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // Novos filhotes nascem em locais adjacentes.
            int births = breed();
            for (int b = 0; b < births; b++) {
                Fox newFox = new Fox(false);
                newFoxes.add(newFox);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newFox.setLocation(loc);
                updatedField.place(newFox, loc);
            }
            // Move em direção à fonte de comida, se encontrada.
            Location newLocation = findFood(currentField, getLocation());
            if (newLocation == null) { // sem comida - move aleatoriamente
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            // Se há um local para se mover
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // Superlotação - não pode mover nem ficar parado
                setDead();
            }
        }
    }

    /**
     * Deixa a raposa com mais fome. Pode resultar na morte da raposa.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Procura por coelhos adjacentes à localização atual.
     * * @param field O campo onde deve procurar.
     * * @param location Onde a raposa está.
     * 
     * @return A localização da comida, ou null se não houver.
     */
    private Location findFood(Field field, Location location) {
        Iterator<Location> adjacentLocations = field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = adjacentLocations.next();
            Animal animal = field.getObjectAt(where);
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if (rabbit.isAlive()) {
                    rabbit.setEaten(); // Coelha morre
                    foodLevel = RABBIT_FOOD_VALUE; // Raposa fica satisfeita
                    return where;
                }
            }
        }
        return null;
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