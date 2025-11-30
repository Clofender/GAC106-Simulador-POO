import java.util.List;


public interface Actor
{
    /**
     * Executa uma ação do ator no passo atual da simulação.
     * 
     * @param currentField O campo atual (estado do passo atual).
     * @param updatedField O campo onde o ator deve ser atualizado.
     * @param newActors Lista onde novos atores (nascimentos, criações, etc.) devem ser adicionados.
     */
    void act(Field currentField, Field updatedField, List<Actor> newActors,WeatherSystem weather);

    /**
     * @return true se o ator ainda está vivo.
     */
    boolean isAlive();

    /**
     * Seta a localização atual do ator.
     */
    void setLocation(Location location);

    /**
     * @return a localização atual do ator.
     */
    Location getLocation();
}
