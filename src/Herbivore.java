public abstract class Herbivore extends Animal {

    public Herbivore(boolean randomAge) {
        super(randomAge);
    }

    @Override
    protected Location findNextLocation(Field currentField, Field updatedField) {
        return updatedField.freeAdjacentLocation(getLocation());
    }
}