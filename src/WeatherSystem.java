/**
 * Classe responsável por gerenciar o clima e as estações do ano na simulação.
 * A estação muda ciclicamente a cada número fixo de passos.
 * * @author TP_Grupo08
 * 
 * @version 2025
 */
public class WeatherSystem {
    // Duração de cada estação em passos (steps)
    private static final int SEASON_DURATION = 50;

    // A estação atual
    private Season currentSeason;
    // Contador de passos na estação atual
    private int stepsInSeason;

    /**
     * Construtor do sistema climático.
     * Inicia na Primavera.
     */
    public WeatherSystem() {
        this.currentSeason = Season.SPRING;
        this.stepsInSeason = 0;
    }

    /**
     * Avança o tempo no sistema climático.
     * Verifica se é hora de mudar de estação.
     */
    public void advanceTime() {
        stepsInSeason++;
        if (stepsInSeason >= SEASON_DURATION) {
            changeSeason();
            stepsInSeason = 0;
        }
    }

    /**
     * Altera a estação atual para a próxima no ciclo:
     * Primavera -> Verão -> Outono -> Inverno -> Primavera.
     */
    private void changeSeason() {
        switch (currentSeason) {
            case SPRING:
                currentSeason = Season.SUMMER;
                break;
            case SUMMER:
                currentSeason = Season.AUTUMN;
                break;
            case AUTUMN:
                currentSeason = Season.WINTER;
                break;
            case WINTER:
                currentSeason = Season.SPRING;
                break;
        }
    }

    /**
     * Obtém a estação atual.
     * 
     * @return A estação vigente.
     */
    public Season getCurrentSeason() {
        return currentSeason;
    }
}