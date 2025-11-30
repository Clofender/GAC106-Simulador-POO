import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JLabel mapPreview;
    private JTextArea descriptionArea;

    /**
     * Cria a tela inicial do simulador.
     */
    public MainMenu() {
        setTitle("Simulador de Ecossistema - Menu Inicial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocation(200, 100);
        
        initializeComponents();
        setupLayout();
        setupListeners();
        
        pack();
        setVisible(true);
    }

    /**
     * Inicializa os componentes da interface.
     */
    private void initializeComponents() {
        // Seletor de mapa
        String[] maps = {"Mapa 1 (Floresta Densa)", "Mapa 2 (Áreas Abertas)"};
        mapSelector = new JComboBox<>(maps);
        
        // Seletor de número de caçadores
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 0, 5, 1);
        hunterSpinner = new JSpinner(spinnerModel);
        
        // Botão de iniciar
        startButton = new JButton("Iniciar Simulação");
        startButton.setBackground(Color.GREEN);
        
        // Área de preview do mapa
        mapPreview = new JLabel("Preview do Mapa Selecionado");
        mapPreview.setPreferredSize(new Dimension(200, 200));
        mapPreview.setOpaque(true);
        mapPreview.setBackground(Color.LIGHT_GRAY);
        mapPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mapPreview.setHorizontalAlignment(SwingConstants.CENTER);
        
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
                "- Caçadores: Controla o número de caçadores ativos");
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(getBackground());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
    }

    /**
     * Configura o layout dos componentes.
     */
    private void setupLayout() {
        // Painel de configurações
        JPanel configPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        configPanel.setBorder(BorderFactory.createTitledBorder("Configurações da Simulação"));
        
        configPanel.add(new JLabel("Mapa:"));
        configPanel.add(mapSelector);
        configPanel.add(new JLabel("Número de Caçadores:"));
        configPanel.add(hunterSpinner);
        configPanel.add(new JLabel("Preview:"));
        configPanel.add(mapPreview);
        configPanel.add(new JLabel(""));
        configPanel.add(startButton);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainPanel.add(configPanel, BorderLayout.CENTER);
        
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("Sobre o Simulador"));
        descriptionScroll.setPreferredSize(new Dimension(400, 150));
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
    }

    /**
     * Atualiza o preview do mapa baseado na seleção.
     */
    private void updateMapPreview() {
        int selectedMap = mapSelector.getSelectedIndex() + 1;
        String previewText = "<html><div style='text-align: center;'>" +
                "Mapa " + selectedMap + " Selecionado<br>" +
                "Terreno: " + (selectedMap == 1 ? "Floresta Densa" : "Áreas Abertas") + "<br>" +
                "Dimensões: 50x50<br>" +
                "Água: ~10%<br>" +
                "Árvores: " + (selectedMap == 1 ? "Muitas" : "Poucas") +
                "</div></html>";
        mapPreview.setText(previewText);
    }

    /**
     * Inicia a simulação com as configurações selecionadas.
     */
    private void startSimulation() {
        int selectedMap = mapSelector.getSelectedIndex() + 1;
        int hunterCount = (Integer) hunterSpinner.getValue();
        
        // Fechar menu
        dispose();
        
        // Iniciar simulação em thread separada para não bloquear a UI
        final Simulator simulator = new Simulator(selectedMap, hunterCount);
        
        // Executar simulação em thread separada
        Thread simulationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                simulator.runLongSimulation();
            }
        });
        simulationThread.start();
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