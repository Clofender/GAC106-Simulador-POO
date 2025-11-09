import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa uma grade retangular de posições do campo.
 * Cada posição pode armazenar um único animal.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public class Field {
    private static final Random rand = new Random();

    // A profundidade e largura do campo.
    private int depth, width;
    // Armazenamento para os animais.
    private Animal[][] field;

    /**
     * Representa um campo com as dimensões dadas.
     * * @param depth A profundidade do campo.
     * * @param width A largura do campo.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Animal[depth][width];
    }

    /**
     * Esvazia o campo.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Posiciona um animal no local dado.
     * Se já houver um animal lá, ele será perdido.
     * * @param animal O animal a ser posicionado.
     * * @param row Coordenada da linha.
     * 
     * @param col Coordenada da coluna.
     */
    public void place(Animal animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Posiciona um animal no local dado.
     * Se já houver um animal lá, ele será perdido.
     * * @param animal O animal a ser posicionado.
     * * @param location Onde posicionar o animal.
     */
    public void place(Animal animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
     * Retorna o animal no local dado, se houver.
     * * @param location Onde no campo.
     * * @return O animal no local, ou null se não houver.
     */
    public Animal getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Retorna o animal no local dado, se houver.
     * * @param row A linha desejada.
     * * @param col A coluna desejada.
     * 
     * @return O animal no local, ou null se não houver.
     */
    public Animal getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Gera uma localização aleatória adjacente ao local dado,
     * ou o mesmo local.
     * O local retornado estará dentro dos limites válidos do campo.
     * * @param location O local a partir do qual gerar um adjacente.
     * * @return Um local válido dentro da área da grade.
     */
    public Location randomAdjacentLocation(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        // Gera um deslocamento de -1, 0, ou +1 para linha e coluna.
        int nextRow = row + rand.nextInt(3) - 1;
        int nextCol = col + rand.nextInt(3) - 1;
        // Verifica se o novo local está fora dos limites.
        if (nextRow < 0 || nextRow >= depth || nextCol < 0 || nextCol >= width) {
            return location;
        } else if (nextRow != row || nextCol != col) {
            return new Location(nextRow, nextCol);
        } else {
            return location;
        }
    }

    /**
     * Tenta encontrar um local livre adjacente ao local dado.
     * Se não houver, retorna o local atual se estiver livre.
     * Se não, retorna null.
     * O local retornado estará dentro dos limites válidos.
     * * @param location O local a partir do qual gerar um adjacente.
     * * @return Um local válido, ou null se todos ao redor estiverem ocupados.
     */
    public Location freeAdjacentLocation(Location location) {
        Iterator<Location> adjacent = adjacentLocations(location);
        while (adjacent.hasNext()) {
            Location next = adjacent.next();
            if (field[next.getRow()][next.getCol()] == null) {
                return next;
            }
        }
        // verifica se o local atual está livre
        if (field[location.getRow()][location.getCol()] == null) {
            return location;
        } else {
            return null;
        }
    }

    /**
     * Gera um iterador sobre uma lista embaralhada de locais adjacentes
     * ao local dado. A lista não incluirá o próprio local.
     * Todos os locais estarão dentro da grade.
     * * @param location O local a partir do qual gerar adjacências.
     * * @return Um iterador sobre locais adjacentes.
     */
    public Iterator<Location> adjacentLocations(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        LinkedList<Location> locations = new LinkedList<>();
        for (int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            if (nextRow >= 0 && nextRow < depth) {
                for (int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    // Exclui locais inválidos e o local original.
                    if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }
        Collections.shuffle(locations, rand);
        return locations.iterator();
    }

    /**
     * @return A profundidade do campo.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return A largura do campo.
     */
    public int getWidth() {
        return width;
    }
}