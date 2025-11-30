/**
 * Enumeração que representa as estações do ano na simulação.
 * Cada estação possui características que influenciam o comportamento dos
 * atores.
 * * @author TP_Grupo08
 * 
 * @version 2025
 */
public enum Season {
    SPRING("Primavera", 1.2), // Aumenta reprodução/comida
    SUMMER("Verão", 1.0), // Normal
    AUTUMN("Outono", 0.8), // Diminui um pouco
    WINTER("Inverno", 0.4); // Diminui drasticamente

    // Nome legível da estação para exibição
    private final String name;
    // Fator que multiplicará as taxas de reprodução
    private final double breedingFactor;

    /**
     * Construtor da estação.
     * 
     * @param name           Nome da estação em português.
     * @param breedingFactor Fator de influência na reprodução.
     */
    Season(String name, double breedingFactor) {
        this.name = name;
        this.breedingFactor = breedingFactor;
    }

    /**
     * @return O nome da estação.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * @return O fator de reprodução da estação.
     */
    public double getBreedingFactor() {
        return breedingFactor;
    }
}