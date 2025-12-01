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
    
    // Componentes da interface
    private JComboBox<String> mapSelector;        // Dropdown para seleção de mapas
    private JSpinner hunterSpinner;               // Controle para número de caçadores
    private JButton startButton;                  // Botão para iniciar simulação
    private JPanel mapPreviewPanel;               // Painel para visualização do mapa
    private JTextArea descriptionArea;            // Área de texto com informações
    private List<String> mapFiles;                // Lista de arquivos de mapa encontrados
    private JCheckBox grassOnlyCheckbox;          // Checkbox para mapa só de grama

    /**
     * Cria a tela inicial do simulador.
     */
    public MainMenu() {
        setTitle("Simulador de Ecossistema - Menu Inicial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha aplicação ao fechar janela
        setLayout(new BorderLayout()); // Layout principal
        setLocation(200, 100); // Posição inicial na tela
        
        // Carregar mapas disponíveis
        loadAvailableMaps();
        
        initializeComponents(); // Criar componentes da interface
        setupLayout();          // Organizar componentes na tela
        setupListeners();       // Configurar eventos dos componentes
        
        // Atualizar preview inicial
        updateMapPreview();
        
        pack();                 // Ajustar tamanho da janela ao conteúdo
        setVisible(true);       // Tornar janela visível
    }

    /**
     * Carrega os mapas disponíveis na pasta Mapas.
     */
    private void loadAvailableMaps() {
        mapFiles = new ArrayList<>(); // Inicializar lista vazia
        File mapDir = new File("Mapas"); // Referência para pasta de mapas
        
        // Verificar se pasta existe e é um diretório
        if (mapDir.exists() && mapDir.isDirectory()) {
            // Listar arquivos .txt na pasta
            File[] files = mapDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
            if (files != null) {
                // Adicionar cada arquivo à lista
                for (File file : files) {
                    mapFiles.add(file.getName());
                }
            }
        }
        
        // Se não encontrou mapas, adiciona padrões (fallback)
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
        String[] mapNames = new String[mapFiles.size() + 1]; // +1 para opção padrão
        mapNames[0] = "Grama (Mapa Padrão)"; // Primeira opção sempre mapa de grama
        
        // Preencher opções com nomes dos mapas
        for (int i = 0; i < mapFiles.size(); i++) {
            String fileName = mapFiles.get(i);
            String displayName = fileName.replace(".txt", "").replace("_", " "); // Formatar nome
            mapNames[i + 1] = displayName; // +1 porque índice 0 já está ocupado
        }
        mapSelector = new JComboBox<>(mapNames);
        mapSelector.setPreferredSize(new Dimension(200, 25)); // Tamanho fixo
        
        // Checkbox para mapa só de grama
        grassOnlyCheckbox = new JCheckBox("Usar apenas grama (ignorar mapa selecionado)");
        grassOnlyCheckbox.setSelected(false); // Inicialmente desmarcado
        
        // Seletor de número de caçadores
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 0, 5, 1); // Min 0, Max 5, passo 1
        hunterSpinner = new JSpinner(spinnerModel);
        hunterSpinner.setPreferredSize(new Dimension(60, 25)); // Tamanho compacto
        
        // Botão de iniciar
        startButton = new JButton("Iniciar Simulação");
        startButton.setBackground(new Color(34, 139, 34)); // Verde floresta
        startButton.setForeground(Color.WHITE); // Texto branco
        startButton.setPreferredSize(new Dimension(150, 30)); // Tamanho padrão
        
        // Painel de preview do mapa
        mapPreviewPanel = new JPanel(new BorderLayout()); // Layout para centralizar conteúdo
        mapPreviewPanel.setPreferredSize(new Dimension(300, 300)); // Tamanho fixo
        mapPreviewPanel.setBackground(Color.LIGHT_GRAY); // Cor de fundo
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
        descriptionArea.setEditable(false); // Somente leitura
        descriptionArea.setBackground(getBackground()); // Cor de fundo igual à janela
        descriptionArea.setLineWrap(true); // Quebra de linha automática
        descriptionArea.setWrapStyleWord(true); // Quebra por palavras
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Fonte legível
    }

    /**
     * Configura o layout dos componentes.
     */
    private void setupLayout() {
        // Painel de configurações com GridBagLayout para controle preciso
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder("Configurações da Simulação"));
        configPanel.setPreferredSize(new Dimension(450, 400)); // Tamanho preferencial
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margens internas
        gbc.fill = GridBagConstraints.HORIZONTAL; // Expandir horizontalmente
        gbc.weightx = 1.0; // Peso horizontal
        
        // Linha 1: Seletor de mapa
        gbc.gridx = 0; gbc.gridy = 0; // Posição (0,0)
        configPanel.add(new JLabel("Mapa:"), gbc); // Label "Mapa"
        
        gbc.gridx = 1; // Próxima coluna
        configPanel.add(mapSelector, gbc); // ComboBox de mapas
        
        // Linha 2: Checkbox apenas grama
        gbc.gridx = 0; gbc.gridy = 1; // Posição (0,1)
        gbc.gridwidth = 2; // Ocupar duas colunas
        configPanel.add(grassOnlyCheckbox, gbc); // Checkbox
        
        // Linha 3: Número de caçadores
        gbc.gridx = 0; gbc.gridy = 2; // Posição (0,2)
        gbc.gridwidth = 1; // Voltar a ocupar uma coluna
        configPanel.add(new JLabel("Número de Caçadores:"), gbc); // Label
        
        gbc.gridx = 1; // Próxima coluna
        configPanel.add(hunterSpinner, gbc); // Spinner de caçadores
        
        // Linha 4: Preview do mapa
        gbc.gridx = 0; gbc.gridy = 3; // Posição (0,3)
        gbc.gridwidth = 2; // Ocupar duas colunas
        gbc.fill = GridBagConstraints.BOTH; // Expandir em ambas direções
        gbc.weighty = 1.0; // Peso vertical (ocupa espaço restante)
        configPanel.add(mapPreviewPanel, gbc); // Painel de preview
        
        // Linha 5: Botão iniciar
        gbc.gridx = 0; gbc.gridy = 4; // Posição (0,4)
        gbc.gridwidth = 2; // Ocupar duas colunas
        gbc.fill = GridBagConstraints.NONE; // Não expandir
        gbc.weighty = 0; // Sem peso vertical
        gbc.anchor = GridBagConstraints.CENTER; // Centralizar
        configPanel.add(startButton, gbc); // Botão iniciar
        
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // Layout com espaçamento
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margens externas
        
        mainPanel.add(configPanel, BorderLayout.CENTER); // Configurações no centro
        
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea); // Área com scroll
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("Sobre o Simulador"));
        descriptionScroll.setPreferredSize(new Dimension(450, 120)); // Altura fixa
        mainPanel.add(descriptionScroll, BorderLayout.SOUTH); // Descrição na parte inferior
        
        add(mainPanel); // Adicionar painel principal à janela
    }

    /**
     * Configura os ouvintes de eventos.
     */
    private void setupListeners() {
        // Botão iniciar - inicia simulação quando clicado
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
        
        // Seletor de mapa - atualiza preview quando muda seleção
        mapSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMapPreview();
            }
        });
        
        // Checkbox grama - atualiza preview quando alterado
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
        mapPreviewPanel.removeAll(); // Limpar preview anterior
        
        if (grassOnlyCheckbox.isSelected()) {
            // Mostrar preview de mapa só de grama
            drawGrassOnlyPreview();
        } else {
            int selectedIndex = mapSelector.getSelectedIndex(); // Índice selecionado
            if (selectedIndex == 0) {
                // Mapa padrão de grama (primeira opção)
                drawGrassOnlyPreview();
            } else if (selectedIndex > 0 && selectedIndex - 1 < mapFiles.size()) {
                String selectedMap = mapFiles.get(selectedIndex - 1); // -1 porque índice 0 é grama
                String mapPath = "Mapas/" + selectedMap; // Caminho completo
                
                // Carrega o mapa para gerar preview
                try {
                    TerrainType[][] terrainMap = MapLoader.loadMap(mapPath, 50, 50); // Carregar mapa 50x50
                    drawFullMapPreview(terrainMap, selectedMap); // Desenhar preview detalhado
                } catch (Exception e) {
                    // Fallback: mostra mensagem de erro
                    showErrorPreview("Erro ao carregar mapa: " + selectedMap);
                }
            }
        }
        
        mapPreviewPanel.revalidate(); // Revalidar layout
        mapPreviewPanel.repaint();    // Redesenhar componente
    }

    /**
     * Desenha o preview de mapa só de grama.
     */
    private void drawGrassOnlyPreview() {
        JPanel gridPanel = new JPanel(new GridLayout(10, 10, 1, 1)); // Grade 10x10
        
        // Preencher grade com células verdes (grama)
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JPanel cell = new JPanel();
                cell.setBackground(TerrainType.GRASS.getColor()); // Cor da grama
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Borda cinza
                gridPanel.add(cell); // Adicionar célula à grade
            }
        }
        
        // Adicionar label informativa
        JLabel infoLabel = new JLabel("Mapa de Grama (50x50) - Todos os terrenos são grama");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Texto centralizado
        infoLabel.setForeground(Color.DARK_GRAY); // Cor discreta
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(gridPanel, BorderLayout.CENTER); // Grade no centro
        container.add(infoLabel, BorderLayout.SOUTH);  // Label embaixo
        
        mapPreviewPanel.add(container, BorderLayout.CENTER); // Adicionar ao preview
    }

    /**
     * Desenha o preview completo do mapa.
     */
    private void drawFullMapPreview(TerrainType[][] terrainMap, String mapName) {
        int rows = terrainMap.length;    // Número de linhas do mapa
        int cols = terrainMap[0].length; // Número de colunas do mapa
        
        // Calcular escala para caber no preview (máximo 20x20)
        int scale = Math.min(20, Math.min(300 / cols, 300 / rows)); // Escala baseada no tamanho
        int displayRows = Math.min(rows, 20); // Máximo 20 linhas no preview
        int displayCols = Math.min(cols, 20); // Máximo 20 colunas no preview
        
        JPanel gridPanel = new JPanel(new GridLayout(displayRows, displayCols, 1, 1)); // Grade para preview
        gridPanel.setPreferredSize(new Dimension(displayCols * scale, displayRows * scale)); // Tamanho proporcional
        
        // Calcular passo para amostragem se o mapa for muito grande
        int rowStep = rows / displayRows; // Passo entre linhas amostradas
        int colStep = cols / displayCols; // Passo entre colunas amostradas
        
        // Preencher grade com amostras do mapa real
        for (int i = 0; i < displayRows; i++) {
            for (int j = 0; j < displayCols; j++) {
                int actualRow = Math.min(i * rowStep, rows - 1); // Linha real no mapa (evitar overflow)
                int actualCol = Math.min(j * colStep, cols - 1); // Coluna real no mapa (evitar overflow)
                
                TerrainType terrain = terrainMap[actualRow][actualCol]; // Tipo de terreno na posição
                JPanel cell = new JPanel();
                cell.setBackground(terrain.getColor()); // Cor baseada no tipo de terreno
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Borda cinza
                
                // Tooltip com informações do terreno
                String tooltip = String.format("Posição: (%d,%d) - %s", 
                    actualRow, actualCol, getTerrainName(terrain));
                cell.setToolTipText(tooltip); // Texto ao passar mouse
                
                gridPanel.add(cell); // Adicionar célula à grade
            }
        }
        
        // Informações do mapa
        JLabel infoLabel = new JLabel(
            String.format("<html><div style='text-align: center;'>%s<br>Dimensões: %dx%d</div></html>", 
                mapName, rows, cols) // Nome e dimensões formatadas
        );
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centralizado
        infoLabel.setForeground(Color.DARK_GRAY); // Cor discreta
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(gridPanel, BorderLayout.CENTER); // Grade no centro
        container.add(infoLabel, BorderLayout.SOUTH);  // Informações embaixo
        
        mapPreviewPanel.add(container, BorderLayout.CENTER); // Adicionar ao preview
    }

    /**
     * Mostra mensagem de erro no preview.
     */
    private void showErrorPreview(String message) {
        JLabel errorLabel = new JLabel(
            "<html><div style='text-align: center; color: red;'>" + 
            message + "<br>Usando mapa padrão de grama</div></html>", 
            SwingConstants.CENTER // Mensagem centralizada e vermelha
        );
        mapPreviewPanel.add(errorLabel, BorderLayout.CENTER); // Adicionar mensagem de erro
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
        int selectedIndex = mapSelector.getSelectedIndex(); // Índice do mapa selecionado
        int hunterCount = (Integer) hunterSpinner.getValue(); // Número de caçadores
        boolean useGrassOnly = grassOnlyCheckbox.isSelected(); // Usar só grama?
        
        String selectedMap;
        if (useGrassOnly || selectedIndex == 0) {
            selectedMap = "GRASS_ONLY"; // Flag para mapa só de grama
        } else if (selectedIndex > 0 && selectedIndex - 1 < mapFiles.size()) {
            selectedMap = mapFiles.get(selectedIndex - 1); // Mapa do arquivo
        } else {
            selectedMap = "GRASS_ONLY"; // Fallback para grama
        }
        
        // Fechar menu
        dispose(); // Fechar esta janela
        
        // Iniciar simulação
        final Simulator simulator = new Simulator(selectedMap, hunterCount, useGrassOnly);
        
        // Executar simulação em thread separada para não travar a interface
        Thread simulationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                simulator.runLongSimulation(); // Executar simulação
                // Quando a simulação terminar, mostrar diálogo de opções
                // MAS a janela da simulação continua aberta até o usuário decidir
                showSimulationEndDialog();
            }
        });
        simulationThread.start(); // Iniciar thread da simulação
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
                    new MainMenu(); // Criar novo menu
                }
            });
        } else if (choice == 1) {
            // Sair
            System.exit(0); // Encerrar aplicação
        }
        // Se escolher "Continuar Visualizando", não faz nada - a janela permanece aberta
    }
}