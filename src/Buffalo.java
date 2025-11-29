import java.util.List;
import java.util.Iterator;

public class Buffalo extends Herbivore{

    // --- Constantes Estáticas ---
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 150;
    private static final double BREEDING_PROBABILITY = 0.09;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int FOOD_VALUE = 20;

   /**
     * Cria um novo búfalo.
     * * @param randomAge Se true, o búfalo terá idade aleatória.
     */
    public Buffalo(boolean randomAge){
        super(randomAge);   
    }

     @Override
    protected Animal createYoung(boolean randomAge, Field field, Location loc) {
        Buffalo young = new Buffalo(randomAge);
        young.setLocation(loc);
        field.place(young, loc); // Importante colocar no field
        return young;
    }
    
    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }

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
