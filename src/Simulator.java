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
    private static final double BUFFALO_CREATION_PROBABILITY = 0.010;
    private static final double LION_CREATION_PROBABILITY = 0.003;

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

    /**
     * Constrói um campo de simulação com tamanho padrão.
     *
     * @param mapa Número do mapa a ser carregado.
     * @param hunterCount Número de caçadores a serem criados.
     */
    public Simulator(int mapa, int hunterCount) {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH, mapa, hunterCount);
    }

    /**
     * Cria um campo de simulação com o tamanho dado.
     *
     * @param depth Profundidade do campo.
     * @param width Largura do campo.
     * @param mapa Número do mapa a ser carregado.
     * @param hunterCount Número de caçadores a serem criados.
     */
    public Simulator(int depth, int width, int mapa, int hunterCount) {
        if (width <= 0 || depth <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        // Carregar mapa antes de criar os campos
        TerrainType[][] terrainMap;
        if (mapa == 1) {
            terrainMap = MapLoader.loadMap("Mapas/mapa1.txt", width, depth);
        } else {
            terrainMap = MapLoader.loadMap("Mapas/mapa2.txt", width, depth);
        }

        animals = new ArrayList<Animal>();
        newAnimals = new ArrayList<Animal>();
        hunters = new ArrayList<Hunter>(); // Lista de caçadores INICIALMENTE VAZIA
        field = new Field(depth, width, terrainMap);
        updatedField = new Field(depth, width, terrainMap);
        
        // Inicializa o sistema climático
        weatherSystem = new WeatherSystem();
        
        // Inicializa estatísticas
        stats = new FieldStats();
        
        // Configura o sistema de clima para os animais
        Animal.setWeatherSystem(weatherSystem);
        
        // Cria a visão
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.RED);        // Raposa vermelha
        view.setColor(Rabbit.class, Color.PINK);    // Coelho rosa
        view.setColor(Hunter.class, Color.BLUE);    // Caçador azul
        view.setColor(Buffalo.class, new Color(139, 69, 19)); // Búfalo marrom
        view.setColor(Lion.class, Color.YELLOW);    // Leão amarelo

        // Configura um ponto de partida válido.
        reset(hunterCount);
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
     *
     * @param numSteps Número de passos a simular.
     */
    public void simulate(int numSteps) {
        for (int i = 0; i < numSteps && view.isViable(field); i++) {
            simulateOneStep();
            
            // Adicionar delay para permitir atualização da interface
            try {
                Thread.sleep(100); // 100ms de delay entre steps
            } catch (InterruptedException e) {
                // Se interrompido, parar a simulação
                break;
            }
        }
    }

    /**
     * Executa um único passo da simulação.
     * Itera por todo o campo atualizando o estado de cada animal e caçador.
     */
    public void simulateOneStep() {
        step++;
        newAnimals.clear();
        
        // Avança o tempo no sistema climático
        weatherSystem.advanceTime();
        
        // Atualizar atividade do caçador baseado na estação
        updateHunterActivity();
        
        // IMPORTANTE: Caçadores agem ANTES dos animais se moverem
        // Isso permite que os caçadores encontrem os animais no campo atual
        for (Iterator<Hunter> iter = hunters.iterator(); iter.hasNext();) {
            Hunter hunter = iter.next();
            if (hunter.isAlive()) {
                java.util.List<Actor> newActors = new ArrayList<>();
                hunter.act(field, updatedField, newActors);
            } else {
                iter.remove(); // Remove caçadores mortos
            }
        }
        
        // Permite que todos os animais ajam (depois dos caçadores).
        for (Iterator<Animal> iter = animals.iterator(); iter.hasNext();) {
            Animal animal = iter.next();
            if (animal.isAlive()) {
                // Usar o método de compatibilidade com List<Animal> (nome alterado)
                animal.actWithAnimals(field, updatedField, newAnimals);
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
        // Usar invokeLater para atualizar na thread do Swing
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
     * Caçadores não caçam durante o inverno.
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
        hunters.clear(); //Limpa a lista de caçadores
        field.clear();
        updatedField.clear();
        stats.reset();
        
        // Reseta o clima
        weatherSystem = new WeatherSystem();
        Animal.setWeatherSystem(weatherSystem);
        
        populate(field, hunterCount); // Popula animais E caçadores
        
        // Mostra o estado inicial na visão.
        view.showStatus(step, field, weatherSystem.getCurrentSeason(), stats, hunters);
    }

    /**
     * Popula o campo com animais e caçadores.
     * Animais só são colocados em terrenos transitáveis.
     *
     * @param field O campo a ser populado.
     * @param hunterCount Número de caçadores a criar.
     */
    private void populate(Field field, int hunterCount) {
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

        // Adicionar caçadores
        for (int i = 0; i < hunterCount; i++) {
            Location home = findValidLocationForHunter(field);
            Hunter hunter = new Hunter(home, stats);
            hunters.add(hunter); // ADICIONA À LISTA hunters
            field.placeHunter(hunter, home);
        }

        Collections.shuffle(animals); // Mistura a lista de animais para diversidade
    }

    /**
     * Encontra uma localização válida para um caçador.
     * Caçadores só podem ser colocados em grama.
     *
     * @param field O campo onde procurar.
     * @return Uma localização válida para o caçador.
     */
    private Location findValidLocationForHunter(Field field) {
        Random rand = new Random();
        int attempts = 0;
        while (attempts < 1000) { // Limite de tentativas para evitar loop infinito
            int row = rand.nextInt(field.getDepth());
            int col = rand.nextInt(field.getWidth());
            Location loc = new Location(row, col);
            if (field.getTerrainAt(loc) == TerrainType.GRASS && 
                field.getObjectAt(loc) == null) {
                return loc;
            }
            attempts++;
        }
        // Fallback: retorna a primeira localização de grama encontrada
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location loc = new Location(row, col);
                if (field.getTerrainAt(loc) == TerrainType.GRASS && 
                    field.getObjectAt(loc) == null) {
                    return loc;
                }
            }
        }
        // Último recurso: retorna (0,0)
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
}