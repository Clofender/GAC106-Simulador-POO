import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * Um simulador simples de predador-presa, baseado em um campo
 * contendo coelhos e raposas.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public class Simulator {
    // --- Constantes de Configuração ---
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_DEPTH = 50;
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;

    // --- Campos de Instância ---

    // A lista de animais no campo
    private List<Animal> animals;
    // A lista de animais recém-nascidos
    private List<Animal> newAnimals;

    // O estado atual do campo.
    private Field field;
    // Um segundo campo, usado para construir o próximo estágio.
    private Field updatedField;
    // O passo atual da simulação.
    private int step;
    // Uma visão gráfica da simulação.
    private SimulatorView view;

    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Cria um campo de simulação com o tamanho dado.
     * * @param depth Profundidade do campo.
     * * @param width Largura do campo.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<Animal>();
        newAnimals = new ArrayList<Animal>();

        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Cria a visão
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);

        // Configura um ponto de partida válido.
        reset();
    }

    /**
     * Executa a simulação por um longo período (500 passos).
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Executa a simulação pelo número de passos dado.
     * Para antes se a simulação não for mais viável.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }

    /**
     * Executa um único passo da simulação.
     * Itera por todo o campo atualizando o estado de cada animal.
     */
    public void simulateOneStep() {
        step++;
        newAnimals.clear();

        // Permite que todos os animais ajam.
        for (Iterator<Animal> iter = animals.iterator(); iter.hasNext();) {
            Animal animal = iter.next();
            if (animal.isAlive()) {
                animal.act(field, updatedField, newAnimals);
            } else {
                iter.remove(); // Remove animais mortos da coleção
            }
        }

        // Adiciona animais recém-nascidos à lista principal
        animals.addAll(newAnimals);

        // Troca os campos no final do passo.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // exibe o novo campo na tela
        view.showStatus(step, field);
    }

    /**
     * Reseta a simulação para a posição inicial.
     */
    public void reset() {
        step = 0;
        animals.clear();
        field.clear();
        updatedField.clear();
        populate(field);

        // Mostra o estado inicial na visão.
        view.showStatus(step, field);
    }

    /**
     * Popula o campo com raposas e coelhos.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true);
                    animals.add(fox);
                    fox.setLocation(row, col);
                    field.place(fox, row, col);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true);
                    animals.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                }
                // else deixa o local vazio.
            }
        }
        Collections.shuffle(animals);
    }
}