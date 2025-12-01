import java.util.Iterator;

/**
 * Classe abstrata que representa um animal predador na simulação.
 * Predadores possuem sistema de fome e podem caçar outros animais.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public abstract class Predator extends Animal {
    
    // Nível atual de comida no estômago do predador
    private int foodLevel;

    /**
     * Cria um novo predador.
     * 
     * @param randomAge Se true, o predador terá idade aleatória.
     */
    public Predator(boolean randomAge) {
        super(randomAge);  // Chamar construtor da classe pai (Animal)
        if (randomAge) {
            // Se idade aleatória, comida também aleatória
            foodLevel = RandomGenerator.nextInt(getMaxFoodValue());
        } else {
            // Se recém-nascido, começa com estômago cheio
            foodLevel = getMaxFoodValue();
        }
    }

    /**
     * Encontra a próxima localização para onde o predador deve se mover.
     * Primeiro tenta encontrar comida, depois move aleatoriamente.
     *
     * @param currentField O campo atual.
     * @param updatedField O campo atualizado.
     * @return A próxima localização, ou null se não puder se mover.
     */
    @Override
    public Location findNextLocation(Field currentField, Field updatedField) {
        incrementHunger();  // Reduzir nível de comida a cada movimento
        
        // Primeiro: tentar encontrar comida nas adjacências
        Location foodLocation = findFood(currentField, getLocation());
        if (foodLocation != null) {
            return foodLocation;  // Se encontrou comida, mover para lá
        }
        
        // Segundo: se não encontrou comida, mover aleatoriamente
        return updatedField.freeAdjacentLocation(getLocation());
    }

    /**
     * Procura por presas adjacentes à localização atual.
     *
     * @param field O campo onde procurar.
     * @param location A localização atual do predador.
     * @return A localização da presa, ou null se não encontrar.
     */
    private Location findFood(Field field, Location location) {
        // Obter iterador para todas as localizações adjacentes
        Iterator<Location> it = field.adjacentLocations(location); 
        
        // Verificar cada localização adjacente
        while(it.hasNext()) {
            Location where = it.next();  // Próxima localização adjacente
            Object object = field.getObjectAt(where);  // Objeto naquela posição
            
            // Verificar se é um animal (presa potencial)
            if(object instanceof Animal) {
                Animal prey = (Animal) object;  // Cast para Animal
                
                // Verificar se a presa está viva e se pode ser comida
                if(prey.isAlive() && canEat(prey)) { 
                    prey.setDead();  // Matar a presa
                    this.foodLevel += prey.getFoodValue();  // Ganhar energia da presa
                    
                    // Não ultrapassar capacidade máxima do estômago
                    if (this.foodLevel > getMaxFoodValue()) {
                        this.foodLevel = getMaxFoodValue();
                    }
                    return where;  // Retornar localização da presa encontrada
                }
            }
        }
        return null;  // Nenhuma presa encontrada
    }

    /**
     * Aumenta a fome do predador. Pode resultar em morte por fome.
     */
    private void incrementHunger() {
        foodLevel--;  // Reduzir nível de comida
        if (foodLevel <= 0) {
            setDead();  // Morrer se ficar sem comida
        }
    }
    
    /**
     * Retorna o valor máximo de comida que este predador pode armazenar.
     * 
     * @return A capacidade máxima do estômago.
     */
    public abstract int getMaxFoodValue();

    /**
     * Verifica se este predador pode comer o animal especificado.
     * 
     * @param animal O animal a verificar.
     * @return true se pode comer o animal, false caso contrário.
     */
    public abstract boolean canEat(Object animal);
}