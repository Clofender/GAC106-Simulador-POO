import java.util.Iterator;
import java.util.List;

/**
 * Representa um caçador na simulação.
 * O caçador é um predador universal que pode caçar qualquer animal.
 * Possui comportamento único de retornar à sua casa após cada caça.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class Hunter implements Actor {
    
    // Localização atual do caçador
    private Location location;
    
    // Localização da casa do caçador (para onde retorna após caçar)
    private final Location homeLocation;
    
    // Número de animais caçados
    private int kills;
    
    // Se o caçador está ativo (pode caçar)
    private boolean active;
    
    // Se o caçador está vivo
    private boolean alive;
    
    // Referência às estatísticas para registrar caças
    private final FieldStats stats;
    
    // Flag para indicar se está voltando para casa após uma caça
    private boolean returningHome;
    
    // Contador de passos no inverno (para movimento mais lento)
    private int winterStepCounter;

    /**
     * Cria um novo caçador.
     *
     * @param home A localização da casa do caçador.
     * @param stats Referência às estatísticas da simulação.
     */
    public Hunter(Location home, FieldStats stats) {
        this.homeLocation = home;
        this.location = home;
        this.alive = true;
        this.kills = 0;
        this.active = true;
        this.stats = stats;
        this.returningHome = false;
        this.winterStepCounter = 0;
    }

    /**
     * Executa a ação do caçador no passo atual.
     * O caçador caça durante todas as estações exceto inverno.
     *
     * @param currentField O campo atual.
     * @param updatedField O campo atualizado.
     * @param newActors Lista para novos atores (não utilizado por Hunter).
     */
    @Override
    public void act(Field currentField, Field updatedField, List<Actor> newActors) {
        if (!alive) return;

        // Verificar se pode caçar (não caça no inverno)
        if (!active) {
            // No inverno: comportamento especial - fica em casa ou volta para casa
            winterStepCounter++;
            
            // No inverno, move mais devagar (a cada 2 passos)
            if (winterStepCounter % 2 == 0) {
                if (!location.equals(homeLocation)) {
                    // Tenta voltar para casa
                    moveTowardsHome(currentField, updatedField);
                } else {
                    // Já está em casa, fica lá
                    if (updatedField != null) {
                        updatedField.placeHunter(this, homeLocation);
                    }
                }
            } else {
                // Em passos ímpares, não se move no inverno
                if (updatedField != null) {
                    updatedField.placeHunter(this, location);
                }
            }
            return;
        }

        // Fora do inverno: comportamento normal de caça
        winterStepCounter = 0; // Reseta contador do inverno

        // Se está voltando para casa após uma caça
        if (returningHome) {
            if (location.equals(homeLocation)) {
                // Chegou em casa, pode caçar novamente
                returningHome = false;
                goHunting(currentField, updatedField);
            } else {
                // Ainda voltando para casa, continuar voltando
                moveTowardsHome(currentField, updatedField);
            }
            return;
        }

        // Tentar caçar normalmente
        goHunting(currentField, updatedField);
    }

    /**
     * Move o caçador 1 bloco em direção à casa.
     *
     * @param currentField O campo atual.
     * @param updatedField O campo atualizado para posicionamento.
     * @return true se chegou em casa, false se ainda está a caminho.
     */
    private boolean moveTowardsHome(Field currentField, Field updatedField) {
        // Se já está em casa, não precisa mover
        if (location.equals(homeLocation)) {
            if (updatedField != null) {
                updatedField.placeHunter(this, homeLocation);
            }
            return true;
        }
        
        // Calcular direção para casa (mover 1 bloco por step)
        int rowDiff = homeLocation.getRow() - location.getRow();
        int colDiff = homeLocation.getCol() - location.getCol();
        
        int moveRow = location.getRow();
        int moveCol = location.getCol();
        
        // Mover 1 bloco na direção da casa (priorizar maior diferença)
        if (Math.abs(rowDiff) > Math.abs(colDiff)) {
            // Mover na direção da linha
            if (rowDiff > 0) moveRow++;
            else if (rowDiff < 0) moveRow--;
        } else if (colDiff != 0) {
            // Mover na direção da coluna
            if (colDiff > 0) moveCol++;
            else if (colDiff < 0) moveCol--;
        } else if (rowDiff != 0) {
            // Se colDiff é 0, mover na linha
            if (rowDiff > 0) moveRow++;
            else moveRow--;
        }
        
        Location moveLoc = new Location(moveRow, moveCol);
        if (currentField.canAnimalMoveTo(moveLoc) && 
            (currentField.getObjectAt(moveLoc) == null || 
             currentField.getObjectAt(moveLoc) == this)) {
            location = moveLoc;
            if (updatedField != null) {
                updatedField.placeHunter(this, moveLoc);
            }
            return location.equals(homeLocation);
        } else {
            // Se não pode mover na direção ideal, tentar adjacente livre
            Location newLoc = currentField.freeAdjacentLocation(location);
            if (newLoc != null && !newLoc.equals(location)) {
                location = newLoc;
                if (updatedField != null) {
                    updatedField.placeHunter(this, newLoc);
                }
            } else {
                // Se não pode se mover, ficar no lugar
                if (updatedField != null) {
                    updatedField.placeHunter(this, location);
                }
            }
            return false;
        }
    }

    /**
     * Verifica se o caçador está vivo.
     *
     * @return true se o caçador está vivo.
     */
    @Override
    public boolean isAlive() {
        return alive;
    }

    /**
     * Define a localização do caçador.
     *
     * @param location A nova localização.
     */
    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Obtém a localização atual do caçador.
     *
     * @return A localização atual.
     */
    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * Obtém a localização da casa do caçador.
     *
     * @return A localização da casa.
     */
    public Location getHomeLocation() {
        return homeLocation;
    }

    /**
     * Obtém o número de animais caçados.
     *
     * @return O número de caças.
     */
    public int getKills() {
        return kills;
    }

    /**
     * Define se o caçador está ativo.
     *
     * @param active true para ativar o caçador.
     */
    public void setActive(boolean active) {
        this.active = active;
        // Se foi desativado (inverno) e não está em casa, marcar para voltar
        if (!active && !location.equals(homeLocation)) {
            returningHome = true;
        }
    }

    /**
     * Lógica de caça: o caçador procura por um animal nas adjacências.
     */
    private void goHunting(Field currentField, Field updatedField) {
        Iterator<Location> adjacentLocs = currentField.adjacentLocations(location);
        
        Location animalLocation = null;
        while (adjacentLocs.hasNext()) {
            Location loc = adjacentLocs.next();
            Object obj = currentField.getObjectAt(loc);
            
            if (obj instanceof Animal) {
                Animal prey = (Animal) obj;
                if (prey.isAlive()) {
                    animalLocation = loc;
                    break;
                }
            }
        }
        
        if (animalLocation != null) {
            location = animalLocation;
            
            Object obj = currentField.getObjectAt(animalLocation);
            if (obj instanceof Animal) {
                Animal prey = (Animal) obj;
                prey.setDead();
                kills++;
                
                if (stats != null) {
                    stats.incrementHunterKills();
                }
            }
            
            if (updatedField != null) {
                updatedField.placeHunter(this, animalLocation);
            }
            
            returningHome = true;
            return;
        }
        
        Location newLoc = currentField.freeAdjacentLocation(location);
        if (newLoc != null && !newLoc.equals(location)) {
            location = newLoc;
            if (updatedField != null) {
                updatedField.placeHunter(this, newLoc);
            }
        } else {
            if (updatedField != null) {
                updatedField.placeHunter(this, location);
            }
        }
    }

    /**
     * Define o caçador como morto.
     */
    public void setDead() {
        alive = false;
    }
}