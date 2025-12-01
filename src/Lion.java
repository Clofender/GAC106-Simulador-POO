/**
 * Representa um leão na simulação - um superpredador.
 * Leões são predadores de topo que podem caçar vários tipos de animais
 * incluindo coelhos, búfalos e raposas.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class Lion extends Predator {
    // --- Constantes Estáticas ---
    
    /** Idade em que o leão pode começar a procriar */
    private static final int BREEDING_AGE = 20;
    
    /** Idade máxima que um leão pode atingir */
    private static final int MAX_AGE = 100;
    
    /** Probabilidade de procriação em cada passo (0.0 a 1.0) */
    private static final double BREEDING_PROBABILITY = 0.01;
    
    /** Número máximo de filhotes por procriação */
    private static final int MAX_LITTER_SIZE = 5;
    
    /** Valor nutricional quando comido por predadores */
    private static final int FOOD_VALUE = 16;
    
    /** Capacidade máxima do estômago do leão */
    private static final int MAX_FOOD_VALUE = 25;
    
    /** Probabilidade de criação durante a população inicial */
    private static final double CREATION_PROBABILITY = 0.010;

    /**
     * Cria um novo leão.
     * 
     * @param randomAge Se true, o leão terá idade aleatória.
     */
    public Lion(boolean randomAge) {
        super(randomAge);
    }

    /**
     * Cria um novo leão jovem.
     * 
     * @param randomAge Se true, o jovem terá idade aleatória.
     * @param field O campo onde o jovem será colocado.
     * @param loc A localização do jovem.
     * @return O leão jovem criado.
     */
    @Override
    public Animal createYoung(boolean randomAge, Field field, Location loc) {
        // Cria uma nova instância de leão jovem
        // O parâmetro randomAge define se o filhote nasce com idade zero ou idade aleatória
        Lion young = new Lion(randomAge);
        
        // Define a localização do leão jovem no campo
        // Esta é a posição onde o filhote vai nascer
        young.setLocation(loc);
        
        // Coloca o leão jovem no campo na localização especificada
        // Isso registra o novo animal no sistema de simulação
        field.place(young, loc);
        
        // Retorna o leão jovem criado
        // O tipo de retorno é Animal para permitir polimorfismo
        return young;
    }
    /**
     * Verifica se este predador pode comer o animal especificado.
     * Leões comem coelhos, búfalos e raposas.
     * 
     * @param animal O animal a verificar.
     * @return true se pode comer o animal, false caso contrário.
     */
    @Override
    public boolean canEat(Object animal) {
        return (animal instanceof Rabbit) ||
               (animal instanceof Buffalo) ||
               (animal instanceof Fox);
    }

    /**
     * Retorna o valor máximo de comida que este leão pode armazenar.
     * 
     * @return A capacidade máxima do estômago do leão.
     */
    @Override
    public int getMaxFoodValue() {
        return MAX_FOOD_VALUE;
    }

    /**
     * Retorna o valor nutricional deste leão quando for comido.
     * 
     * @return O valor nutricional do leão.
     */
    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }

    /**
     * Marca o leão como comido (morto por outro predador).
     */
    public void setEaten() {
        setDead();
    }

    /**
     * Retorna a idade máxima que um leão pode atingir.
     * 
     * @return A idade máxima do leão.
     */
    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Retorna a idade em que um leão pode começar a procriar.
     * 
     * @return A idade de procriação do leão.
     */
    @Override
    public int getBreedingAge() {
        return BREEDING_AGE;
    }

    /**
     * Retorna a probabilidade de um leão procriar em cada passo.
     * 
     * @return A probabilidade de procriação (0.0 a 1.0).
     */
    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * Retorna o tamanho máximo da ninhada de leões.
     * 
     * @return O número máximo de filhotes por procriação.
     */
    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Retorna a probabilidade de criação de um leão durante a população inicial.
     * 
     * @return A probabilidade de criação (0.0 a 1.0).
     */
    public static double getCreationProbability() {
        return CREATION_PROBABILITY;
    }
}