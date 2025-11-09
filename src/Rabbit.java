import java.util.List;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, se movem, procriam e morrem.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public class Rabbit extends Animal {
    // --- Constantes Estáticas ---
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 50;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 5;

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
    @Override
    public void act(Field currentField, Field updatedField, List<Animal> newAnimals) {
        run(updatedField, newAnimals);
    }

    /**
     * O que o coelho faz: corre. Às vezes procria ou morre de velhice.
     * * @param updatedField O campo atualizado.
     * * @param newRabbits Uma lista para adicionar novos coelhos.
     */
    private void run(Field updatedField, List<Animal> newRabbits) {
        incrementAge();
        if (isAlive()) {
            int births = breed();
            for (int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
                newRabbits.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newRabbit.setLocation(loc);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            // Só se transfere para o novo campo se houver um local livre
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // Superlotação
                setDead();
            }
        }
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