
import java.util.Iterator;

public abstract class Predator extends Animal {
    private int foodLevel;

    public Predator(boolean randomAge) {
        super(randomAge);
        if (randomAge) {
            // Começa com comida aleatória se idade for aleatória
            foodLevel = rand.nextInt(getMaxFoodValue());
        } else {
            foodLevel = getMaxFoodValue(); // Estômago cheio ao nascer/iniciar
        }
    }

    @Override
    protected Location findNextLocation(Field currentField, Field updatedField) {
        incrementHunger();
        
        Location foodLocation = findFood(currentField, getLocation());
        
       if (foodLocation != null) {
    Object preyObject = currentField.getObjectAt(foodLocation);
    if (preyObject instanceof Animal) {
        Animal prey = (Animal) preyObject;
        
        this.foodLevel += prey.getFoodValue();
        
        if (this.foodLevel > getMaxFoodValue()) {
            this.foodLevel = getMaxFoodValue();
        }
    }
    return foodLocation;
}
        
        return updatedField.freeAdjacentLocation(getLocation());
    }

  private Location findFood(Field field, Location location) {

        Iterator<Location> it = field.adjacentLocations(location); 
        
        while(it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            
            if(object instanceof Animal) {
                Animal prey = (Animal) object;
                
                if(prey.isAlive() && canEat(prey)) { 
                    prey.setDead();
                    return where;     
                }
            }
        }
        return null;
    }

    public void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) setDead();
    }

    
    // método abstrato para definir o tamanho do estômago
    public abstract int getMaxFoodValue();

    protected abstract boolean canEat(Object animal);
}