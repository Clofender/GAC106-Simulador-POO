/**
 * Interface que define o contrato básico para todos os atores na simulação.
 * Um ator é qualquer entidade que pode realizar ações no campo.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public interface Actor {
    
    /**
     * Executa uma ação do ator no passo atual da simulação.
     *
     * @param currentField O campo atual (estado do passo atual).
     * @param updatedField O campo onde o ator deve ser atualizado.
     * @param newActors Lista onde novos atores (nascimentos, criações, etc.) devem ser adicionados.
     */
    void act(Field currentField, Field updatedField, java.util.List<Actor> newActors);
    
    /**
     * Verifica se o ator ainda está ativo (vivo) na simulação.
     *
     * @return true se o ator ainda está vivo/ativo.
     */
    boolean isAlive();
    
    /**
     * Define a localização atual do ator no campo.
     *
     * @param location A nova localização do ator.
     */
    void setLocation(Location location);
    
    /**
     * Obtém a localização atual do ator.
     *
     * @return A localização atual do ator.
     */
    Location getLocation();
}