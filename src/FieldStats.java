import java.util.HashMap;
import java.util.Iterator;

/**
 * Esta classe coleta e fornece alguns dados estatísticos sobre o estado
 * de um campo. É flexível: ela criará e manterá um contador
 * para qualquer classe de objeto que for encontrada dentro do campo.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author TP_Grupo08 (expansão para estatísticas de caçador)
 * @version 2025
 */
public class FieldStats {
    
    // Contadores para cada tipo de entidade (raposa, coelho, etc.) na simulação.
    private final HashMap<Class<?>, Counter> counters;
    
    // Contador de caças realizadas pelo Hunter
    private int hunterKills;
    
    // Se os contadores estão atualmente atualizados.
    private boolean countsValid;

    /**
     * Constrói um objeto de estatísticas do campo.
     */
    public FieldStats() {
        counters = new HashMap<>();
        hunterKills = 0;
        countsValid = true;
    }

    /**
     * Obtém os detalhes da população atual no campo.
     * Inclui contagem de todos os animais e estatísticas do caçador.
     *
     * @param field O campo da simulação.
     * @return Uma string descrevendo quais animais estão no campo.
     */
    public String getPopulationDetails(Field field) {
        // Cria um buffer para construir a string de resultado
        StringBuffer buffer = new StringBuffer();
        
        // Verifica se as contagens estão atualizadas, se não, gera novas contagens
        if (!countsValid) {
            generateCounts(field);
        }
        
        // Obtém um iterador para percorrer todas as classes de animais contadas
        Iterator<Class<?>> keys = counters.keySet().iterator();
        
        // Percorre todas as espécies de animais contabilizadas
        while (keys.hasNext()) {
            // Obtém o contador para a espécie atual
            Counter info = counters.get(keys.next());
            
            // Adiciona o nome da espécie ao resultado
            buffer.append(info.getName());
            buffer.append(": ");
            
            // Adiciona a contagem da espécie ao resultado
            buffer.append(info.getCount());
            buffer.append(' '); // Espaço separador entre espécies
        }
        
        // Conta manualmente os caçadores vivos no campo
        int hunterCount = 0;
        
        // Percorre todo o campo linha por linha
        for (int row = 0; row < field.getDepth(); row++) {
            // Percorre todo o campo coluna por coluna
            for (int col = 0; col < field.getWidth(); col++) {
                // Obtém o ator na posição atual
                Actor actor = field.getObjectAt(row, col);
                
                // Verifica se é um caçador e se está vivo
                if (actor instanceof Hunter && actor.isAlive()) {
                    // Incrementa o contador de caçadores
                    hunterCount++;
                }
            }
        }
        
        // Se há caçadores vivos, adiciona essa informação ao resultado
        if (hunterCount > 0) {
            buffer.append("Caçadores: ").append(hunterCount).append(" ");
        }
        
        // Retorna a string completa com todas as informações populacionais
        return buffer.toString();
    }

    /**
     * Incrementa o contador de caças do Hunter.
     */
    public void incrementHunterKills() {
        hunterKills++;
    }

    /**
     * Obtém o número total de caças realizadas pelo Hunter.
     *
     * @return O número de caças.
     */
    public int getHunterKills() {
        return hunterKills;
    }

    /**
     * Reseta o contador de caças do Hunter.
     */
    public void resetHunterKills() {
        hunterKills = 0;
    }

   /**
     * Invalida o conjunto atual de estatísticas; reseta todas
     * as contagens para zero.
     */
    public void reset() {
        // Marca que as contagens estão desatualizadas/inválidas
        countsValid = false;
        
        // Obtém um iterador para percorrer todas as classes de animais registradas
        Iterator<Class<?>> keys = counters.keySet().iterator();
        
        // Percorre todos os contadores existentes
        while (keys.hasNext()) {
            // Obtém o contador para a próxima classe de animal
            Counter cnt = counters.get(keys.next());
            
            // Reseta a contagem desse contador para zero
            cnt.reset();
        }
        // OBS: Não reseta hunterKills - ele é acumulativo durante toda a simulação
    }

    /**
     * Incrementa a contagem para uma classe de animal.
     *
     * @param animalClass A classe do animal a incrementar.
     */
    public void incrementCount(Class<?> animalClass) {
        // Tenta obter o contador existente para esta classe de animal
        Counter cnt = counters.get(animalClass);
        
        // Verifica se não existe contador para esta classe
        if (cnt == null) {
            // Cria um novo contador com o nome da classe como identificador
            cnt = new Counter(animalClass.getName());
            
            // Adiciona o novo contador ao mapa de contadores
            counters.put(animalClass, cnt);
        }
        
        // Incrementa a contagem deste animal (seja contador novo ou existente)
        cnt.increment();
    }

    /**
     * Indica que uma contagem de animais foi concluída.
     */
    public void countFinished() {
        countsValid = true;
    }

    /**
     * Determina se a simulação deve continuar a ser executada.
     * A simulação continua se houver pelo menos um animal vivo no campo.
     * Caçadores não influenciam na decisão - são independentes.
     *
     * @param field O campo da simulação.
     * @return true Se houver pelo menos um animal vivo no campo.
     */
    public boolean isViable(Field field) {
        // Percorre todo o campo procurando por animais vivos
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = field.getObjectAt(row, col);
                // Verifica se é um animal E se está vivo
                if (actor instanceof Animal && actor.isAlive()) {
                    // Encontrou pelo menos um animal vivo - simulação continua
                    return true;
                }
            }
        }
        
        // Se chegou aqui, não encontrou nenhum animal vivo, a simulação para
        return false;
    }

    /**
     * Gera contagens do número de animais.
     * Percorre todo o campo e conta cada animal vivo por espécie.
     */
    private void generateCounts(Field field) {
        // Reseta todas as contagens anteriores
        reset();
        
        // Percorre todo o campo célula por célula
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = field.getObjectAt(row, col);
                
                // Verifica se é um animal (não caçador)
                if (actor instanceof Animal) {
                    Animal animal = (Animal) actor;
                    // Incrementa o contador da espécie específica deste animal
                    incrementCount(animal.getClass());
                }
            }
        }
        
        // Marca que as contagens agora estão válidas e atualizadas
        countsValid = true;
    }
}