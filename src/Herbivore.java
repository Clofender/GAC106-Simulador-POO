/**
 * Classe abstrata que representa um animal herbívoro na simulação.
 * Herbívoros se alimentam de plantas e se movem aleatoriamente.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public abstract class Herbivore extends Animal {

    /**
     * Cria um novo herbívoro.
     * 
     * @param randomAge Se true, o herbívoro terá idade aleatória.
     */
    public Herbivore(boolean randomAge) {
        super(randomAge);
    }

    /**
     * Encontra a próxima localização para onde o herbívoro deve se mover.
     * Herbívoros se movem para localizações adjacentes livres.
     *
     * @param currentField O campo atual.
     * @param updatedField O campo atualizado.
     * @return A próxima localização, ou null se não puder se mover.
     */
    @Override
    public Location findNextLocation(Field currentField, Field updatedField) {
        return updatedField.freeAdjacentLocation(getLocation());
    }
}