/**
 * Sistema de clima que controla as estações do ano na simulação.
 * As estações afetam o comportamento dos animais e caçadores.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class WeatherSystem {
    // Estação atual do sistema climático
    private Season currentSeason;
    // Contador de dias dentro da estação atual
    private int dayCounter;
    // Duração fixa de cada estação em dias de simulação
    private static final int SEASON_DURATION = 100; // dias por estação

    /**
     * Cria um novo sistema de clima.
     */
    public WeatherSystem() {
        // Inicia na primavera (estação de renovação)
        currentSeason = Season.SPRING;
        // Começa no dia 0 da estação
        dayCounter = 0;
    }

    /**
     * Avança o tempo no sistema climático.
     * Muda as estações após um certo número de dias.
     */
    public void advanceTime() {
        // Incrementa o contador de dias
        dayCounter++;
        // Verifica se chegou ao final da estação atual
        if (dayCounter >= SEASON_DURATION) {
            // Muda para a próxima estação
            changeSeason();
            // Reinicia o contador de dias
            dayCounter = 0;
        }
    }

    /**
     * Muda para a próxima estação.
     */
    private void changeSeason() {
        // Usa switch para transição cíclica das estações
        switch (currentSeason) {
            case SPRING:
                // Primavera para Verão
                currentSeason = Season.SUMMER;
                break;
            case SUMMER:
                // Verão para  Outono  
                currentSeason = Season.AUTUMN;
                break;
            case AUTUMN:
                // Outono para  Inverno
                currentSeason = Season.WINTER;
                break;
            case WINTER:
                // Inverno para Primavera (ciclo completo)
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