/**
 * Um modelo simples de uma raposa.
 * Raposas são predadores que se alimentam de coelhos.
 * Envelhecem, se movem, comem coelhos e morrem.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author TP_Grupo08 (modificações)
 * @version 2025
 */
public class Fox extends Predator {
    // --- Constantes Estáticas ---
    
    /** Idade em que a raposa pode começar a procriar */
    private static final int BREEDING_AGE = 10;
    
    /** Idade máxima que uma raposa pode atingir */
    private static final int MAX_AGE = 90;
    
    /** Probabilidade de procriação em cada passo (0.0 a 1.0) */
    private static final double BREEDING_PROBABILITY = 0.023;
    
    /** Número máximo de filhotes por procriação */
    private static final int MAX_LITTER_SIZE = 9;
    
    /** Valor nutricional quando comido por predadores */
    private static final int FOOD_VALUE = 12;
    
    /** Capacidade máxima do estômago da raposa */
    private static final int MAX_FOOD_VALUE = 12;
    
    /** Probabilidade de criação durante a população inicial */
    private static final double CREATION_PROBABILITY = 0.04;

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
     * @param randomAge Se true, terá idade aleatória.
     * @param field O campo onde a raposa será colocada.
     * @param loc A localização da raposa.
     * @return A raposa jovem criada.
     */
    @Override
    public Animal createYoung(boolean randomAge, Field field, Location loc) {
        // Cria uma nova instância de raposa jovem
        // O parâmetro randomAge define se nasce com idade aleatória ou zero
        Fox young = new Fox(randomAge);
        
        // Define a localização da raposa jovem no campo
        // A localização é passada pelo método giveBirth() que encontrou um espaço livre
        young.setLocation(loc);
        
        // Posiciona a raposa jovem no campo na localização especificada
        // O campo verifica se a localização é válida e transitável
        field.place(young, loc);
        
        // Retorna a raposa jovem como tipo Animal (polimorfismo)
        // Permite que o método giveBirth() trate todos os animais de forma uniforme
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
    public boolean canEat(Object animal) {
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
        return MAX_FOOD_VALUE;
    }

    /**
     * Retorna a idade máxima que uma raposa pode atingir.
     * 
     * @return A idade máxima da raposa.
     */
    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Retorna a idade em que uma raposa pode começar a procriar.
     * 
     * @return A idade de procriação da raposa.
     */
    @Override
    public int getBreedingAge() {
        return BREEDING_AGE;
    }

    /**
     * Retorna a probabilidade de uma raposa procriar em cada passo.
     * 
     * @return A probabilidade de procriação (0.0 a 1.0).
     */
    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * Retorna o tamanho máximo da ninhada de raposas.
     * 
     * @return O número máximo de filhotes por procriação.
     */
    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Retorna a probabilidade de criação de uma raposa durante a população inicial.
     * 
     * @return A probabilidade de criação (0.0 a 1.0).
     */
    public static double getCreationProbability() {
        return CREATION_PROBABILITY;
    }
}