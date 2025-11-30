import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * Um simulador simples de predador-presa, baseado em um campo
 * contendo coelhos e raposas.
 * * @author David J. Barnes and Michael Kolling (modificado)
 * 
 * @version 2025
 */
public class Simulator {
    // --- Constantes de Configuração ---
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_DEPTH = 50;
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    private static final double HUNTER_CREATION_PROBABILITY = 0.005;
    private static final double BUFFALO_CREATION_PROBABILITY = 0.01;
    private static final double LION_CREATION_PROBABILITY = 0.005;

    // --- Campos de Instância ---

    // A lista de animais no campo
    private List<Animal> animals;
    // A lista de animais recém-nascidos
    private List<Animal> newAnimals;

    //caçador
    private Actor hunter;

    // O estado atual do campo.
    private Field field;
    // Um segundo campo, usado para construir o próximo estágio.
    private Field updatedField;
    // O passo atual da simulação.
    private int step;
    // Uma visão gráfica da simulação.
    private SimulatorView view;

    // Sistema climático
    private WeatherSystem weatherSystem;

    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulator(int mapa) {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH, mapa);
    }

    /**
     * Cria um campo de simulação com o tamanho dado.
     * * @param depth Profundidade do campo.
     * * @param width Largura do campo.
     */
    public Simulator(int depth, int width, int mapa) {
        if (width <= 0 || depth <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        // Carregar mapa antes de criar os campos
            TerrainType[][] terrainMap;
        if(mapa == 1){
            terrainMap = MapLoader.loadMap("Src/Mapas/mapa1.txt", width, depth);
        }
        else{
            terrainMap = MapLoader.loadMap("Src/Mapas/mapa2.txt", width, depth);
        }
        


        animals = new ArrayList<Animal>();
        newAnimals = new ArrayList<Animal>();
        field = new Field(depth, width, terrainMap);
        updatedField = new Field(depth, width, terrainMap);

        // Inicializa o sistema climático
        weatherSystem = new WeatherSystem();

        // Cria a visão
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.RED);    // Raposa vermelha
        view.setColor(Rabbit.class, Color.PINK); // Coelho rosa
        view.setColor(Hunter.class, Color.BLUE); // Caçador azul
    

        // Configura um ponto de partida válido.
        reset();
    }

    /**
     * Executa a simulação por um longo período (500 passos).
     */
    public void runLongSimulation() {
        simulate(500,1);
    }

    /**
     * Executa a simulação pelo número de passos dado.
     * Para antes se a simulação não for mais viável.
     */
    public void simulate(int numSteps , int mapa) {
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

        // Avança o tempo no sistema climático
        weatherSystem.advanceTime();

        // Permite que todos os animais ajam.
        for (Iterator<Animal> iter = animals.iterator(); iter.hasNext();) {
            Animal animal = iter.next();
            if (animal.isAlive()) {
                // Nota: A Pessoa 1 deverá atualizar o método act para receber o weatherSystem
                // se necessário
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

        // Exibe o novo campo na tela, passando a estação atual
        view.showStatus(step, field, weatherSystem.getCurrentSeason());
    }

    /**
     * Reseta a simulação para a posição inicial.
     */
    public void reset() {
        step = 0;
        animals.clear();
        field.clear();
        updatedField.clear();

        // Reseta o clima
        weatherSystem = new WeatherSystem();

        populate(field);

        // Mostra o estado inicial na visão.
        view.showStatus(step, field, weatherSystem.getCurrentSeason());
    }

    /**
     * Popula o campo com raposas e coelhos.
     * Animais só são colocados em terrenos transitáveis.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();
        
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                TerrainType terrain = field.getTerrainAt(location);
                
                // Só coloca animais em terrenos transitáveis
                if (terrain.isTraversable()) {
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
                   /*  else if (rand.nextDouble() <= HUNTER_CREATION_PROBABILITY) {
                        Hunter hunter = new Hunter(location);
                        hunter.setLocation(location);
                        field.placehunter(hunter, row, col);
                       
                    }
                         */
                    else if (rand.nextDouble() <= BUFFALO_CREATION_PROBABILITY) {
                        Buffalo buffalo = new Buffalo(true);
                        animals.add(buffalo);
                        buffalo.setLocation(row, col);
                        field.place(buffalo, row, col);
                    }
                    else if (rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                        Lion lion = new Lion(true);
                        animals.add(lion);
                        lion.setLocation(row, col);
                        field.place(lion, row, col);
                }
                // else deixa o local vazio (para terrenos intransitáveis)
            }
        }
    }
        Collections.shuffle(animals); // Mistura a lista de animais para diversidade
    }

    /**
     * Retorna o sistema de clima.
     * 
     * @return O sistema climático atual.
     */
    public WeatherSystem getWeatherSystem() {
        return weatherSystem;
    }
}