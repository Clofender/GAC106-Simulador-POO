/**
 * Representa uma localização em uma grade retangular.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2025
 */
public class Location {
    // Posições de linha e coluna.
    private final int row;
    private final int col;

    /**
     * Representa uma linha e coluna.
     * @param row A linha.
     * @param col A coluna.
     */
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    /**
     * Implementa igualdade de conteúdo.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        }
        else {
            return false;
        }
    }
    
    /**
     * Retorna uma string no formato linha,coluna
     * @return Uma representação em string da localização.
     */
    @Override
    public String toString() {
        return row + "," + col;
    }
    
    /**
     * Usa os 16 bits superiores para o valor da linha e os inferiores para
     * a coluna. Exceto para grades muito grandes, isso deve dar um
     * código hash único para cada par (linha, coluna).
     */
    @Override
    public int hashCode() {
        return (row << 16) + col;
    }
    
    /**
     * @return a linha.
     */
    public int getRow() {
        return row;
    }
    
    /**
     * @return a coluna.
     */
    public int getCol() {
        return col;
    }
}