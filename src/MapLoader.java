import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe responsável por carregar mapas de terreno a partir de arquivos de texto.
 * Os mapas definem a disposição dos diferentes tipos de terreno no campo.
 * 
 * @author Gustavo Alessandro de Souza Sabino
 * @version 2025
 */
public class MapLoader {
    
    /**
     * Carrega um mapa de terreno a partir de um arquivo.
     * 
     * @param filename Nome do arquivo contendo o mapa
     * @param width Largura esperada do mapa
     * @param depth Profundidade esperada do mapa
     * @return Matriz com os tipos de terreno carregados
     */
    public static TerrainType[][] loadMap(String filename, int width, int depth) {
        TerrainType[][] terrainMap = new TerrainType[depth][width];
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            
            while ((line = reader.readLine()) != null && row < depth) {
                line = line.trim();
                String[] characteres = line.split("\\s+");
                
                if (characteres.length != width) {
                    throw new IllegalArgumentException("Largura do mapa na linha " + (row + 1) + 
                                                     " não corresponde à largura esperada: " + 
                                                     characteres.length + " vs " + width);
                }
                
                for (int col = 0; col < width; col++) {
                    char terrainChar = characteres[col].charAt(0);
                    terrainMap[row][col] = TerrainType.fromChar(terrainChar);
                }
                row++;
            }
            
            if (row != depth) {
                throw new IllegalArgumentException("Profundidade do mapa não corresponde à profundidade esperada: " + 
                                                 row + " vs " + depth);
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar mapa: " + e.getMessage());
            return createDefaultMap(width, depth);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro no formato do mapa: " + e.getMessage());
            return createDefaultMap(width, depth);
        }
        
        return terrainMap;
    }
    
    /**
     * Cria um mapa padrão composto apenas por grama.
     */
    private static TerrainType[][] createDefaultMap(int width, int depth) {
        TerrainType[][] defaultMap = new TerrainType[depth][width];
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                defaultMap[row][col] = TerrainType.GRASS;
            }
        }
        return defaultMap;
    }
}