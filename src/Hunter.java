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
        // Define a localização da casa do caçador (ponto de retorno após caças)
        this.homeLocation = home;
        
        // Inicializa a localização atual na mesma posição da casa (nasce em casa)
        this.location = home;
        
        // Marca o caçador como vivo/ativo
        this.alive = true;
        
        // Inicializa o contador de animais caçados como zero
        this.kills = 0;
        
        // Define que o caçador está ativo (pode caçar)
        this.active = true;
        
        // Armazena referência às estatísticas para registrar caças
        this.stats = stats;
        
        // Inicializa como não estando voltando para casa (está em casa)
        this.returningHome = false;
        
        // Inicializa o contador de passos no inverno (para movimento mais lento)
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
        // Verifica se já está em casa
        if (location.equals(homeLocation)) {
            // Se já está em casa, apenas se posiciona no campo atualizado
            if (updatedField != null) {
                updatedField.placeHunter(this, homeLocation);
            }
            return true; // Já chegou em casa
        }
        
        // Calcula a diferença de posição entre localização atual e casa
        int rowDiff = homeLocation.getRow() - location.getRow();
        int colDiff = homeLocation.getCol() - location.getCol();
        
        // Começa com a posição atual
        int moveRow = location.getRow();
        int moveCol = location.getCol();
        
        // Decide a direção do movimento (prioriza o eixo com maior diferença)
        if (Math.abs(rowDiff) > Math.abs(colDiff)) {
            // Move na direção vertical (linha) - diferença maior nas linhas
            if (rowDiff > 0) moveRow++; // Casa está abaixo, move para baixo
            else if (rowDiff < 0) moveRow--; // Casa está acima, move para cima
        } else if (colDiff != 0) {
            // Move na direção horizontal (coluna) - diferença maior nas colunas
            if (colDiff > 0) moveCol++; // Casa está à direita, move para direita
            else if (colDiff < 0) moveCol--; // Casa está à esquerda, move para esquerda
        } else if (rowDiff != 0) {
            // Se colDiff é 0 mas rowDiff não é, move na vertical
            if (rowDiff > 0) moveRow++;
            else moveRow--;
        }
        
        // Cria a localização de destino ideal
        Location moveLoc = new Location(moveRow, moveCol);
        
        // Verifica se pode mover para a localização ideal
        if (currentField.canAnimalMoveTo(moveLoc) && 
            (currentField.getObjectAt(moveLoc) == null || 
            currentField.getObjectAt(moveLoc) == this)) {
            // Movimento bem-sucedido para a direção ideal
            location = moveLoc; // Atualiza a localização do caçador
            if (updatedField != null) {
                updatedField.placeHunter(this, moveLoc); // Posiciona no campo atualizado
            }
            // Retorna true se chegou em casa, false se ainda está a caminho
            return location.equals(homeLocation);
        } else {
            // Se não pode mover na direção ideal, tenta qualquer adjacente livre
            Location newLoc = currentField.freeAdjacentLocation(location);
            if (newLoc != null && !newLoc.equals(location)) {
                // Move para um local adjacente livre qualquer
                location = newLoc;
                if (updatedField != null) {
                    updatedField.placeHunter(this, newLoc);
                }
            } else {
                // Se não pode se mover para nenhum local, fica no lugar
                if (updatedField != null) {
                    updatedField.placeHunter(this, location);
                }
            }
            // Ainda não chegou em casa
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
        // Obtém todas as localizações adjacentes à posição atual (embaralhadas)
        Iterator<Location> adjacentLocs = currentField.adjacentLocations(location);
        
        // Procura por animais vivos nas posições adjacentes
        Location animalLocation = null;
        while (adjacentLocs.hasNext()) {
            Location loc = adjacentLocs.next();
            Object obj = currentField.getObjectAt(loc);
            
            // Verifica se encontrou um animal vivo
            if (obj instanceof Animal) {
                Animal prey = (Animal) obj;
                if (prey.isAlive()) {
                    animalLocation = loc; // Marca a localização da presa
                    break; // Para de procurar na primeira presa encontrada
                }
            }
        }
        
        // Se encontrou um animal para caçar
        if (animalLocation != null) {
            // Move o caçador para a localização do animal
            location = animalLocation;
            
            // Obtém o animal na localização (pode ser o mesmo ou outro devido à concorrência)
            Object obj = currentField.getObjectAt(animalLocation);
            if (obj instanceof Animal) {
                Animal prey = (Animal) obj;
                // Mata o animal
                prey.setDead();
                // Incrementa o contador pessoal de caças
                kills++;
                
                // Registra a caça nas estatísticas globais da simulação
                if (stats != null) {
                    stats.incrementHunterKills();
                }
            }
            
            // Posiciona o caçador no campo atualizado
            if (updatedField != null) {
                updatedField.placeHunter(this, animalLocation);
            }
            
            // Marca que deve voltar para casa após a caça
            returningHome = true;
            return; // Sai do método - caça concluída
        }
        
        // Se não encontrou animais para caçar, move-se aleatoriamente
        Location newLoc = currentField.freeAdjacentLocation(location);
        if (newLoc != null && !newLoc.equals(location)) {
            // Move para um local adjacente livre
            location = newLoc;
            if (updatedField != null) {
                updatedField.placeHunter(this, newLoc);
            }
        } else {
            // Se não pode se mover, fica no lugar atual
            if (updatedField != null) {
                updatedField.placeHunter(this, location);
            }
        }
        // Continua caçando no próximo passo (não marca returningHome)
    }
}