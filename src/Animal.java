import java.util.List;
import java.util.Random;

/**
 * Uma classe abstrata que representa as características comuns
 * de todos os animais na simulação.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public abstract class Animal {
    // Um gerador de números aleatórios compartilhado.
    protected static final Random rand = new Random();

    // A idade do animal.
    private int age;
    // Se o animal está vivo ou não.
    private boolean alive;
    // A localização do animal.
    private Location location;

    /**
     * Cria um novo animal.
     * * @param randomAge Se true, o animal terá uma idade aleatória.
     */
    public Animal(boolean randomAge) {
        age = 0;
        alive = true;
        if (randomAge) {
            age = rand.nextInt(getMaxAge());
        }
    }

    /**
     * O método principal de ação do animal, a ser implementado pelas subclasses.
     * * @param currentField O campo atual.
     * * @param updatedField O campo atualizado.
     * 
     * @param newAnimals Uma lista para adicionar novos animais.
     */
     public void act(Field currentField, Field updatedField, List<Animal> newAnimals) {
        incrementAge();
        if (isAlive()) {

            giveBirth(newAnimals, updatedField);

            Location newLocation = findNextLocation(currentField, updatedField);
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                setDead(); // Superlotação
            }
        }
    }

    /**
     * @return A idade máxima para esta espécie.
     */
    abstract protected int getMaxAge();

    /**
     * @return A idade de procriação para esta espécie.
     */
    abstract protected int getBreedingAge();

    /**
     * @return A probabilidade de procriação (0.0 a 1.0).
     */
    abstract protected double getBreedingProbability();

    /**
     * @return O tamanho máximo da ninhada.
     */
    abstract protected int getMaxLitterSize();

    protected abstract Location findNextLocation(Field currentField, Field updatedField);

    protected abstract Animal createYoung(boolean randomAge, Field field, Location loc);

    // lógica de nascimento 
    protected void giveBirth(List<Animal> newAnimals, Field field) {
        int births = breed();
        for (int b = 0; b < births; b++) {
            Location loc = field.randomAdjacentLocation(getLocation());
            
            Animal young = createYoung(false, field, loc);
            if (young != null) {
                newAnimals.add(young);
            }
        }
    }
    /**
     * Aumenta a idade. Isso pode resultar na morte do animal.
     */
    protected void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }

    /**
     * Gera um número de nascimentos, se o animal puder procriar.
     * * @return O número de nascimentos (pode ser zero).
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    /**
     * Um animal pode procriar se atingiu a idade de procriação.
     * * @return true se o animal pode procriar.
     */
    private boolean canBreed() {
        return age >= getBreedingAge();
    }

    /**
     * Marca o animal como morto (por exemplo, por velhice ou fome).
     */
    protected void setDead() {
        alive = false;
    }

    /**
     * Verifica se o animal ainda está vivo.
     * * @return True se o animal está vivo.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Define a localização do animal.
     * * @param location A nova localização.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Define a localização do animal.
     * * @param row A coordenada vertical.
     * * @param col A coordenada horizontal.
     */
    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    /**
     * @return A localização atual do animal.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return O valor nutricional deste animal quando for comido.
     */
    public abstract int getFoodValue();
}