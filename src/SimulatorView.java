import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Uma visão gráfica da grade de simulação.
 * A visão exibe um retângulo colorido para cada local
 * representando seu conteúdo. Ela usa uma cor de fundo padrão.
 * Cores para cada tipo de espécie podem ser definidas usando o
 * método setColor.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2025
 */
public class SimulatorView extends JFrame {
    // Cor usada para locais vazios.
    private static final Color EMPTY_COLOR = Color.white;

    // Cor usada para objetos que não têm cor definida.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Passo: ";
    private final String POPULATION_PREFIX = "População: ";
    private final String SEASON_PREFIX = "Estação: ";

    private JLabel stepLabel, population, seasonLabel;
    private FieldView fieldView;

    // Um mapa para armazenar cores para os participantes da simulação
    private HashMap<Class<?>, Color> colors;
    // Um objeto de estatísticas que calcula e armazena informações da simulação
    private FieldStats stats;

    /**
     * Cria uma visão da largura e altura fornecidas.
     * * @param height A altura da simulação.
     * * @param width A largura da simulação.
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap<>();

        setTitle("Simulação Raposas e Coelhos (com Clima)");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        seasonLabel = new JLabel(SEASON_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();

        // Painel superior para informações de passo e estação
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(stepLabel);
        topPanel.add(seasonLabel);

        contents.add(topPanel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Define uma cor a ser usada para uma determinada classe de animal.
     * * @param animalClass A classe do animal.
     * * @param color A cor a ser usada.
     */
    public void setColor(Class<?> animalClass, Color color) {
        colors.put(animalClass, color);
    }

    /**
     * @param animalClass A classe do animal.
     * @return A cor definida para a classe do animal.
     */
    private Color getColor(Class<?> animalClass) {
        Color col = colors.get(animalClass);
        if (col == null) {
            // Nenhuma cor definida para esta classe
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    /**
     * Mostra o estado atual do campo e do clima.
     * * @param step Qual passo (iteração) é.
     * * @param field O campo a ser exibido.
     * 
     * @param currentSeason A estação do ano atual.
     */
    public void showStatus(int step, Field field, Season currentSeason) {
        if (!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);
        seasonLabel.setText(SEASON_PREFIX + currentSeason.toString());

        stats.reset();
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getObjectAt(row, col);
                if (animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determina se a simulação deve continuar a ser executada.
     * * @param field O campo da simulação.
     * * @return true Se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Fornece uma visão gráfica de um campo retangular. Esta é
     * uma classe aninhada (uma classe definida dentro de outra) que
     * define um componente customizado para a interface do usuário.
     * Este componente exibe o campo.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Cria um novo componente FieldView.
         * * @param height A altura do campo.
         * 
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
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepara para uma nova rodada de pintura. Como o componente
         * pode ser redimensionado, calcula o fator de escala novamente.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) { // se o tamanho mudou...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
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
         * * @param x A coordenada x.
         * 
         * @param y     A coordenada y.
         * @param color A cor.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * O componente de visão do campo precisa ser reexibido. Copia a
         * imagem interna para a tela.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}