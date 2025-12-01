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
    private static final int DEFAULT_WIDTH = 50;  // Largura padrão do campo em células
    private static final int DEFAULT_DEPTH = 50;  // Profundidade padrão do campo em células

    // Campos de Instância
    private java.util.List<Animal> animals;      // Lista principal de todos os animais vivos
    private java.util.List<Animal> newAnimals;   // Lista temporária para animais recém-nascidos
    private java.util.List<Hunter> hunters;      // Lista de todos os caçadores ativos
    private Field field;                         // Campo representando o estado atual da simulação
    private Field updatedField;                  // Campo para construir o próximo estado
    private int step;                            // Contador do passo atual da simulação
    private SimulatorView view;                  // Interface gráfica que mostra o estado
    private WeatherSystem weatherSystem;         // Sistema que controla estações e clima
    private FieldStats stats;                    // Coletor de estatísticas populacionais
    private int maxSteps;                        // Número máximo de passos antes de parar
    private boolean simulationRunning;           // Flag que indica se simulação está ativa

    /**
     * Constrói um campo de simulação com tamanho padrão.
     *
     * @param mapFileName Nome do arquivo de mapa a ser carregado.
     * @param hunterCount Número de caçadores a serem criados.
     * @param useGrassOnly Se true, usa apenas terreno de grama.
     */
    public Simulator(String mapFileName, int hunterCount, boolean useGrassOnly) {
        // Chama o construtor principal com dimensões padrão
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
        // Valida se as dimensões são positivas
        if (width <= 0 || depth <= 0) {
            System.out.println("As dimensões devem ser maiores que zero.");
            System.out.println("Usando valores padrão.");
            depth = DEFAULT_DEPTH;  // Usa valor padrão se inválido
            width = DEFAULT_WIDTH;  // Usa valor padrão se inválido
        }

        // Carregar mapa - decide entre mapa só de grama ou arquivo
        TerrainType[][] terrainMap;
        if (useGrassOnly || "GRASS_ONLY".equals(mapFileName)) {
            // Cria um mapa uniforme de apenas grama
            terrainMap = createGrassOnlyMap(width, depth);
            System.out.println("Usando mapa somente de grama (" + width + "x" + depth + ")");
        } else {
            // Carrega mapa personalizado do arquivo
            String mapPath = "Mapas/" + mapFileName;
            terrainMap = MapLoader.loadMap(mapPath, width, depth);
            System.out.println("Mapa carregado: " + mapFileName + " (" + width + "x" + depth + ")");
        }

        // Inicializar todas as listas e estruturas de dados
        animals = new ArrayList<Animal>();       // Lista para animais existentes
        newAnimals = new ArrayList<Animal>();    // Lista para novos nascimentos
        hunters = new ArrayList<Hunter>();       // Lista para caçadores
        field = new Field(depth, width, terrainMap);        // Campo atual
        updatedField = new Field(depth, width, terrainMap); // Campo próximo estado
        
        // Configurar sistemas auxiliares
        weatherSystem = new WeatherSystem();     // Sistema de estações do ano
        stats = new FieldStats();                // Coletor de estatísticas
        Animal.setWeatherSystem(weatherSystem);  // Compartilha clima com todos os animais
        
        // Configurar interface gráfica
        view = new SimulatorView(depth, width);  // Cria a janela de visualização
        view.setColor(Fox.class, Color.RED);              // Raposas em vermelho
        view.setColor(Rabbit.class, Color.PINK);          // Coelhos em rosa
        view.setColor(Hunter.class, Color.BLUE);          // Caçadores em azul
        view.setColor(Buffalo.class, new Color(139, 69, 19)); // Búfalos em marrom
        view.setColor(Lion.class, Color.YELLOW);          // Leões em amarelo

        // Configurar parâmetros de execução
        maxSteps = 500;              // Limite padrão de 500 passos
        simulationRunning = true;    // Inicia como executando
        
        reset(hunterCount);  // Popula o campo com animais e caçadores iniciais
    }

    /**
     * Cria um mapa composto apenas por grama.
     * Usado como fallback ou quando solicitado pelo usuário.
     *
     * @param width Largura do mapa.
     * @param depth Profundidade do mapa.
     * @return Matriz com todos os terrenos como grama.
     */
    private TerrainType[][] createGrassOnlyMap(int width, int depth) {
        TerrainType[][] grassMap = new TerrainType[depth][width];  // Cria matriz vazia
        // Preenche toda a matriz com terreno de grama
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                grassMap[row][col] = TerrainType.GRASS;  // Cada célula é grama
            }
        }
        return grassMap;  // Retorna mapa uniforme de grama
    }

    /**
     * Executa a simulação por um longo período (500 passos).
     * Método conveniente para simulações padrão.
     */
    public void runLongSimulation() {
        simulate(maxSteps);  // Executa até o limite máximo de passos
    }

    /**
     * Executa a simulação pelo número de passos dado.
     * Para antes se a simulação não for mais viável.
     *
     * @param numSteps Número de passos a simular.
     */
    public void simulate(int numSteps) {
        simulationRunning = true;  // Marca simulação como ativa
        // Executa cada passo até atingir o limite ou condições de parada
        for (int i = 0; i < numSteps && simulationRunning && view.isViable(field); i++) {
            simulateOneStep();  // Processa um único passo da simulação
            
            try {
                Thread.sleep(100);  // Pequena pausa para permitir visualização
            } catch (InterruptedException e) {
                break;  // Sai do loop se a thread for interrompida
            }
        }
        
        // Marca que a simulação terminou
        simulationRunning = false;
    }

    /**
     * Executa um único passo da simulação.
     * Processa todas as entidades e atualiza o estado do campo.
     */
    public void simulateOneStep() {
        if (!simulationRunning) return;  // Verifica se a simulação deve continuar
        
        step++;           // Incrementa o contador de passos
        newAnimals.clear();  // Limpa a lista de novos animais do passo anterior
        
        weatherSystem.advanceTime();  // Avança o tempo no sistema climático
        updateHunterActivity();       // Atualiza atividade dos caçadores por estação
        
        // Processa todos os caçadores - eles agem primeiro
        for (Iterator<Hunter> iter = hunters.iterator(); iter.hasNext();) {
            Hunter hunter = iter.next();
            if (hunter.isAlive()) {
                java.util.List<Actor> newActors = new ArrayList<>();
                hunter.act(field, updatedField, newActors);  // Executa ação do caçador
            } else {
                iter.remove();  // Remove caçadores que morreram
            }
        }
        
        // Processa todos os animais
        for (Iterator<Animal> iter = animals.iterator(); iter.hasNext();) {
            Animal animal = iter.next();
            if (animal.isAlive()) {
                // Executa ação do animal e coleta novos nascimentos
                animal.actWithAnimals(field, updatedField, newAnimals);
            } else {
                iter.remove();  // Remove animais que morreram
            }
        }

        animals.addAll(newAnimals);  // Adiciona os novos animais à população

        // Troca os campos: o campo atualizado se torna o novo campo atual
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();  // Limpa o campo antigo para reutilização

        // Prepara dados finais para atualização da interface
        final int currentStep = step;
        final Field currentField = field;
        final Season currentSeason = weatherSystem.getCurrentSeason();
        final FieldStats currentStats = stats;
        final java.util.List<Hunter> currentHunters = new ArrayList<>(hunters);
        
        // Atualiza a interface gráfica na thread do Swing (EDT)
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
        boolean active = weatherSystem.getCurrentSeason() != Season.WINTER;  // Ativos em todas as estações exceto inverno
        for (Hunter hunter : hunters) {
            hunter.setActive(active);  // Define atividade de cada caçador
        }
    }

    /**
     * Reseta a simulação para a posição inicial.
     * Limpa todos os estados e recria a população inicial.
     *
     * @param hunterCount Número de caçadores a criar.
     */
    public void reset(int hunterCount) {
        step = 0;           // Reinicia contador de passos
        animals.clear();    // Remove todos os animais
        hunters.clear();    // Remove todos os caçadores
        field.clear();      // Limpa o campo atual
        updatedField.clear();  // Limpa o campo de atualização
        stats.reset();      // Reinicia estatísticas
        
        weatherSystem = new WeatherSystem();     // Recria sistema climático
        Animal.setWeatherSystem(weatherSystem);  // Reconfigura clima para animais
        
        populate(field, hunterCount);  // Recria população inicial
        
        // Mostra estado inicial na interface
        view.showStatus(step, field, weatherSystem.getCurrentSeason(), stats, hunters);
    }

    /**
     * Popula o campo com animais e caçadores.
     * Distribui entidades aleatoriamente pelo campo.
     *
     * @param field O campo a ser populado.
     * @param hunterCount Número de caçadores a criar.
     */
    private void populate(Field field, int hunterCount) {
        Random rand = RandomGenerator.getRandom();  // Gerador de números aleatórios
        field.clear();  // Garante que o campo está vazio

        // Percorre todas as posições do campo
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                TerrainType terrain = field.getTerrainAt(location);

                // Só coloca animais em terrenos transitáveis
                if (terrain.isTraversable()) {
                    // Tenta criar cada tipo de animal baseado em probabilidade
                    if (rand.nextDouble() <= Fox.getCreationProbability()) {
                        Fox fox = new Fox(true);  // Cria raposa com idade aleatória
                        animals.add(fox);
                        fox.setLocation(row, col);
                        field.place(fox, row, col);
                    } else if (rand.nextDouble() <= Rabbit.getCreationProbability()) {
                        Rabbit rabbit = new Rabbit(true);  // Cria coelho com idade aleatória
                        animals.add(rabbit);
                        rabbit.setLocation(row, col);
                        field.place(rabbit, row, col);
                    } else if (rand.nextDouble() <= Buffalo.getCreationProbability()) {
                        Buffalo buffalo = new Buffalo(true);  // Cria búfalo com idade aleatória
                        animals.add(buffalo);
                        buffalo.setLocation(row, col);
                        field.place(buffalo, row, col);
                    } else if (rand.nextDouble() <= Lion.getCreationProbability()) {
                        Lion lion = new Lion(true);  // Cria leão com idade aleatória
                        animals.add(lion);
                        lion.setLocation(row, col);
                        field.place(lion, row, col);
                    }
                }
            }
        }

        // Adiciona caçadores em posições válidas
        for (int i = 0; i < hunterCount; i++) {
            Location home = findValidLocationForHunter(field);  // Encontra localização válida
            Hunter hunter = new Hunter(home, stats);  // Cria caçador com casa
            hunters.add(hunter);
            field.placeHunter(hunter, home);  // Posiciona no campo
        }

        Collections.shuffle(animals);  // Embaralha animais para diversidade inicial
        
        // Log da população inicial para debug
        System.out.println("População inicial:");
        System.out.println("- Coelhos: " + countAnimals(Rabbit.class));
        System.out.println("- Raposas: " + countAnimals(Fox.class));
        System.out.println("- Búfalos: " + countAnimals(Buffalo.class));
        System.out.println("- Leões: " + countAnimals(Lion.class));
        System.out.println("- Caçadores: " + hunters.size());
    }

    /**
     * Conta o número de animais de uma classe específica.
     *
     * @param animalClass A classe do animal a contar.
     * @return O número de animais vivos da classe especificada.
     */
    private int countAnimals(Class<?> animalClass) {
        int count = 0;
        for (Animal animal : animals) {
            if (animal.getClass().equals(animalClass) && animal.isAlive()) {
                count++;  // Conta apenas animais vivos da classe especificada
            }
        }
        return count;
    }

    /**
     * Encontra uma localização válida para um caçador.
     * Caçadores só podem ser colocados em grama desocupada.
     *
     * @param field O campo onde procurar.
     * @return Uma localização válida para o caçador.
     */
    private Location findValidLocationForHunter(Field field) {
        Random rand = RandomGenerator.getRandom();
        int attempts = 0;
        // Tenta encontrar localização aleatória válida
        while (attempts < 1000) {
            int row = rand.nextInt(field.getDepth());
            int col = rand.nextInt(field.getWidth());
            Location loc = new Location(row, col);
            // Verifica se é grama e está vazia
            if (field.getTerrainAt(loc) == TerrainType.GRASS && 
                field.getObjectAt(loc) == null) {
                return loc;  // Retorna localização válida
            }
            attempts++;
        }
        
        // Fallback: procura sequencialmente por grama vazia
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location loc = new Location(row, col);
                if (field.getTerrainAt(loc) == TerrainType.GRASS && 
                    field.getObjectAt(loc) == null) {
                    return loc;  // Retorna primeira grama vazia encontrada
                }
            }
        }
        
        return new Location(0, 0);  // Último recurso: retorna origem
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
        return new ArrayList<>(animals);  // Retorna cópia para evitar modificações externas
    }

    /**
     * Retorna a lista de caçadores vivos.
     *
     * @return Lista de caçadores vivos.
     */
    public java.util.List<Hunter> getHunters() {
        return new ArrayList<>(hunters);  // Retorna cópia para evitar modificações externas
    }
}