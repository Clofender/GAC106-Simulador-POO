import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma grade retangular de posições do campo.
 * Cada posição pode armazenar um ator (Animal ou Hunter) e tem um tipo de terreno.
 * Usa composição com TerrainType para definir as propriedades do terreno.
 *
 * @author David J. Barnes and Michael Kolling (base original)
 * @author TP_Grupo08 (modificações para sistema de terrenos e atores)
 * @version 2025
 */
public class Field {
    
    // A profundidade e largura do campo (linhas x colunas).
    private final int depth, width;
    
    // Armazenamento para os atores (uso de composição).
    private final Actor[][] field;
    
    // Armazenamento para os tipos de terreno (uso de composição).
    private TerrainType[][] terrainGrid;
    
    // Lista para armazenar as casas dos caçadores
    private final List<Location> hunterHomes;

    /**
     * Representa um campo com as dimensões dadas e terreno específico.
     *
     * @param depth A profundidade do campo.
     * @param width A largura do campo.
     * @param terrainMap Mapa de terreno pré-carregado.
     */
    public Field(int depth, int width, TerrainType[][] terrainMap) {
        // Define as dimensões do campo (linhas x colunas)
        this.depth = depth;
        this.width = width;
        // Cria uma matriz para armazenar os atores (animais e caçadores)
        field = new Actor[depth][width];
        // Inicializa a lista de casas de caçadores
        hunterHomes = new ArrayList<>();
        // Inicializa o sistema de terreno com o mapa fornecido
        initializeTerrain(terrainMap);
    }

    /**
     * Verifica se uma localização está dentro dos limites do campo.
     * Método privado pois só é usado internamente nesta classe.
     *
     * @param location A localização a verificar.
     * @return true se a localização está dentro dos limites do campo.
     */
    private boolean isWithinBounds(Location location) {
        // Extrai as coordenadas da localização
        int row = location.getRow();
        int col = location.getCol();
        // Verifica se as coordenadas estão dentro dos limites do campo
        return row >= 0 && row < depth && col >= 0 && col < width;
    }

    /**
     * Verifica se coordenadas específicas estão dentro dos limites do campo.
     * Método público e estático para uso em outras classes quando necessário.
     *
     * @param row A linha a verificar.
     * @param col A coluna a verificar.
     * @param depth A profundidade do campo.
     * @param width A largura do campo.
     * @return true se as coordenadas estão dentro dos limites.
     */
    public static boolean isWithinBounds(int row, int col, int depth, int width) {
        // Verifica se as coordenadas estão dentro dos limites especificados
        return row >= 0 && row < depth && col >= 0 && col < width;
    }

    /**
     * Inicializa o terreno com um mapa específico.
     *
     * @param terrainMap Mapa de terreno a ser usado.
     */
    private void initializeTerrain(TerrainType[][] terrainMap) {
        // Verifica se as dimensões do mapa batem com as do campo
        if (terrainMap.length != depth || terrainMap[0].length != width) {
            // Lança exceção se as dimensões não coincidem
            throw new IllegalArgumentException("Dimensões do terreno não batem com campo");
        }
        // Atribui o mapa de terreno ao campo
        this.terrainGrid = terrainMap;
    }

    /**
     * Esvazia o campo (remove atores, mantém terreno).
     */
    public void clear() {
        // Percorre todas as linhas do campo
        for (int row = 0; row < depth; row++) {
            // Percorre todas as colunas do campo
            for (int col = 0; col < width; col++) {
                // Remove qualquer ator da posição atual
                field[row][col] = null;
            }
        }
        // Limpa a lista de casas de caçadores
        hunterHomes.clear();
        // OBS: O terreno (terrainGrid) permanece inalterado
    }

    /**
     * Verifica se um ator pode se mover para uma localização específica.
     * Considera tanto os limites do campo quanto o tipo de terreno.
     *
     * @param location A localização de destino.
     * @return true se o movimento é permitido.
     */
    public boolean canAnimalMoveTo(Location location) {
        // Primeiro verifica se a localização está dentro dos limites do campo
        if (!isWithinBounds(location)) {
            // Se fora dos limites, movimento não permitido
            return false;
        }
        
        // Verifica se a localização é uma casa de caçador (apenas caçadores podem ocupar)
        if (isHunterHome(location)) {
            return false;  // Animais não podem ocupar casas de caçadores
        }
        
        // Obtém o tipo de terreno na localização desejada
        TerrainType terrain = getTerrainAt(location);
        // Verifica se o terreno é transitável (não é água nem árvore)
        return terrain.isTraversable();
    }

    /**
     * Verifica se um caçador pode se mover para uma localização específica.
     * Caçadores podem ocupar suas próprias casas e terrenos transitáveis.
     *
     * @param location A localização de destino.
     * @param hunter O caçador que está tentando se mover.
     * @return true se o movimento é permitido.
     */
    public boolean canHunterMoveTo(Location location, Hunter hunter) {
        // Primeiro verifica se a localização está dentro dos limites do campo
        if (!isWithinBounds(location)) {
            return false;
        }
        
        // Caçador pode sempre voltar para sua própria casa
        if (hunter.getHomeLocation().equals(location)) {
            return true;
        }
        
        // Verifica se a localização é casa de outro caçador
        if (isHunterHome(location) && !hunter.getHomeLocation().equals(location)) {
            return false;  // Não pode ocupar casa de outro caçador
        }
        
        // Obtém o tipo de terreno na localização desejada
        TerrainType terrain = getTerrainAt(location);
        // Verifica se o terreno é transitável
        return terrain.isTraversable();
    }

    /**
     * Verifica se uma localização é a casa de algum caçador.
     *
     * @param location A localização a verificar.
     * @return true se é uma casa de caçador.
     */
    public boolean isHunterHome(Location location) {
        return hunterHomes.contains(location);
    }

    /**
     * Registra uma casa de caçador no campo.
     *
     * @param location A localização da casa.
     */
    public void registerHunterHome(Location location) {
        if (!hunterHomes.contains(location)) {
            hunterHomes.add(location);
        }
    }

    /**
     * Remove o registro de uma casa de caçador.
     *
     * @param location A localização da casa a remover.
     */
    public void unregisterHunterHome(Location location) {
        hunterHomes.remove(location);
    }

    /**
     * Posiciona um animal no local dado.
     * Se já houver um ator lá, ele será perdido.
     *
     * @param animal O animal a ser posicionado.
     * @param row Coordenada da linha.
     * @param col Coordenada da coluna.
     */
    public void place(Animal animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Posiciona um ator no local dado.
     * Se já houver um ator lá, ele será perdido.
     *
     * @param actor O ator a ser posicionado.
     * @param location Onde posicionar o ator.
     */
    public void place(Actor actor, Location location) {
        if (!isWithinBounds(location)) {
            return;  // Fora dos limites - não posiciona
        }
        
        // Verifica se é um caçador para registrar sua casa
        if (actor instanceof Hunter) {
            Hunter hunter = (Hunter) actor;
            registerHunterHome(hunter.getHomeLocation());
        }
        
        // Verifica se pode posicionar baseado no tipo de ator
        if (actor instanceof Animal) {
            if (canAnimalMoveTo(location)) {
                field[location.getRow()][location.getCol()] = actor;
                if (actor != null) {
                    actor.setLocation(location);
                }
            }
        } else if (actor instanceof Hunter) {
            Hunter hunter = (Hunter) actor;
            if (canHunterMoveTo(location, hunter)) {
                field[location.getRow()][location.getCol()] = actor;
                if (actor != null) {
                    actor.setLocation(location);
                }
            }
        }
    }

    /**
     * Posiciona um caçador no local dado.
     * Método específico para Hunter para melhor organização.
     *
     * @param hunter O caçador a ser posicionado.
     * @param location Onde posicionar o caçador.
     */
    public void placeHunter(Hunter hunter, Location location) {
        place(hunter, location);
    }

    /**
     * Remove um ator de uma localização específica.
     *
     * @param location A localização de onde remover o ator.
     */
    public void removeActor(Location location) {
        if (isWithinBounds(location)) {
            Actor actor = field[location.getRow()][location.getCol()];
            // Se for um caçador, remove o registro da casa
            if (actor instanceof Hunter) {
                Hunter hunter = (Hunter) actor;
                unregisterHunterHome(hunter.getHomeLocation());
            }
            field[location.getRow()][location.getCol()] = null;
        }
    }

    /**
     * Retorna o ator no local dado, se houver.
     *
     * @param location Onde no campo.
     * @return O ator no local, ou null se não houver.
     */
    public Actor getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Retorna o ator no local dado, se houver.
     *
     * @param row A linha desejada.
     * @param col A coluna desejada.
     * @return O ator no local, ou null se não houver.
     */
    public Actor getObjectAt(int row, int col) {
        if (isWithinBounds(new Location(row, col))) {
            return field[row][col];
        }
        return null;
    }

    /**
     * Retorna o tipo de terreno em uma localização específica.
     *
     * @param location A localização desejada.
     * @return O tipo de terreno na localização.
     */
    public TerrainType getTerrainAt(Location location) {
        if (!isWithinBounds(location)) {
            return TerrainType.WATER;  // Considera fora dos limites como água
        }
        return terrainGrid[location.getRow()][location.getCol()];
    }

    /**
     * Retorna o tipo de terreno em coordenadas específicas.
     *
     * @param row A linha desejada.
     * @param col A coluna desejada.
     * @return O tipo de terreno nas coordenadas.
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
        // Obtém as coordenadas atuais
        int row = location.getRow();
        int col = location.getCol();
        
        // Gera deslocamento aleatório: -1, 0 ou +1 para linha e coluna
        int nextRow = row + RandomGenerator.nextInt(3) - 1;
        int nextCol = col + RandomGenerator.nextInt(3) - 1;
        
        // Verifica se o novo local está fora dos limites do campo
        if (!isWithinBounds(new Location(nextRow, nextCol))) {
            // Se fora dos limites, retorna a localização original (fica no lugar)
            return location;
        }
        
        // Cria um objeto Location com as novas coordenadas
        Location newLocation = new Location(nextRow, nextCol);
        
        // Verifica se pode se mover para o novo local (terreno transitável)
        if (canAnimalMoveTo(newLocation)) {
            // Retorna o novo local se for transitável
            return newLocation;
        } else {
            // Se terreno intransitável, fica no lugar atual
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
        // Obtém um iterador com todas as localizações adjacentes (embaralhadas)
        Iterator<Location> adjacent = adjacentLocations(location);
        
        // Percorre todas as localizações adjacentes
        while (adjacent.hasNext()) {
            Location next = adjacent.next();
            // Verifica se a localização está vazia E é transitável
            if (field[next.getRow()][next.getCol()] == null && canAnimalMoveTo(next)) {
                // Retorna o primeiro local livre e transitável encontrado
                return next;
            }
        }
        
        // Se não encontrou adjacente livre, verifica se o local atual está livre
        if (field[location.getRow()][location.getCol()] == null && canAnimalMoveTo(location)) {
            // Retorna o próprio local se estiver livre
            return location;
        } else {
            // Retorna null se não há local livre algum
            return null;
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
        // Obtém as coordenadas atuais
        int row = location.getRow();
        int col = location.getCol();
        
        // Cria uma lista para armazenar as localizações adjacentes
        LinkedList<Location> locations = new LinkedList<>();
        
        // Percorre todas as direções ao redor (3x3 grid exceto o centro)
        for (int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            // Verifica se a linha está dentro dos limites
            if (nextRow >= 0 && nextRow < depth) {
                for (int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    // Verifica se coluna está dentro dos limites E não é a própria localização
                    if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        // Adiciona a localização válida à lista
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }
        
        // Embaralha a lista para ordem aleatória de verificação
        Collections.shuffle(locations, RandomGenerator.getRandom());
        // Retorna um iterador para percorrer as localizações
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
    
    /**
     * @return Lista de todas as casas de caçadores registradas.
     */
    public List<Location> getHunterHomes() {
        return new ArrayList<>(hunterHomes);  // Retorna cópia para evitar modificação externa
    }
}