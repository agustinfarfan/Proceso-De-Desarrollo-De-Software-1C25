package com.findamatch.views;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private String currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel welcomeLabel;
    
    // Paneles para cada sección
    private JPanel homePanel;
    private JPanel createMatchPanel;
    private JPanel joinMatchPanel;
    private JPanel historyPanel;
    
    public MainView(String username) {
        this.currentUser = username;
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setTitle("Gestión de Partidos - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Crear paneles para cada sección
        createHomePanel();
        createMatchPanel();
        createJoinMatchPanel();
        createHistoryPanel();
        
        // Agregar paneles al CardLayout
        contentPanel.add(homePanel, "HOME");
        contentPanel.add(createMatchPanel, "CREATE");
        contentPanel.add(joinMatchPanel, "JOIN");
        contentPanel.add(historyPanel, "HISTORY");
    }
    
    private void createHomePanel() {
        homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Color.WHITE);
        
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        
        welcomeLabel = new JLabel("¡Bienvenido, " + currentUser + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(51, 51, 51));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        welcomePanel.add(welcomeLabel, gbc);
        
        JLabel instructionLabel = new JLabel("Selecciona una opción del menú lateral para comenzar");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        welcomePanel.add(instructionLabel, gbc);
        
        // Panel de estadísticas rápidas
        JPanel statsPanel = createStatsPanel();
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        welcomePanel.add(statsPanel, gbc);
        
        homePanel.add(welcomePanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        
        // Tarjetas de estadísticas
        JPanel matchesPlayedCard = createStatCard("Partidos Jugados", "15", new Color(70, 130, 180));
        JPanel matchesWonCard = createStatCard("Partidos Ganados", "12", new Color(60, 179, 113));
        JPanel upcomingCard = createStatCard("Próximos Partidos", "3", new Color(255, 140, 0));
        
        statsPanel.add(matchesPlayedCard);
        statsPanel.add(matchesWonCard);
        statsPanel.add(upcomingCard);
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createRaisedBevelBorder());
        card.setPreferredSize(new Dimension(150, 80));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void createMatchPanel() {
        createMatchPanel = new JPanel(new BorderLayout());
        createMatchPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Crear Nuevo Partido", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        createMatchPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Deporte
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Deporte:"), gbc);
        
        JComboBox<String> sportCombo = new JComboBox<>(new String[]{"Fútbol", "Básquet", "Tenis", "Vóley", "Paddle"});
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(sportCombo, gbc);
        
        // Fecha
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Fecha:"), gbc);
        
        JTextField dateField = new JTextField("dd/mm/yyyy", 15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(dateField, gbc);
        
        // Hora
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Hora:"), gbc);
        
        JTextField timeField = new JTextField("HH:MM", 15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(timeField, gbc);
        
        // Lugar
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Lugar:"), gbc);
        
        JTextField locationField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(locationField, gbc);
        
        // Jugadores máximos
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Jugadores máximos:"), gbc);
        
        JSpinner maxPlayersSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 50, 1));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(maxPlayersSpinner, gbc);
        
        // Descripción
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Descripción:"), gbc);
        
        JTextArea descriptionArea = new JTextArea(4, 15);
        descriptionArea.setBorder(BorderFactory.createLoweredBevelBorder());
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(scrollPane, gbc);
        
        // Botón crear
        JButton createButton = new JButton("Crear Partido");
        createButton.setBackground(new Color(60, 179, 113));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(createButton, gbc);
        
        createMatchPanel.add(formPanel, BorderLayout.CENTER);
    }
    
    private void createJoinMatchPanel() {
        joinMatchPanel = new JPanel(new BorderLayout());
        joinMatchPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Unirse a Partido", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        joinMatchPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Panel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        filterPanel.add(new JLabel("Deporte:"));
        JComboBox<String> filterSport = new JComboBox<>(new String[]{"Todos", "Fútbol", "Básquet", "Tenis", "Vóley", "Paddle"});
        filterPanel.add(filterSport);
        
        filterPanel.add(new JLabel("Fecha:"));
        JTextField filterDate = new JTextField("dd/mm/yyyy", 10);
        filterPanel.add(filterDate);
        
        JButton searchButton = new JButton("Buscar");
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        filterPanel.add(searchButton);
        
        joinMatchPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Lista de partidos disponibles
        String[] columnNames = {"Deporte", "Fecha", "Hora", "Lugar", "Jugadores", "Organizador", "Acción"};
        Object[][] data = {
            {"Fútbol", "15/06/2025", "18:00", "Cancha Municipal", "8/11", "Juan Pérez", "Unirse"},
            {"Básquet", "16/06/2025", "20:00", "Club Deportivo", "4/10", "María García", "Unirse"},
            {"Tenis", "17/06/2025", "17:30", "Club Tennis", "2/4", "Carlos López", "Unirse"}
        };
        
        JTable matchTable = new JTable(data, columnNames);
        matchTable.setRowHeight(30);
        JScrollPane tableScrollPane = new JScrollPane(matchTable);
        
        joinMatchPanel.add(tableScrollPane, BorderLayout.CENTER);
    }
    
    private void createHistoryPanel() {
        historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Historial de Partidos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        historyPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Pestañas para separar partidos jugados y organizados
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Partidos jugados
        String[] playedColumns = {"Deporte", "Fecha", "Lugar", "Resultado", "Organizador"};
        Object[][] playedData = {
            {"Fútbol", "10/06/2025", "Cancha Municipal", "Victoria", "Juan Pérez"},
            {"Básquet", "08/06/2025", "Club Deportivo", "Derrota", "María García"},
            {"Tenis", "05/06/2025", "Club Tennis", "Victoria", "Carlos López"}
        };
        
        JTable playedTable = new JTable(playedData, playedColumns);
        playedTable.setRowHeight(25);
        JScrollPane playedScrollPane = new JScrollPane(playedTable);
        tabbedPane.addTab("Partidos Jugados", playedScrollPane);
        
        // Partidos organizados
        String[] organizedColumns = {"Deporte", "Fecha", "Lugar", "Jugadores", "Estado"};
        Object[][] organizedData = {
            {"Fútbol", "12/06/2025", "Cancha Norte", "11/11", "Completado"},
            {"Vóley", "20/06/2025", "Polideportivo", "6/12", "Pendiente"}
        };
        
        JTable organizedTable = new JTable(organizedData, organizedColumns);
        organizedTable.setRowHeight(25);
        JScrollPane organizedScrollPane = new JScrollPane(organizedTable);
        tabbedPane.addTab("Partidos Organizados", organizedScrollPane);
        
        historyPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Barra lateral
        JPanel sidePanel = createSidePanel();
        add(sidePanel, BorderLayout.WEST);
        
        // Panel de contenido
        add(contentPanel, BorderLayout.CENTER);
        
        // Barra superior
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
    }
    
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(new Color(45, 45, 45));
        sidePanel.setPreferredSize(new Dimension(200, 0));
        
        // Logo/Título
        JLabel logoLabel = new JLabel("PARTIDOS", SwingConstants.CENTER);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidePanel.add(logoLabel, BorderLayout.NORTH);
        
        // Panel de botones de navegación
        JPanel navPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        navPanel.setBackground(new Color(45, 45, 45));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton homeButton = createNavButton("🏠 Inicio", "HOME");
        JButton createButton = createNavButton("⚽ Crear Partido", "CREATE");
        JButton joinButton = createNavButton("👥 Unirse a Partido", "JOIN");
        JButton historyButton = createNavButton("📊 Historial", "HISTORY");
        
        navPanel.add(homeButton);
        navPanel.add(createButton);
        navPanel.add(joinButton);
        navPanel.add(historyButton);
        
        sidePanel.add(navPanel, BorderLayout.CENTER);
        
        // Panel inferior con logout
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(45, 45, 45));
        
        JButton logoutButton = new JButton("🚪 Cerrar Sesión");
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> logout());
        
        bottomPanel.add(logoutButton);
        sidePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return sidePanel;
    }
    
    private JButton createNavButton(String text, String panel) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(180, 40));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(90, 90, 90));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(70, 70, 70));
            }
        });
        
        button.addActionListener(e -> showPanel(panel));
        
        return button;
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(250, 250, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Información del usuario
        JLabel userLabel = new JLabel("Usuario: " + currentUser);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(userLabel, BorderLayout.EAST);
        
        // Fecha actual
        JLabel dateLabel = new JLabel("Fecha: " + java.time.LocalDate.now().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(dateLabel, BorderLayout.WEST);
        
        return topPanel;
    }
    
    private void setupEventListeners() {
        // Los event listeners ya están configurados en los métodos create
    }
    
    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
    
    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cerrar sesión?", 
            "Confirmar Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                new LoginView().setVisible(true);
                this.dispose();
            });
        }
    }

}
