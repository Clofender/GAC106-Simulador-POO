/**
 * Enum que representa os diferentes tipos de terreno no campo de simulação.
 * Cada tipo tem propriedades específicas como transponibilidade e cor.
 * 
 * @author TP_Grupo08 (modificações)
 * @version 2025
 */
public enum TerrainType {
    /**
     * Água - terreno não ocupável para todos os animais
     */
    WATER('W', false, java.awt.Color.BLUE),  // 'W' no arquivo, azul na interface, bloqueia todos
    
    /**
     * Grama - terreno padrão, transitável por todos os animais
     */
    GRASS('G', true, java.awt.Color.GREEN),  // 'G' no arquivo, verde na interface, todos passam
    
    /**
     * Árvore - terreno intransponível para todos os animais
     */
    TREE('T', false, java.awt.Color.GREEN.darker().darker());  // 'T' no arquivo, verde escuro, bloqueia todos

    // Símbolo usado para representar este terreno em arquivos .txt
    private final char symbol;
    // Indica se animais podem se mover através deste terreno
    private final boolean traversable;
    // Cor visual usada para representar o terreno na interface gráfica
    private final java.awt.Color color;

    /**
     * Construtor do tipo de terreno.
     * 
     * @param symbol Símbolo que representa o terreno no arquivo de mapa (.txt)
     * @param traversable Se o terreno pode ser atravessado por animais
     * @param color Cor visual do terreno na interface (usado no SimulatorView)
     */
    TerrainType(char symbol, boolean traversable, java.awt.Color color) {
        this.symbol = symbol;
        this.traversable = traversable;
        this.color = color;
    }

    /**
     * Verifica se animais podem atravessar este terreno.
     * 
     * @return true se o terreno pode ser atravessado por animais
     */
    public boolean isTraversable() {
        return traversable;
    }

    /**
     * Retorna a cor usada para desenhar este terreno na interface.
     * 
     * @return A cor visual do terreno
     */
    public java.awt.Color getColor() {
        return color;
    }

    /**
     * Retorna o caractere que representa este terreno em arquivos.
     * 
     * @return O símbolo que representa o terreno no arquivo
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Converte um caractere em seu respectivo tipo de terreno.
     * Usado pelo MapLoader para ler arquivos de mapa.
     * 
     * @param c O caractere a ser convertido (W, G, T)
     * @return O tipo de terreno correspondente
     * @throws Se caractere não reconhecido, retorna GRASS como padrão
     */
    public static TerrainType fromChar(char c) {
        // Percorre todos os valores do enum
        for (TerrainType type : values()) {
            // Verifica se o símbolo corresponde
            if (type.getSymbol() == c) {
                return type;
            }
        }
        // Fallback: retorna grama se caractere não for reconhecido
        return GRASS;
    }
}