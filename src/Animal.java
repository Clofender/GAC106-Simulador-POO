import java.util.Random;
import java.util.ArrayList;

/**
 * Uma classe abstrata que representa as características comuns
 * de todos os animais na simulação. Implementa a interface Actor
 * para permitir tratamento polimórfico.
 * 
 * @author David J. Barnes and Michael Kolling (base)
 * @author TP_Grupo08 (modificações)
 * @version 2025
 */
public abstract class Animal implements Actor {
    
    // Um gerador de números aleatórios compartilhado.
    private static final Random rand = RandomGenerator.getRandom();
    
    // A idade do animal.
    private int age;
    
    // Se o animal está vivo ou não.
    private boolean alive;
    
    // A localização do animal.
    private Location location;
    
    // Referência ao sistema de clima (para efeitos sazonais)
    private static WeatherSystem weatherSystem;

    /**
     * Cria um novo animal.
     *
     * @param randomAge Se true, o animal terá uma idade aleatória.
     */
    public Animal(boolean randomAge) {
        age = 0;
        alive = true;
        if (randomAge) {
            age = rand.nextInt(getMaxAge());
        }
    }

    /**
     * Define o sistema de clima para todos os animais.
     * Método estático para compartilhar a mesma instância entre todos os animais.
     *
     * @param weather O sistema de clima a ser usado.
     */
    public static void setWeatherSystem(WeatherSystem weather) {
        weatherSystem = weather;
    }

    /**
     * Implementação do método da interface Actor.
     * Executa a ação principal do animal no passo atual.
     *
     * @param currentField O campo atual.
     * @param updatedField O campo atualizado.
     * @param newActors Lista para adicionar novos atores.
     */
    @Override
    public void act(Field currentField, Field updatedField, java.util.List<Actor> newActors) {
        incrementAge();
        if (isAlive()) {
            java.util.List<Animal> newAnimals = new ArrayList<>();
            giveBirth(newAnimals, updatedField);
            
            for (Animal animal : newAnimals) {
                newActors.add(animal);
            }
            
            Location newLocation = findNextLocation(currentField, updatedField);
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                setDead();
            }
        }
    }

    /**
     * Método de compatibilidade - mantém interface antiga para o simulador
     * Usa nome diferente para evitar conflito de type erasure
     *
     * @param currentField O campo atual.
     * @param updatedField O campo atualizado.
     * @param newAnimals Lista para adicionar novos animais.
     */
    public void actWithAnimals(Field currentField, Field updatedField, java.util.List<Animal> newAnimals) {
        java.util.List<Actor> newActors = new ArrayList<>();
        act(currentField, updatedField, newActors);
        
        for (Actor actor : newActors) {
            if (actor instanceof Animal) {
                newAnimals.add((Animal) actor);
            }
        }
    }

    /**
     * @return A idade máxima para esta espécie.
     */
    public abstract int getMaxAge();

    /**
     * @return A idade de procriação para esta espécie.
     */
    public abstract int getBreedingAge();

    /**
     * @return A probabilidade de procriação (0.0 a 1.0).
     */
    public abstract double getBreedingProbability();

    /**
     * @return O tamanho máximo da ninhada.
     */
    public abstract int getMaxLitterSize();

    /**
     * Encontra a próxima localização para onde o animal deve se mover.
     *
     * @param currentField O campo atual.
     * @param updatedField O campo atualizado.
     * @return A próxima localização, ou null se não puder se mover.
     */
    public abstract Location findNextLocation(Field currentField, Field updatedField);

    /**
     * Cria um novo animal jovem.
     *
     * @param randomAge Se true, o jovem terá idade aleatória.
     * @param field O campo onde o jovem será colocado.
     * @param loc A localização do jovem.
     * @return O animal jovem criado.
     */
    public abstract Animal createYoung(boolean randomAge, Field field, Location loc);

    /**
     * Lógica de nascimento - cria novos animais através de reprodução.
     *
     * @param newAnimals Lista para adicionar os novos animais.
     * @param field O campo onde os novos animais serão colocados.
     */
    private void giveBirth(java.util.List<Animal> newAnimals, Field field) {
        int births = breed();
        for (int b = 0; b < births; b++) {
            Location loc = field.freeAdjacentLocation(getLocation());
            if (loc != null) {
                Animal young = createYoung(false, field, loc);
                if (young != null) {
                    newAnimals.add(young);
                }
            }
        }
    }

    /**
     * Aumenta a idade. Isso pode resultar na morte do animal.
     */
    private void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }

    /**
     * Gera um número de nascimentos, se o animal puder procriar.
     * Aplica efeitos sazonais se o sistema de clima estiver disponível.
     *
     * @return O número de nascimentos (pode ser zero).
     */
    private int breed() {
        int births = 0;
        double probability = getBreedingProbability();
        
        if (weatherSystem != null) {
            Season currentSeason = weatherSystem.getCurrentSeason();
            probability *= currentSeason.getBreedingFactor();
        }
        
        if (canBreed() && rand.nextDouble() <= probability) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    /**
     * Um animal pode procriar se atingiu a idade de procriação.
     *
     * @return true se o animal pode procriar.
     */
    private boolean canBreed() {
        return age >= getBreedingAge();
    }

    /**
     * Marca o animal como morto (por exemplo, por velhice ou fome).
     */
    public void setDead() {
        alive = false;
    }

    /**
     * Verifica se o animal ainda está vivo.
     *
     * @return True se o animal está vivo.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Define a localização do animal.
     *
     * @param location A nova localização.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Define a localização do animal.
     *
     * @param row A coordenada vertical.
     * @param col A coordenada horizontal.
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