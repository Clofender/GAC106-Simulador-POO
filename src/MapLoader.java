import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe responsável por carregar mapas de terreno a partir de arquivos de texto.
 * Os mapas definem a disposição dos diferentes tipos de terreno no campo.
 * 
 * @author TP_Grupo08 (modificações)
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
        // Criar matriz para armazenar o mapa carregado
        TerrainType[][] terrainMap = new TerrainType[depth][width];
        
        // Usar try-with-resources para garantir fechamento do arquivo
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;  // Variável para cada linha do arquivo
            int row = 0;  // Contador de linhas lidas
            
            // Ler cada linha do arquivo até o fim ou atingir a profundidade esperada
            while ((line = reader.readLine()) != null && row < depth) {
                line = line.trim();  // Remover espaços em branco do início/fim
                String[] characteres = line.split("\\s+");  // Dividir por espaços (um ou mais)
                
                // Verificar se a linha tem a largura esperada
                if (characteres.length != width) {
                    throw new IllegalArgumentException("Largura do mapa na linha " + (row + 1) + 
                                                     " não corresponde à largura esperada: " + 
                                                     characteres.length + " vs " + width);
                }
                
                // Processar cada caractere da linha
                for (int col = 0; col < width; col++) {
                    char terrainChar = characteres[col].charAt(0);  // Primeiro caractere de cada token
                    terrainMap[row][col] = TerrainType.fromChar(terrainChar);  // Converter char para TerrainType
                }
                row++;  // Avançar para próxima linha
            }
            
            // Verificar se leu todas as linhas esperadas
            if (row != depth) {
                throw new IllegalArgumentException("Profundidade do mapa não corresponde à profundidade esperada: " + 
                                                 row + " vs " + depth);
            }
            
        } catch (IOException e) {
            // Erro de leitura do arquivo - usar mapa padrão
            System.err.println("Erro ao carregar mapa: " + e.getMessage());
            return createDefaultMap(width, depth);
        } catch (IllegalArgumentException e) {
            // Erro no formato do arquivo - usar mapa padrão
            System.err.println("Erro no formato do mapa: " + e.getMessage());
            return createDefaultMap(width, depth);
        }
        
        return terrainMap;  // Retornar mapa carregado com sucesso
    }
    
    /**
     * Cria um mapa padrão composto apenas por grama.
     */
    private static TerrainType[][] createDefaultMap(int width, int depth) {
        TerrainType[][] defaultMap = new TerrainType[depth][width];  // Criar matriz vazia
        // Preencher toda a matriz com grama
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                defaultMap[row][col] = TerrainType.GRASS;  // Todas as posições são grama
            }
        }
        return defaultMap;  // Retornar mapa padrão
    }
}