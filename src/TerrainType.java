/**
 * Enum que representa os diferentes tipos de terreno no campo de simulação.
 * Cada tipo tem propriedades específicas como transponibilidade e cor.
 * 
 * @author Gustavo Alessandro de Souza Sabino
 * @version 2025
 */
public enum TerrainType {
    /**
     * Água - terreno não ocupável para todos os animais
     */
    WATER('W', false, java.awt.Color.BLUE),
    
    /**
     * Grama - terreno padrão, transitável por todos os animais
     */
    GRASS('G', true, java.awt.Color.GREEN),
    
    /**
     * Árvore - terreno intransponível para todos os animais
     */
    TREE('T', false, java.awt.Color.GREEN.darker().darker());

    private final char symbol;
    private final boolean traversable;
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
     * @return true se o terreno pode ser atravessado por animais
     */
    public boolean isTraversable() {
        return traversable;
    }

    /**
     * @return A cor visual do terreno
     */
    public java.awt.Color getColor() {
        return color;
    }

    /**
     * @return O símbolo que representa o terreno no arquivo
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Converte um caractere em seu respectivo tipo de terreno.
     * 
     * @param c O caractere a ser convertido
     * @return O tipo de terreno correspondente
     */
    public static TerrainType fromChar(char c) {
        for (TerrainType type : values()) {
            if (type.getSymbol() == c) {
                return type;
            }
        }
        return GRASS;
    }
}