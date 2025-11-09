import java.util.HashMap;
import java.util.Iterator;

/**
 * Esta classe coleta e fornece alguns dados estatísticos sobre o estado
 * de um campo. É flexível: ela criará e manterá um contador
 * para qualquer classe de objeto que for encontrada dentro do campo.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public class FieldStats {
    // Contadores para cada tipo de entidade (raposa, coelho, etc.) na simulação.
    private HashMap<Class<?>, Counter> counters;
    // Se os contadores estão atualmente atualizados.
    private boolean countsValid;

    /**
     * Constrói um objeto de estatísticas do campo.
     */
    public FieldStats() {
        // Configura uma coleção para contadores de cada tipo de animal que
        // podemos encontrar
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
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
        return buffer.toString();
    }

    /**
     * Invalida o conjunto atual de estatísticas; reseta todas
     * as contagens para zero.
     */
    public void reset() {
        countsValid = false;
        Iterator<Class<?>> keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter cnt = counters.get(keys.next());
            cnt.reset();
        }
    }

    /**
     * Incrementa a contagem para uma classe de animal.
     * * @param animalClass A classe do animal a incrementar.
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
     * * @param field O campo da simulação.
     * * @return true Se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field) {
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
     * Gera contagens do número de raposas e coelhos.
     * Elas não são mantidas atualizadas conforme raposas e coelhos
     * são colocados no campo, mas apenas quando uma solicitação
     * é feita pela informação.
     * * @param field O campo da simulação.
     */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getObjectAt(row, col);
                if (animal != null) {
                    incrementCount(animal.getClass());
                }
            }
        }
        countsValid = true;
    }
}