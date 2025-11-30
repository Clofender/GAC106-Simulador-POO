import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa uma grade retangular de posições do campo.
 * Cada posição pode armazenar um único animal e tem um tipo de terreno.
 * 
 * @author David J. Barnes and Michael Kolling (base original)
 * @author Gustavo Alessandro de Souza Sabino (modificações para sistema de terrenos)
 * @version 2025
 */
public class Field {
    private static final Random rand = new Random();
    
    // A profundidade e largura do campo (linhas x colunas).
    private int depth, width;
    
    // Armazenamento para os animais.
    private Animal[][] field;
    
    // Armazenamento para os tipos de terreno.
    private TerrainType[][] terrainGrid;

    /**
     * Construtor alternativo para campo com terreno padrão (grama).
     * Atualmente não utilizado - mantido para compatibilidade futura.
     */
    /*
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Animal[depth][width];
        initializeDefaultTerrain();
    }
    */

    /**
     * Representa um campo com as dimensões dadas e terreno específico.
     * 
     * @param depth A profundidade do campo.
     * @param width A largura do campo.
     * @param terrainMap Mapa de terreno pré-carregado
     */
    public Field(int depth, int width, TerrainType[][] terrainMap) {
        this.depth = depth;
        this.width = width;
        field = new Animal[depth][width];
        initializeTerrain(terrainMap);
    }

    /*
     * Inicializa o terreno com valores padrão (grama).
     * Não utilizado - mantido para referência futura - utilizado pelo contrutor.
     *
    private void initializeDefaultTerrain() {
        terrainGrid = new TerrainType[depth][width];
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                terrainGrid[row][col] = TerrainType.GRASS;
            }
        }
    }
    */

    /**
     * Inicializa o terreno com um mapa específico.
     * 
     * @param terrainMap Mapa de terreno a ser usado
     */
    private void initializeTerrain(TerrainType[][] terrainMap) {
        if (terrainMap.length != depth || terrainMap[0].length != width) {
            throw new IllegalArgumentException("Dimensões do terreno não batem com campo");
        }
        this.terrainGrid = terrainMap;
    }

    /**
     * Esvazia o campo (remove animais, mantém terreno).
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Verifica se uma localização é válida dentro dos limites do campo.
     * 
     * @param location A localização a verificar
     * @return true se a localização é válida
     */
    private boolean isValidLocation(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        return row >= 0 && row < depth && col >= 0 && col < width;
    }

    /**
     * Verifica se um animal pode se mover para uma localização específica.
     * Considera tanto os limites do campo quanto o tipo de terreno.
     * 
     * @param location A localização de destino
     * @return true se o movimento é permitido
     */
    public boolean canAnimalMoveTo(Location location) {
        if (!isValidLocation(location)) {
            return false;
        }
        
        TerrainType terrain = getTerrainAt(location);
        return terrain.isTraversable();
    }

    /**
     * Posiciona um animal no local dado.
     * Se já houver um animal lá, ele será perdido.
     * 
     * @param animal O animal a ser posicionado.
     * @param row Coordenada da linha.
     * @param col Coordenada da coluna.
     */
    public void place(Animal animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Posiciona um animal no local dado.
     * Se já houver um animal lá, ele será perdido.
     * 
     * @param animal O animal a ser posicionado.
     * @param location Onde posicionar o animal.
     */
    public void place(Animal animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
     * Retorna o animal no local dado, se houver.
     * 
     * @param location Onde no campo.
     * @return O animal no local, ou null se não houver.
     */
    public Animal getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Retorna o animal no local dado, se houver.
     * 
     * @param row A linha desejada.
     * @param col A coluna desejada.
     * @return O animal no local, ou null se não houver.
     */
    public Animal getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Retorna o tipo de terreno em uma localização específica.
     * 
     * @param location A localização desejada
     * @return O tipo de terreno na localização
     */
    public TerrainType getTerrainAt(Location location) {
        if (!isValidLocation(location)) {
            return TerrainType.WATER; // default seguro para locais inválidos
        }
        return terrainGrid[location.getRow()][location.getCol()];
    }

    /**
     * Retorna o tipo de terreno em coordenadas específicas.
     * 
     * @param row A linha desejada
     * @param col A coluna desejada
     * @return O tipo de terreno nas coordenadas
     */
    public TerrainType getTerrainAt(int row, int col) {
        return getTerrainAt(new Location(row, col));
    }

    /**
     * Gera uma localização aleatória adjacente ao local dado, ou o mesmo local.
     * O local retornado estará dentro dos limites válidos do campo e em terreno transitável.
     * 
     * @param location O local a partir do qual gerar um adjacente.
     * @return Um local válido dentro da área da grade em terreno transitável.
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
        }
        
        Location newLocation = new Location(nextRow, nextCol);
        
        // Verifica se o terreno no novo local é transitável
        if (canAnimalMoveTo(newLocation)) {
            return newLocation;
        } else {
            return location;
        }
    }

    /**
     * Tenta encontrar um local livre adjacente ao local dado.
     * Se não houver, retorna o local atual se estiver livre.
     * Se não, retorna null.
     * O local retornado estará dentro dos limites válidos e em terreno transitável.
     * 
     * @param location O local a partir do qual gerar um adjacente.
     * @return Um local válido, ou null se todos ao redor estiverem ocupados ou em terreno intransitável.
     */
    public Location freeAdjacentLocation(Location location) {
        // Obtém todos locais adjacentes embaralhados
        Iterator<Location> adjacent = adjacentLocations(location);
        
        // Procura por local vazio e transitável
        while (adjacent.hasNext()) {
            Location next = adjacent.next();
            // Verifica se não tem animal E terreno é transitável
            if (field[next.getRow()][next.getCol()] == null && canAnimalMoveTo(next)) {
                return next; // Encontrou local livre
            }
        }
        
        // verifica se o local atual está livre
        // Tenta ficar no próprio local se estiver vazio
        if (field[location.getRow()][location.getCol()] == null && canAnimalMoveTo(location)) {
            return location;
        } else {
            return null; // Nenhum local disponível
        }
    }

    /**
     * Gera um iterador sobre uma lista embaralhada de locais adjacentes
     * ao local dado. A lista não incluirá o próprio local.
     * Todos os locais estarão dentro da grade.
     * 
     * @param location O local a partir do qual gerar adjacências.
     * @return Um iterador sobre locais adjacentes.
     */
    public Iterator<Location> adjacentLocations(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        
        // Lista para armazenar locais vizinhos
        LinkedList<Location> locations = new LinkedList<>();
        
        // Verifica todas as 8 direções ao redor
        for (int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            // Verifica se linha está dentro do campo
            if (nextRow >= 0 && nextRow < depth) {
                for (int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    // Exclui locais inválidos e o local original.
                    // Só adiciona se dentro dos limites e não for o próprio local
                    if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        locations.add(new Location(nextRow, nextCol)); // Adiciona local válido
                    }
                }
            }
        }
        
        // Embaralha locais para ordem aleatória
        Collections.shuffle(locations, rand);
        return locations.iterator(); // Retorna iterador para percorrer
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
 /* 
    public void placehunter(Hunter hunter, int row, int col) {
        placehunter(hunter, new Location(row, col));
    }
    public void placehunter(Hunter hunter, Location location) {
        field[location.getRow()][location.getCol()] = hunter;
    }
    */
}