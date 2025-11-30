import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Uma visão gráfica da grade de simulação.
 * A visão exibe um retângulo colorido para cada local
 * representando seu conteúdo. Ela usa cores diferentes para
 * terreno e atores.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author TP_Grupo08 (expansão para múltiplos atores e estatísticas)
 * @version 2025
 */
public class SimulatorView extends JFrame {
    
    // Cores para elementos da interface
    private static final Color UNKNOWN_COLOR = Color.gray;
    
    // Prefixos para labels
    private final String STEP_PREFIX = "Passo: ";
    private final String POPULATION_PREFIX = "População: ";
    private final String SEASON_PREFIX = "Estação: ";
    private final String HUNTER_PREFIX = "Caçador: ";
    
    // Componentes da interface
    private final JLabel stepLabel, population, seasonLabel, hunterLabel;
    private final FieldView fieldView;
    
    // Mapa para armazenar cores para os participantes da simulação
    private final HashMap<Class<?>, Color> colors;
    
    // Um objeto de estatísticas que calcula e armazena informações da simulação
    private final FieldStats stats;

    /**
     * Cria uma visão da largura e altura fornecidas.
     *
     * @param height A altura da simulação.
     * @param width A largura da simulação.
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap<>();
        setTitle("Simulação Ecossistema - TP_Grupo08");
        
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        seasonLabel = new JLabel(SEASON_PREFIX, JLabel.CENTER);
        hunterLabel = new JLabel(HUNTER_PREFIX, JLabel.CENTER);
        
        setLocation(100, 50);
        fieldView = new FieldView(height, width);
        
        Container contents = getContentPane();
        
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(stepLabel);
        topPanel.add(seasonLabel);
        topPanel.add(hunterLabel);
        
        contents.add(topPanel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Define uma cor a ser usada para uma determinada classe de animal.
     *
     * @param animalClass A classe do animal.
     * @param color A cor a ser usada.
     */
    public void setColor(Class<?> animalClass, Color color) {
        colors.put(animalClass, color);
    }

    /**
     * Obtém a cor definida para uma classe de animal.
     *
     * @param animalClass A classe do animal.
     * @return A cor definida para a classe do animal.
     */
    private Color getColor(Class<?> animalClass) {
        Color col = colors.get(animalClass);
        if (col == null) {
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    /**
     * Mostra o estado atual do campo e do clima.
     *
     * @param step Qual passo (iteração) é.
     * @param field O campo a ser exibido.
     * @param currentSeason A estação do ano atual.
     * @param stats As estatísticas atuais da simulação.
     * @param hunters Lista de caçadores para desenhar suas casas.
     */
    public void showStatus(int step, Field field, Season currentSeason, FieldStats stats, java.util.List<Hunter> hunters) {
        if (!isVisible()) {
            setVisible(true);
        }
        
        stepLabel.setText(STEP_PREFIX + step);
        seasonLabel.setText(SEASON_PREFIX + currentSeason.toString());
        hunterLabel.setText(HUNTER_PREFIX + stats.getHunterKills() + " caças");
        
        this.stats.reset();
        stats.reset();
        fieldView.preparePaint();
        
        drawTerrainGrid(field);
        
        if (hunters != null) {
            for (Hunter hunter : hunters) {
                if (hunter.isAlive()) {
                    Location home = hunter.getHomeLocation();
                    fieldView.drawMark(home.getCol(), home.getRow(), Color.ORANGE);
                }
            }
        }
        
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = field.getObjectAt(row, col);
                if (actor != null && actor.isAlive()) {
                    if (actor instanceof Animal) {
                        Animal animal = (Animal) actor;
                        this.stats.incrementCount(animal.getClass());
                        stats.incrementCount(animal.getClass());
                        fieldView.drawMark(col, row, getColor(animal.getClass()));
                    } else if (actor instanceof Hunter) {
                        fieldView.drawMark(col, row, getColor(Hunter.class));
                    }
                }
            }
        }
        
        this.stats.countFinished();
        stats.countFinished();
        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determina se a simulação deve continuar a ser executada.
     *
     * @param field O campo da simulação.
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Fornece uma visão gráfica de um campo retangular.
     */
    private class FieldView extends JPanel {
        
        private final int GRID_VIEW_SCALING_FACTOR = 6;
        private final int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Cria um novo componente FieldView.
         *
         * @param height A altura do campo.
         * @param width A largura do campo.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Informa ao gerenciador de GUI o quão grande gostaríamos de ser.
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepara para uma nova rodada de pintura.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) {
                size = getSize();
                fieldImage = createImage(size.width, size.height);
                g = fieldImage.getGraphics();
                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Pinta a localização da grade neste campo em uma determinada cor.
         *
         * @param x A coordenada x.
         * @param y A coordenada y.
         * @param color A cor.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * O componente de visão do campo precisa ser reexibido.
         */
        @Override
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }

    /**
     * Desenha a grade de terreno como fundo da simulação.
     *
     * @param field O campo contendo as informações do terreno.
     */
    private void drawTerrainGrid(Field field) {
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                TerrainType terrain = field.getTerrainAt(row, col);
                fieldView.drawMark(col, row, terrain.getColor());
            }
        }
    }
}