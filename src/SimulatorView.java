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
    private static final Color UNKNOWN_COLOR = Color.gray;  // Cor padrão para tipos desconhecidos
    
    // Prefixos para labels de informação
    private final String STEP_PREFIX = "Passo: ";          // Prefixo do contador de passos
    private final String POPULATION_PREFIX = "População: "; // Prefixo da população
    private final String SEASON_PREFIX = "Estação: ";      // Prefixo da estação atual
    private final String HUNTER_PREFIX = "Caçador: ";      // Prefixo das estatísticas de caçador
    
    // Componentes da interface gráfica
    private final JLabel stepLabel, population, seasonLabel, hunterLabel;
    private final FieldView fieldView;  // Componente que desenha o campo
    
    // Mapa para armazenar cores para os participantes da simulação (classe -> cor)
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
        stats = new FieldStats();  // Inicializa estatísticas
        colors = new HashMap<>();  // Inicializa mapa de cores
        setTitle("Simulação Ecossistema - TP_Grupo08");  // Título da janela
        
        // Cria labels para informações da simulação
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        seasonLabel = new JLabel(SEASON_PREFIX, JLabel.CENTER);
        hunterLabel = new JLabel(HUNTER_PREFIX, JLabel.CENTER);
        
        setLocation(100, 50);  // Posição inicial da janela na tela
        fieldView = new FieldView(height, width);  // Cria a visualização do campo
        
        Container contents = getContentPane();  // Obtém o container principal
        
        // Painel superior com informações
        JPanel topPanel = new JPanel(new GridLayout(3, 1));  // 3 linhas, 1 coluna
        topPanel.add(stepLabel);     // Adiciona label de passo
        topPanel.add(seasonLabel);   // Adiciona label de estação
        topPanel.add(hunterLabel);   // Adiciona label de caçador
        
        // Organiza os componentes na janela
        contents.add(topPanel, BorderLayout.NORTH);      // Painel superior
        contents.add(fieldView, BorderLayout.CENTER);    // Campo no centro
        contents.add(population, BorderLayout.SOUTH);    // População embaixo
        
        pack();  // Ajusta tamanho da janela ao conteúdo
        setVisible(true);  // Torna a janela visível
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Fecha aplicação ao fechar janela
    }

    /**
     * Define uma cor a ser usada para uma determinada classe de animal.
     *
     * @param animalClass A classe do animal.
     * @param color A cor a ser usada.
     */
    public void setColor(Class<?> animalClass, Color color) {
        colors.put(animalClass, color);  // Armazena a cor no mapa
    }

    /**
     * Obtém a cor definida para uma classe de animal.
     *
     * @param animalClass A classe do animal.
     * @return A cor definida para a classe do animal.
     */
    private Color getColor(Class<?> animalClass) {
        Color col = colors.get(animalClass);  // Busca cor no mapa
        if (col == null) {
            return UNKNOWN_COLOR;  // Retorna cor padrão se não encontrada
        } else {
            return col;  // Retorna cor específica da classe
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
        // Garante que a janela está visível
        if (!isVisible()) {
            setVisible(true);
        }
        
        // Atualiza labels com informações atuais
        stepLabel.setText(STEP_PREFIX + step);  // Atualiza contador de passos
        seasonLabel.setText(SEASON_PREFIX + currentSeason.toString());  // Atualiza estação
        hunterLabel.setText(HUNTER_PREFIX + stats.getHunterKills() + " caças");  // Atualiza caçadas
        
        // Reseta estatísticas para novo cálculo
        this.stats.reset();
        stats.reset();
        fieldView.preparePaint();  // Prepara o campo para desenho
        
        // Desenha o terreno como fundo
        drawTerrainGrid(field);
        
        // Desenha as casas dos caçadores (se houver)
        if (hunters != null) {
            for (Hunter hunter : hunters) {
                if (hunter.isAlive()) {
                    Location home = hunter.getHomeLocation();  // Obtém localização da casa
                    fieldView.drawMark(home.getCol(), home.getRow(), Color.ORANGE);  // Desenha casa em laranja
                }
            }
        }
        
        // Desenha todos os atores vivos no campo
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = field.getObjectAt(row, col);  // Obtém ator na posição
                if (actor != null && actor.isAlive()) {
                    if (actor instanceof Animal) {
                        // Processa animais
                        Animal animal = (Animal) actor;
                        this.stats.incrementCount(animal.getClass());  // Conta nas estatísticas internas
                        stats.incrementCount(animal.getClass());       // Conta nas estatísticas externas
                        fieldView.drawMark(col, row, getColor(animal.getClass()));  // Desenha animal
                    } else if (actor instanceof Hunter) {
                        // Processa caçadores
                        fieldView.drawMark(col, row, getColor(Hunter.class));  // Desenha caçador
                    }
                }
            }
        }
        
        // Finaliza contagem de estatísticas
        this.stats.countFinished();
        stats.countFinished();
        // Atualiza label de população com detalhes
        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();  // Redesenha o campo
    }

    /**
     * Determina se a simulação deve continuar a ser executada.
     *
     * @param field O campo da simulação.
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);  // Delega para FieldStats verificar viabilidade
    }

    /**
     * Fornece uma visão gráfica de um campo retangular.
     * Classe interna responsável pelo desenho do campo.
     */
    private class FieldView extends JPanel {
        
        private final int GRID_VIEW_SCALING_FACTOR = 6;  // Fator de escala para células
        private final int gridWidth, gridHeight;         // Dimensões em células
        private int xScale, yScale;                      // Escalas atuais de desenho
        Dimension size;                                  // Tamanho atual do componente
        private Graphics g;                              // Contexto gráfico para desenho
        private Image fieldImage;                        // Imagem de fundo do campo

        /**
         * Cria um novo componente FieldView.
         *
         * @param height A altura do campo.
         * @param width A largura do campo.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);  // Inicializa tamanho
        }

        /**
         * Informa ao gerenciador de GUI o quão grande gostaríamos de ser.
         */
        @Override
        public Dimension getPreferredSize() {
            // Calcula tamanho preferido baseado no fator de escala
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepara para uma nova rodada de pintura.
         * Cria imagem de fundo se necessário e calcula escalas.
         */
        public void preparePaint() {
            // Verifica se o tamanho mudou (redimensionamento da janela)
            if (!size.equals(getSize())) {
                size = getSize();  // Atualiza tamanho atual
                fieldImage = createImage(size.width, size.height);  // Cria nova imagem
                g = fieldImage.getGraphics();  // Obtém contexto gráfico
                // Calcula escala horizontal
                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;  // Usa escala mínima
                }
                // Calcula escala vertical
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;  // Usa escala mínima
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
            g.setColor(color);  // Define cor
            // Desenha retângulo na posição calculada (com margem de 1 pixel)
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * O componente de visão do campo precisa ser reexibido.
         * Chamado automaticamente pelo Swing.
         */
        @Override
        public void paintComponent(Graphics g) {
            // Desenha a imagem de fundo se existir
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);  // Desenha imagem completa
            }
        }
    }

    /**
     * Desenha a grade de terreno como fundo da simulação.
     *
     * @param field O campo contendo as informações do terreno.
     */
    private void drawTerrainGrid(Field field) {
        // Percorre todas as posições do campo
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                TerrainType terrain = field.getTerrainAt(row, col);  // Obtém tipo de terreno
                fieldView.drawMark(col, row, terrain.getColor());    // Desenha terreno com sua cor
            }
        }
    }
}