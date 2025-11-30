import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela inicial do simulador com seleção de configurações.
 * Permite escolher o mapa e o número de caçadores antes de iniciar a simulação.
 * 
 * @author TP_Grupo08
 * @version 2025
 */
public class MainMenu extends JFrame {
    
    private JComboBox<String> mapSelector;
    private JSpinner hunterSpinner;
    private JButton startButton;
    private JPanel mapPreviewPanel;
    private JTextArea descriptionArea;
    private List<String> mapFiles;
    private JCheckBox grassOnlyCheckbox;

    /**
     * Cria a tela inicial do simulador.
     */
    public MainMenu() {
        setTitle("Simulador de Ecossistema - Menu Inicial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocation(200, 100);
        
        // Carregar mapas disponíveis
        loadAvailableMaps();
        
        initializeComponents();
        setupLayout();
        setupListeners();
        
        // Atualizar preview inicial
        updateMapPreview();
        
        pack();
        setVisible(true);
    }

    /**
     * Carrega os mapas disponíveis na pasta Mapas.
     */
    private void loadAvailableMaps() {
        mapFiles = new ArrayList<>();
        File mapDir = new File("Mapas");
        if (mapDir.exists() && mapDir.isDirectory()) {
            File[] files = mapDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    mapFiles.add(file.getName());
                }
            }
        }
        
        // Se não encontrou mapas, adiciona padrões
        if (mapFiles.isEmpty()) {
            mapFiles.add("mapa1.txt");
            mapFiles.add("mapa2.txt");
        }
    }

    /**
     * Inicializa os componentes da interface.
     */
    private void initializeComponents() {
        // Seletor de mapa - agora dinâmico
        String[] mapNames = new String[mapFiles.size() + 1];
        mapNames[0] = "Grama (Mapa Padrão)";
        for (int i = 0; i < mapFiles.size(); i++) {
            String fileName = mapFiles.get(i);
            String displayName = fileName.replace(".txt", "").replace("_", " ");
            mapNames[i + 1] = displayName;
        }
        mapSelector = new JComboBox<>(mapNames);
        mapSelector.setPreferredSize(new Dimension(200, 25));
        
        // Checkbox para mapa só de grama
        grassOnlyCheckbox = new JCheckBox("Usar apenas grama (ignorar mapa selecionado)");
        grassOnlyCheckbox.setSelected(false);
        
        // Seletor de número de caçadores
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 0, 5, 1);
        hunterSpinner = new JSpinner(spinnerModel);
        hunterSpinner.setPreferredSize(new Dimension(60, 25));
        
        // Botão de iniciar
        startButton = new JButton("Iniciar Simulação");
        startButton.setBackground(new Color(34, 139, 34)); // Verde floresta
        startButton.setForeground(Color.WHITE);
        startButton.setPreferredSize(new Dimension(150, 30));
        
        // Painel de preview do mapa
        mapPreviewPanel = new JPanel(new BorderLayout());
        mapPreviewPanel.setPreferredSize(new Dimension(300, 300));
        mapPreviewPanel.setBackground(Color.LIGHT_GRAY);
        mapPreviewPanel.setBorder(BorderFactory.createTitledBorder("Visualização do Mapa"));
        
        // Área de descrição
        descriptionArea = new JTextArea();
        descriptionArea.setText("Bem-vindo ao Simulador de Ecossistema!\n\n" +
                "Este simulador modela as interações entre múltiplas espécies:\n" +
                "• Coelhos (herbívoros pequenos)\n" +
                "• Búfalos (herbívoros grandes)\n" +
                "• Raposas (carnívoros)\n" +
                "• Leões (superpredadores)\n" +
                "• Caçadores (predadores universais)\n\n" +
                "Configurações:\n" +
                "- Mapa: Define o terreno da simulação\n" +
                "- Apenas Grama: Ignora o mapa e usa apenas terreno de grama\n" +
                "- Caçadores: Controla o número de caçadores ativos");
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(getBackground());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    /**
     * Configura o layout dos componentes.
     */
    private void setupLayout() {
        // Painel de configurações
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder("Configurações da Simulação"));
        configPanel.setPreferredSize(new Dimension(450, 400));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Linha 1: Seletor de mapa
        gbc.gridx = 0; gbc.gridy = 0;
        configPanel.add(new JLabel("Mapa:"), gbc);
        
        gbc.gridx = 1;
        configPanel.add(mapSelector, gbc);
        
        // Linha 2: Checkbox apenas grama
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        configPanel.add(grassOnlyCheckbox, gbc);
        
        // Linha 3: Número de caçadores
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        configPanel.add(new JLabel("Número de Caçadores:"), gbc);
        
        gbc.gridx = 1;
        configPanel.add(hunterSpinner, gbc);
        
        // Linha 4: Preview do mapa
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        configPanel.add(mapPreviewPanel, gbc);
        
        // Linha 5: Botão iniciar
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        configPanel.add(startButton, gbc);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        mainPanel.add(configPanel, BorderLayout.CENTER);
        
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("Sobre o Simulador"));
        descriptionScroll.setPreferredSize(new Dimension(450, 120));
        mainPanel.add(descriptionScroll, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    /**
     * Configura os ouvintes de eventos.
     */
    private void setupListeners() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
        
        mapSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMapPreview();
            }
        });
        
        grassOnlyCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMapPreview();
            }
        });
    }

    /**
     * Atualiza o preview do mapa baseado na seleção.
     */
    private void updateMapPreview() {
        mapPreviewPanel.removeAll();
        
        if (grassOnlyCheckbox.isSelected()) {
            // Mostrar preview de mapa só de grama
            drawGrassOnlyPreview();
        } else {
            int selectedIndex = mapSelector.getSelectedIndex();
            if (selectedIndex == 0) {
                // Mapa padrão de grama
                drawGrassOnlyPreview();
            } else if (selectedIndex > 0 && selectedIndex - 1 < mapFiles.size()) {
                String selectedMap = mapFiles.get(selectedIndex - 1);
                String mapPath = "Mapas/" + selectedMap;
                
                // Carrega o mapa para gerar preview
                try {
                    TerrainType[][] terrainMap = MapLoader.loadMap(mapPath, 50, 50);
                    drawFullMapPreview(terrainMap, selectedMap);
                } catch (Exception e) {
                    // Fallback: mostra mensagem de erro
                    showErrorPreview("Erro ao carregar mapa: " + selectedMap);
                }
            }
        }
        
        mapPreviewPanel.revalidate();
        mapPreviewPanel.repaint();
    }

    /**
     * Desenha o preview de mapa só de grama.
     */
    private void drawGrassOnlyPreview() {
        JPanel gridPanel = new JPanel(new GridLayout(10, 10, 1, 1));
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JPanel cell = new JPanel();
                cell.setBackground(TerrainType.GRASS.getColor());
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                gridPanel.add(cell);
            }
        }
        
        // Adicionar label informativa
        JLabel infoLabel = new JLabel("Mapa de Grama (50x50) - Todos os terrenos são grama");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setForeground(Color.DARK_GRAY);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(gridPanel, BorderLayout.CENTER);
        container.add(infoLabel, BorderLayout.SOUTH);
        
        mapPreviewPanel.add(container, BorderLayout.CENTER);
    }

    /**
     * Desenha o preview completo do mapa.
     */
    private void drawFullMapPreview(TerrainType[][] terrainMap, String mapName) {
        int rows = terrainMap.length;
        int cols = terrainMap[0].length;
        
        // Calcular escala para caber no preview (máximo 20x20)
        int scale = Math.min(20, Math.min(300 / cols, 300 / rows));
        int displayRows = Math.min(rows, 20);
        int displayCols = Math.min(cols, 20);
        
        JPanel gridPanel = new JPanel(new GridLayout(displayRows, displayCols, 1, 1));
        gridPanel.setPreferredSize(new Dimension(displayCols * scale, displayRows * scale));
        
        // Calcular passo para amostragem se o mapa for muito grande
        int rowStep = rows / displayRows;
        int colStep = cols / displayCols;
        
        for (int i = 0; i < displayRows; i++) {
            for (int j = 0; j < displayCols; j++) {
                int actualRow = Math.min(i * rowStep, rows - 1);
                int actualCol = Math.min(j * colStep, cols - 1);
                
                TerrainType terrain = terrainMap[actualRow][actualCol];
                JPanel cell = new JPanel();
                cell.setBackground(terrain.getColor());
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                
                // Tooltip com informações do terreno
                String tooltip = String.format("Posição: (%d,%d) - %s", 
                    actualRow, actualCol, getTerrainName(terrain));
                cell.setToolTipText(tooltip);
                
                gridPanel.add(cell);
            }
        }
        
        // Informações do mapa
        JLabel infoLabel = new JLabel(
            String.format("<html><div style='text-align: center;'>%s<br>Dimensões: %dx%d</div></html>", 
                mapName, rows, cols)
        );
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setForeground(Color.DARK_GRAY);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(gridPanel, BorderLayout.CENTER);
        container.add(infoLabel, BorderLayout.SOUTH);
        
        mapPreviewPanel.add(container, BorderLayout.CENTER);
    }

    /**
     * Mostra mensagem de erro no preview.
     */
    private void showErrorPreview(String message) {
        JLabel errorLabel = new JLabel(
            "<html><div style='text-align: center; color: red;'>" + 
            message + "<br>Usando mapa padrão de grama</div></html>", 
            SwingConstants.CENTER
        );
        mapPreviewPanel.add(errorLabel, BorderLayout.CENTER);
    }

    /**
     * Retorna o nome legível do terreno.
     */
    private String getTerrainName(TerrainType terrain) {
        switch (terrain) {
            case GRASS: return "Grama";
            case WATER: return "Água";
            case TREE: return "Árvore";
            default: return "Desconhecido";
        }
    }

    /**
     * Inicia a simulação com as configurações selecionadas.
     */
    private void startSimulation() {
        int selectedIndex = mapSelector.getSelectedIndex();
        int hunterCount = (Integer) hunterSpinner.getValue();
        boolean useGrassOnly = grassOnlyCheckbox.isSelected();
        
        String selectedMap;
        if (useGrassOnly || selectedIndex == 0) {
            selectedMap = "GRASS_ONLY"; // Flag para mapa só de grama
        } else if (selectedIndex > 0 && selectedIndex - 1 < mapFiles.size()) {
            selectedMap = mapFiles.get(selectedIndex - 1);
        } else {
            selectedMap = "GRASS_ONLY"; // Fallback
        }
        
        // Fechar menu
        dispose();
        
        // Iniciar simulação
        final Simulator simulator = new Simulator(selectedMap, hunterCount, useGrassOnly);
        
        Thread simulationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                simulator.runLongSimulation();
                // Quando a simulação terminar, mostrar diálogo de opções
                // MAS a janela da simulação continua aberta até o usuário decidir
                showSimulationEndDialog();
            }
        });
        simulationThread.start();
    }

    /**
     * Mostra o diálogo quando a simulação termina.
     * A janela da simulação permanece aberta até o usuário escolher.
     */
    private void showSimulationEndDialog() {
        // O diálogo é mostrado, mas a janela do simulador continua aberta
        String[] options = {"Voltar ao Menu", "Sair", "Continuar Visualizando"};
        int choice = JOptionPane.showOptionDialog(null,
            "Simulação concluída!\nA simulação atingiu o número máximo de passos ou não é mais viável.\n\n" +
            "O que você deseja fazer?",
            "Simulação Finalizada",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[2]); // "Continuar Visualizando" como padrão
        
        if (choice == 0) {
            // Voltar ao menu - fechar a janela do simulador atual
            // A janela será fechada quando o usuário voltar ao menu
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MainMenu();
                }
            });
        } else if (choice == 1) {
            // Sair
            System.exit(0);
        }
        // Se escolher "Continuar Visualizando", não faz nada - a janela permanece aberta
    }

    /**
     * Método principal para iniciar a aplicação.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu();
            }
        });
    }
}