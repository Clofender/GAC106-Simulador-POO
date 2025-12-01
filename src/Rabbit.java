/**
 * Um modelo simples de um coelho.
 * Coelhos são herbívoros pequenos que servem de presa para
 * raposas, leões e caçadores. Envelhecem, se movem, procriam e morrem.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author TP_Grupo08 (modificações)
 * @version 2025
 */
public class Rabbit extends Herbivore {
    // --- Constantes Estáticas ---
    
    /** Idade em que o coelho pode começar a procriar */
    private static final int BREEDING_AGE = 5;
    
    /** Idade máxima que um coelho pode atingir */
    private static final int MAX_AGE = 25;
    
    /** Probabilidade de procriação em cada passo (0.0 a 1.0) */
    private static final double BREEDING_PROBABILITY = 0.6;
    
    /** Número máximo de filhotes por procriação */
    private static final int MAX_LITTER_SIZE = 23;
    
    /** Valor nutricional quando comido por predadores */
    private static final int FOOD_VALUE = 10;
    
    /** Probabilidade de criação durante a população inicial */
    private static final double CREATION_PROBABILITY = 0.12;

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
     * Implementa o padrão Factory Method para criação de novos coelhos.
     * 
     * @param randomAge Se true, o jovem terá idade aleatória.
     * @param field O campo onde o jovem será colocado.
     * @param loc A localização do jovem.
     * @return O coelho jovem criado.
     */
    @Override
    public Animal createYoung(boolean randomAge, Field field, Location loc) {
        // Instancia um novo coelho jovem
        Rabbit young = new Rabbit(randomAge);
        
        // Define a posição do filhote no campo
        young.setLocation(loc);
        
        // Registra o coelho no campo de simulação
        field.place(young, loc);
        
        // Retorna o novo coelho para o sistema
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
    public int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Retorna a idade em que um coelho pode começar a procriar.
     * 
     * @return A idade de procriação do coelho.
     */
    @Override
    public int getBreedingAge() {
        return BREEDING_AGE;
    }

    /**
     * Retorna a probabilidade de um coelho procriar em cada passo.
     * 
     * @return A probabilidade de procriação (0.0 a 1.0).
     */
    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * Retorna o tamanho máximo da ninhada de coelhos.
     * 
     * @return O número máximo de filhotes por procriação.
     */
    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Retorna a probabilidade de criação de um coelho durante a população inicial.
     * 
     * @return A probabilidade de criação (0.0 a 1.0).
     */
    public static double getCreationProbability() {
        return CREATION_PROBABILITY;
    }
}