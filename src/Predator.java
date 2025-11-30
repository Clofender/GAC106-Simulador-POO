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
        super(randomAge);
        if (randomAge) {
            // Começa com comida aleatória se idade for aleatória
            foodLevel = rand.nextInt(getMaxFoodValue());
        } else {
            foodLevel = getMaxFoodValue(); // Estômago cheio ao nascer/iniciar
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
    protected Location findNextLocation(Field currentField, Field updatedField) {
        incrementHunger();
        
        // Primeiro tenta encontrar comida
        Location foodLocation = findFood(currentField, getLocation());
        if (foodLocation != null) {
            return foodLocation;
        }
        
        // Se não encontrou comida, move aleatoriamente
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
        Iterator<Location> it = field.adjacentLocations(location); 
        
        while(it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            
            if(object instanceof Animal) {
                Animal prey = (Animal) object;
                
                if(prey.isAlive() && canEat(prey)) { 
                    // Come a presa e ganha energia
                    prey.setDead();
                    this.foodLevel += prey.getFoodValue();
                    
                    // Não deixa a energia passar do máximo
                    if (this.foodLevel > getMaxFoodValue()) {
                        this.foodLevel = getMaxFoodValue();
                    }
                    return where;     
                }
            }
        }
        return null;
    }

    /**
     * Aumenta a fome do predador. Pode resultar em morte por fome.
     */
    public void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
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
    protected abstract boolean canEat(Object animal);
}