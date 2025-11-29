import java.util.List;

public class Lion extends Predator{

    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 50;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 5;
    private static final int RABBIT_FOOD_VALUE = 4;
    private static final int FOX_FOOD_VALUE = 4;
    private static final int BUFFALO_FOOD_VALUE = 4;
    private static final int FOOD_VALUE = 6;

    public Lion(boolean randomAge) {
        super(randomAge); 
    }


    @Override
    protected Animal createYoung(boolean randomAge, Field field, Location loc) {
        Lion young = new Lion(randomAge);
        young.setLocation(loc);
        field.place(young, loc); 
        return young;
    }

    @Override
    protected boolean canEat(Object animal) {
        return (animal instanceof Rabbit) || 
               (animal instanceof Buffalo) || 
               (animal instanceof Fox);
    }


    @Override
    public int getMaxFoodValue() {
        return 40;
    }

    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }


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
