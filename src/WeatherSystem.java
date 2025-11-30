/**
 * Sistema de clima que controla as estações do ano na simulação.
 * As estações afetam o comportamento dos animais e caçadores.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class WeatherSystem {
    private Season currentSeason;
    private int dayCounter;
    private static final int SEASON_DURATION = 100; // dias por estação

    /**
     * Cria um novo sistema de clima.
     */
    public WeatherSystem() {
        currentSeason = Season.SPRING;
        dayCounter = 0;
    }

    /**
     * Avança o tempo no sistema climático.
     * Muda as estações após um certo número de dias.
     */
    public void advanceTime() {
        dayCounter++;
        if (dayCounter >= SEASON_DURATION) {
            changeSeason();
            dayCounter = 0;
        }
    }

    /**
     * Muda para a próxima estação.
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
     * Retorna a estação atual.
     *
     * @return A estação atual.
     */
    public Season getCurrentSeason() {
        return currentSeason;
    }

    /**
     * Retorna o contador de dias da estação atual.
     *
     * @return O número de dias na estação atual.
     */
    public int getDayCounter() {
        return dayCounter;
    }
}