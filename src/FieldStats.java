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
        // Configura uma coleção para contadores de cada tipo de animal que
        // podemos encontrar
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
        StringBuffer buffer = new StringBuffer();
        if (!countsValid) {
            generateCounts(field);
        }
        
        Iterator<Class<?>> keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter info = counters.get(keys.next());
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        
        // Contar caçadores vivos no campo
        int hunterCount = 0;
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = field.getObjectAt(row, col);
                if (actor instanceof Hunter && actor.isAlive()) {
                    hunterCount++;
                }
            }
        }
        
        // Adicionar contagem de caçadores na população
        if (hunterCount > 0) {
            buffer.append("Caçadores: ").append(hunterCount).append(" ");
        }
        
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
     * NOTA: NÃO reseta o contador de caças (hunterKills) - ele é acumulativo.
     */
    public void reset() {
        countsValid = false;
        Iterator<Class<?>> keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter cnt = counters.get(keys.next());
            cnt.reset();
        }
        // NÃO resetar hunterKills - ele deve ser acumulativo durante toda a simulação
        // resetHunterKills();
    }

    /**
     * Incrementa a contagem para uma classe de animal.
     *
     * @param animalClass A classe do animal a incrementar.
     */
    public void incrementCount(Class<?> animalClass) {
        Counter cnt = counters.get(animalClass);
        if (cnt == null) {
            // ainda não temos um contador para esta espécie - crie um
            cnt = new Counter(animalClass.getName());
            counters.put(animalClass, cnt);
        }
        cnt.increment();
    }

    /**
     * Indica que uma contagem de animais foi concluída.
     */
    public void countFinished() {
        countsValid = true;
    }

    /**
     * Determina se a simulação ainda é viável.
     * Ou seja, se deve continuar a ser executada.
     *
     * @param field O campo da simulação.
     * @return true Se houver mais de uma espécie viva ou se houver caçadores ativos.
     */
    public boolean isViable(Field field) {
        // Verificar se há caçadores vivos (se houver, simulação continua)
        boolean hasHunters = false;
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = field.getObjectAt(row, col);
                if (actor instanceof Hunter && actor.isAlive()) {
                    hasHunters = true;
                    break;
                }
            }
            if (hasHunters) break;
        }
        
        // Se há caçadores, simulação continua
        if (hasHunters) {
            return true;
        }
        
        // Quantas contagens são diferentes de zero.
        int nonZero = 0;
        if (!countsValid) {
            generateCounts(field);
        }
        
        Iterator<Class<?>> keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter info = counters.get(keys.next());
            if (info.getCount() > 0) {
                nonZero++;
            }
        }
        
        return nonZero > 1;
    }

    /**
     * Gera contagens do número de animais.
     * Elas não são mantidas atualizadas conforme animais
     * são colocados no campo, mas apenas quando uma solicitação
     * é feita pela informação.
     *
     * @param field O campo da simulação.
     */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = field.getObjectAt(row, col);
                if (actor instanceof Animal) {
                    Animal animal = (Animal) actor;
                    incrementCount(animal.getClass());
                }
            }
        }
        countsValid = true;
    }
}