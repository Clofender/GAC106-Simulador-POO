import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * Um simulador de ecossistema, baseado em um campo
 * contendo múltiplas espécies de animais e caçadores.
 * 
 * @author David J. Barnes and Michael Kolling (base)
 * @author TP_Grupo08 (expansão completa)
 * @version 2025
 */
public class Simulator {
    
    // Constantes de Configuração
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_DEPTH = 50;
    private static final double FOX_CREATION_PROBABILITY = 0.04;
    private static final double RABBIT_CREATION_PROBABILITY = 0.12;
    private static final double BUFFALO_CREATION_PROBABILITY = 0.008;
    private static final double LION_CREATION_PROBABILITY = 0.010;

    // Campos de Instância
    private java.util.List<Animal> animals;
    private java.util.List<Animal> newAnimals;
    private java.util.List<Hunter> hunters;
    private Field field;
    private Field updatedField;
    private int step;
    private SimulatorView view;
    private WeatherSystem weatherSystem;
    private FieldStats stats;
    private int maxSteps;
    private boolean simulationRunning;

    /**
     * Constrói um campo de simulação com tamanho padrão.
     *
     * @param mapFileName Nome do arquivo de mapa a ser carregado.
     * @param hunterCount Número de caçadores a serem criados.
     * @param useGrassOnly Se true, usa apenas terreno de grama.
     */
    public Simulator(String mapFileName, int hunterCount, boolean useGrassOnly) {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH, mapFileName, hunterCount, useGrassOnly);
    }

    /**
     * Cria um campo de simulação com o tamanho dado.
     *
     * @param depth Profundidade do campo.
     * @param width Largura do campo.
     * @param mapFileName Nome do arquivo de mapa a ser carregado.
     * @param hunterCount Número de caçadores a serem criados.
     * @param useGrassOnly Se true, usa apenas terreno de grama.
     */
    public Simulator(int depth, int width, String mapFileName, int hunterCount, boolean useGrassOnly) {
        if (width <= 0 || depth <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        // Carregar mapa
        TerrainType[][] terrainMap;
        if (useGrassOnly || "GRASS_ONLY".equals(mapFileName)) {
            // Criar mapa só de grama
            terrainMap = createGrassOnlyMap(width, depth);
            System.out.println("Usando mapa somente de grama (" + width + "x" + depth + ")");
        } else {
            // Carregar mapa do arquivo
            String mapPath = "Mapas/" + mapFileName;
            terrainMap = MapLoader.loadMap(mapPath, width, depth);
            System.out.println("Mapa carregado: " + mapFileName + " (" + width + "x" + depth + ")");
        }

        animals = new ArrayList<Animal>();
        newAnimals = new ArrayList<Animal>();
        hunters = new ArrayList<Hunter>();
        field = new Field(depth, width, terrainMap);
        updatedField = new Field(depth, width, terrainMap);
        
        weatherSystem = new WeatherSystem();
        stats = new FieldStats();
        Animal.setWeatherSystem(weatherSystem);
        
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.RED);
        view.setColor(Rabbit.class, Color.PINK);
        view.setColor(Hunter.class, Color.BLUE);
        view.setColor(Buffalo.class, new Color(139, 69, 19));
        view.setColor(Lion.class, Color.YELLOW);

        maxSteps = 500;
        simulationRunning = true;
        
        reset(hunterCount);
    }

    /**
     * Cria um mapa composto apenas por grama.
     *
     * @param width Largura do mapa.
     * @param depth Profundidade do mapa.
     * @return Matriz com todos os terrenos como grama.
     */
    private TerrainType[][] createGrassOnlyMap(int width, int depth) {
        TerrainType[][] grassMap = new TerrainType[depth][width];
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                grassMap[row][col] = TerrainType.GRASS;
            }
        }
        return grassMap;
    }

    /**
     * Executa a simulação por um longo período (500 passos).
     */
    public void runLongSimulation() {
        simulate(maxSteps);
    }

    /**
     * Executa a simulação pelo número de passos dado.
     * Para antes se a simulação não for mais viável.
     *
     * @param numSteps Número de passos a simular.
     */
    public void simulate(int numSteps) {
        simulationRunning = true;
        for (int i = 0; i < numSteps && simulationRunning && view.isViable(field); i++) {
            simulateOneStep();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
        
        // Marca que a simulação terminou
        simulationRunning = false;
    }

    /**
     * Executa um único passo da simulação.
     */
    public void simulateOneStep() {
        if (!simulationRunning) return;
        
        step++;
        newAnimals.clear();
        
        weatherSystem.advanceTime();
        updateHunterActivity();
        
        for (Iterator<Hunter> iter = hunters.iterator(); iter.hasNext();) {
            Hunter hunter = iter.next();
            if (hunter.isAlive()) {
                java.util.List<Actor> newActors = new ArrayList<>();
                hunter.act(field, updatedField, newActors);
            } else {
                iter.remove();
            }
        }
        
        for (Iterator<Animal> iter = animals.iterator(); iter.hasNext();) {
            Animal animal = iter.next();
            if (animal.isAlive()) {
                animal.actWithAnimals(field, updatedField, newAnimals);
            } else {
                iter.remove();
            }
        }

        animals.addAll(newAnimals);

        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        final int currentStep = step;
        final Field currentField = field;
        final Season currentSeason = weatherSystem.getCurrentSeason();
        final FieldStats currentStats = stats;
        final java.util.List<Hunter> currentHunters = new ArrayList<>(hunters);
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view.showStatus(currentStep, currentField, currentSeason, currentStats, currentHunters);
            }
        });
    }

    /**
     * Atualiza a atividade dos caçadores baseado na estação atual.
     */
    private void updateHunterActivity() {
        boolean active = weatherSystem.getCurrentSeason() != Season.WINTER;
        for (Hunter hunter : hunters) {
            hunter.setActive(active);
        }
    }

    /**
     * Reseta a simulação para a posição inicial.
     *
     * @param hunterCount Número de caçadores a criar.
     */
    public void reset(int hunterCount) {
        step = 0;
        animals.clear();
        hunters.clear();
        field.clear();
        updatedField.clear();
        stats.reset();
        
        weatherSystem = new WeatherSystem();
        Animal.setWeatherSystem(weatherSystem);
        
        populate(field, hunterCount);
        
        view.showStatus(step, field, weatherSystem.getCurrentSeason(), stats, hunters);
    }

    /**
     * Popula o campo com animais e caçadores.
     */
    private void populate(Field field, int hunterCount) {
        Random rand = RandomGenerator.getRandom();
        field.clear();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                TerrainType terrain = field.getTerrainAt(location);

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
                    } else if (rand.nextDouble() <= BUFFALO_CREATION_PROBABILITY) {
                        Buffalo buffalo = new Buffalo(true);
                        animals.add(buffalo);
                        buffalo.setLocation(row, col);
                        field.place(buffalo, row, col);
                    } else if (rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                        Lion lion = new Lion(true);
                        animals.add(lion);
                        lion.setLocation(row, col);
                        field.place(lion, row, col);
                    }
                }
            }
        }

        for (int i = 0; i < hunterCount; i++) {
            Location home = findValidLocationForHunter(field);
            Hunter hunter = new Hunter(home, stats);
            hunters.add(hunter);
            field.placeHunter(hunter, home);
        }

        Collections.shuffle(animals);
        
        // Log da população inicial
        System.out.println("População inicial:");
        System.out.println("- Coelhos: " + countAnimals(Rabbit.class));
        System.out.println("- Raposas: " + countAnimals(Fox.class));
        System.out.println("- Búfalos: " + countAnimals(Buffalo.class));
        System.out.println("- Leões: " + countAnimals(Lion.class));
        System.out.println("- Caçadores: " + hunters.size());
    }

    /**
     * Conta o número de animais de uma classe específica.
     */
    private int countAnimals(Class<?> animalClass) {
        int count = 0;
        for (Animal animal : animals) {
            if (animal.getClass().equals(animalClass) && animal.isAlive()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Encontra uma localização válida para um caçador.
     */
    private Location findValidLocationForHunter(Field field) {
        Random rand = RandomGenerator.getRandom();
        int attempts = 0;
        while (attempts < 1000) {
            int row = rand.nextInt(field.getDepth());
            int col = rand.nextInt(field.getWidth());
            Location loc = new Location(row, col);
            if (field.getTerrainAt(loc) == TerrainType.GRASS && 
                field.getObjectAt(loc) == null) {
                return loc;
            }
            attempts++;
        }
        
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location loc = new Location(row, col);
                if (field.getTerrainAt(loc) == TerrainType.GRASS && 
                    field.getObjectAt(loc) == null) {
                    return loc;
                }
            }
        }
        
        return new Location(0, 0);
    }

    /**
     * Retorna o sistema de clima.
     *
     * @return O sistema climático atual.
     */
    public WeatherSystem getWeatherSystem() {
        return weatherSystem;
    }

    /**
     * Retorna as estatísticas da simulação.
     *
     * @return As estatísticas atuais.
     */
    public FieldStats getStats() {
        return stats;
    }

    /**
     * Retorna a view do simulador para poder fechá-la.
     *
     * @return A view do simulador.
     */
    public SimulatorView getView() {
        return view;
    }

    /**
     * Verifica se a simulação está rodando.
     *
     * @return true se a simulação está em execução.
     */
    public boolean isSimulationRunning() {
        return simulationRunning;
    }

    /**
     * Para a simulação.
     */
    public void stopSimulation() {
        simulationRunning = false;
    }

    /**
     * Retorna o número atual de passos da simulação.
     *
     * @return O número do passo atual.
     */
    public int getStep() {
        return step;
    }

    /**
     * Retorna o número máximo de passos da simulação.
     *
     * @return O número máximo de passos.
     */
    public int getMaxSteps() {
        return maxSteps;
    }

    /**
     * Define o número máximo de passos da simulação.
     *
     * @param maxSteps O número máximo de passos.
     */
    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    /**
     * Retorna o campo atual da simulação.
     *
     * @return O campo atual.
     */
    public Field getField() {
        return field;
    }

    /**
     * Retorna a lista de animais vivos.
     *
     * @return Lista de animais vivos.
     */
    public java.util.List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    /**
     * Retorna a lista de caçadores vivos.
     *
     * @return Lista de caçadores vivos.
     */
    public java.util.List<Hunter> getHunters() {
        return new ArrayList<>(hunters);
    }
}