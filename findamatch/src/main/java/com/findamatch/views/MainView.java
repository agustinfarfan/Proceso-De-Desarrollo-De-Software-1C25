package com.findamatch.views;

import com.findamatch.controller.UsuarioController;
import com.findamatch.controller.DeporteController;
import com.findamatch.controller.PartidoController;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.dto.DeporteDTO;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.geocoding.GoogleGeocoderAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MainView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel homePanel;
    private JPanel crearPartidoPanel;
    private JPanel unirsePartidoPanel;
    private JPanel historialPanel;
    
    // Colores del tema
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);      
    private static final Color PRIMARY_DARK = new Color(25, 118, 210);       
    private static final Color ACCENT_COLOR = new Color(255, 193, 7);        
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  
    private static final Color SIDEBAR_COLOR = new Color(38, 50, 56);        
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);       
    private static final Color WARNING_COLOR = new Color(255, 152, 0);       

    private UsuarioDTO usuarioActual;
    private JButton selectedButton = null;

    public MainView(UsuarioDTO usuarioDTO) {
        this.usuarioActual = usuarioDTO;
        initializeFrame();
        createComponents();
        setupLayout();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Find a Match - Panel Principal");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Establecer Look and Feel más moderno
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            // Continuar con el Look and Feel por defecto
        }
    }

    private void createComponents() {
        // Panels principales
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        homePanel = createHomePanel();
        crearPartidoPanel = createCrearPartidoPanel();
        unirsePartidoPanel = createUnirsePartidoPanel();
        historialPanel = createHistorialPanel();

        mainPanel.add(homePanel, "inicio");
        mainPanel.add(crearPartidoPanel, "crear");
        mainPanel.add(unirsePartidoPanel, "unirse");
        mainPanel.add(historialPanel, "historial");
    }

    private void setupLayout() {
        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header del sidebar con avatar y nombre
        JPanel headerPanel = createSidebarHeader();
        sidebar.add(headerPanel);
        sidebar.add(Box.createVerticalStrut(30));

        // Botones de navegación
        JButton btnInicio = createSidebarButton("🏠 Inicio", "inicio");
        JButton btnCrearPartido = createSidebarButton("⚽ Crear Partido", "crear");
        JButton btnUnirsePartido = createSidebarButton("👥 Unirse a Partido", "unirse");
        JButton btnHistorial = createSidebarButton("📊 Historial", "historial");

        sidebar.add(btnInicio);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnCrearPartido);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnUnirsePartido);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnHistorial);

        sidebar.add(Box.createVerticalGlue());

        // Botón de cerrar sesión en la parte inferior
        JButton btnSalir = createLogoutButton();
        sidebar.add(btnSalir);

        // Seleccionar el botón de inicio por defecto
        selectedButton = btnInicio;
        selectButton(btnInicio);

        return sidebar;
    }

    private JPanel createSidebarHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(SIDEBAR_COLOR);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Avatar placeholder
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        avatarLabel.setForeground(ACCENT_COLOR);
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre de usuario
        JLabel nameLabel = new JLabel(usuarioActual.getNombreUsuario());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Estado online
        JLabel statusLabel = new JLabel("● En línea");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(SUCCESS_COLOR);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(avatarLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(nameLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(statusLabel);

        return headerPanel;
    }

    private JButton createSidebarButton(String text, String action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 45));
        button.setMaximumSize(new Dimension(200, 45));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(SIDEBAR_COLOR);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efectos hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedButton != button) {
                    button.setBackground(PRIMARY_DARK);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedButton != button) {
                    button.setBackground(SIDEBAR_COLOR);
                }
            }
        });

        button.addActionListener(e -> {
            selectButton(button);
            cardLayout.show(mainPanel, action);
        });

        return button;
    }

    private JButton createLogoutButton() {
        JButton btnSalir = new JButton("🚪 Cerrar Sesión");
        btnSalir.setPreferredSize(new Dimension(200, 40));
        btnSalir.setMaximumSize(new Dimension(200, 40));
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setBackground(new Color(244, 67, 54)); // Red
        btnSalir.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnSalir.setFocusPainted(false);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSalir.setBackground(new Color(211, 47, 47));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSalir.setBackground(new Color(244, 67, 54));
            }
        });

        btnSalir.addActionListener(this::cerrarSesion);
        return btnSalir;
    }

    private void selectButton(JButton button) {
        if (selectedButton != null) {
            selectedButton.setBackground(SIDEBAR_COLOR);
        }
        selectedButton = button;
        button.setBackground(PRIMARY_COLOR);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel welcomeLabel = new JLabel("¡Bienvenido de vuelta, " + usuarioActual.getNombreUsuario() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Aquí tienes un resumen de tu actividad deportiva");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Stats Cards
        JPanel statsPanel = createStatsPanel();
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        try {
            int partidosJugados = PartidoController.getInstance().getAllPartidosDTO().size();
            List<DeporteDTO> deportes = DeporteController.getInstance().getAllDeportesDTOs();

            statsPanel.add(createStatCard("⚽", "Partidos Jugados", String.valueOf(partidosJugados), PRIMARY_COLOR));
            statsPanel.add(createStatCard("🏃", "Deportes Disponibles", String.valueOf(deportes.size()), SUCCESS_COLOR));
            statsPanel.add(createStatCard("🔔", "Notificaciones", "En desarrollo", WARNING_COLOR));
            statsPanel.add(createStatCard("🏆", "Próximamente", "Estadísticas avanzadas", ACCENT_COLOR));
        } catch (Exception e) {
            statsPanel.add(createStatCard("❌", "Error", "No se pudieron cargar las estadísticas", new Color(244, 67, 54)));
        }

        return statsPanel;
    }

    private JPanel createStatCard(String icon, String title, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(0, 0, 0, 0), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto sombra simulado
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);

        // Efecto hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 248, 248));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
            }
        });

        return card;
    }

    private JPanel createCrearPartidoPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BACKGROUND_COLOR);
    panel.setBorder(new EmptyBorder(30, 30, 30, 30));

    // Header
    JLabel titleLabel = new JLabel("Crear Partido");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    titleLabel.setForeground(TEXT_PRIMARY);

    JLabel subtitleLabel = new JLabel("Organiza un nuevo encuentro deportivo");
    subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    subtitleLabel.setForeground(TEXT_SECONDARY);

    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setBackground(BACKGROUND_COLOR);
    headerPanel.add(titleLabel);
    headerPanel.add(Box.createVerticalStrut(5));
    headerPanel.add(subtitleLabel);

    panel.add(headerPanel, BorderLayout.NORTH);

    // Formulario
    JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
    formPanel.setBackground(CARD_COLOR);
    formPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
        new EmptyBorder(20, 20, 20, 20)
    ));

    JLabel lblUbicacion = new JLabel("Ubicación:");
    JTextField txtUbicacion = new JTextField();

    JLabel lblFecha = new JLabel("Fecha (yyyy-MM-ddTHH:mm):");
    JTextField txtFecha = new JTextField();

    JLabel lblDuracion = new JLabel("Duración (min):");
    JTextField txtDuracion = new JTextField();

    JLabel lblMinPartidos = new JLabel("Mín. Partidos Jugados:");
    JTextField txtMinPartidos = new JTextField();

    JLabel lblDeporte = new JLabel("Deporte:");
    JComboBox<String> comboDeporte = new JComboBox<>();
    List<DeporteDTO> deportes = DeporteController.getInstance().getAllDeportesDTOs();
    for (DeporteDTO d : deportes) {
        comboDeporte.addItem(d.getNombre());
    }

    JButton btnCrear = new JButton("Crear Partido");
    btnCrear.setBackground(PRIMARY_COLOR);
    btnCrear.setForeground(Color.WHITE);
    btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));

    btnCrear.addActionListener((ActionEvent e) -> {
        try {
            String fechaTexto = txtFecha.getText();
            if (!esFechaValida(fechaTexto)) {
                JOptionPane.showMessageDialog(panel, "Formato de fecha inválido. Usa yyyy-MM-ddTHH:mm", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PartidoDTO partidoDTO = new PartidoDTO();
            partidoDTO.setCreador(usuarioActual);

            // Obtener coordenadas desde Google Maps
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setDireccion(txtUbicacion.getText());
            GoogleGeocoderAdapter geocoder = new GoogleGeocoderAdapter();
            ubicacion = geocoder.getUbicacion(ubicacion);
            partidoDTO.setUbicacion(ubicacion);

            partidoDTO.setComienzo(LocalDateTime.parse(fechaTexto));
            partidoDTO.setDuracion(Integer.parseInt(txtDuracion.getText()));
            partidoDTO.setMinimoPartidosJugados(Integer.parseInt(txtMinPartidos.getText()));

            String nombreDeporte = (String) comboDeporte.getSelectedItem();
            DeporteDTO deporteSeleccionado = deportes.stream()
                .filter(d -> d.getNombre().equals(nombreDeporte))
                .findFirst()
                .orElse(null);
            partidoDTO.setDeporte(deporteSeleccionado);

            List<UsuarioDTO> jugadores = new ArrayList<>();
            jugadores.add(usuarioActual);
            partidoDTO.setJugadores(jugadores);

            int id = PartidoController.getInstance().createPartido(partidoDTO);
            JOptionPane.showMessageDialog(panel, "Partido creado con ID: " + id);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, "Error al crear el partido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    formPanel.add(lblUbicacion);
    formPanel.add(txtUbicacion);
    formPanel.add(lblFecha);
    formPanel.add(txtFecha);
    formPanel.add(lblDuracion);
    formPanel.add(txtDuracion);
    formPanel.add(lblMinPartidos);
    formPanel.add(txtMinPartidos);
    formPanel.add(lblDeporte);
    formPanel.add(comboDeporte);
    formPanel.add(new JLabel());
    formPanel.add(btnCrear);

    panel.add(formPanel, BorderLayout.CENTER);
    return panel;
    }

    private boolean esFechaValida(String fechaTexto) {
    try {
        LocalDateTime.parse(fechaTexto);
        return true;
    } catch (Exception e) {
        return false;
    }
    }

    private JPanel createUnirsePartidoPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BACKGROUND_COLOR);
    panel.setBorder(new EmptyBorder(30, 30, 30, 30));

    JLabel titleLabel = new JLabel("Partidos Disponibles");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    titleLabel.setForeground(TEXT_PRIMARY);

    JLabel subtitleLabel = new JLabel("Encuentra el partido perfecto para ti");
    subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    subtitleLabel.setForeground(TEXT_SECONDARY);

    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setBackground(BACKGROUND_COLOR);
    headerPanel.add(titleLabel);
    headerPanel.add(Box.createVerticalStrut(5));
    headerPanel.add(subtitleLabel);

    panel.add(headerPanel, BorderLayout.NORTH);

    JPanel listPanel = new JPanel(new BorderLayout());
    listPanel.setBackground(CARD_COLOR);
    listPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
        new EmptyBorder(20, 20, 20, 20)
    ));

    DefaultListModel<String> partidoListModel = new DefaultListModel<>();
    JList<String> partidoList = new JList<>(partidoListModel);
    partidoList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    partidoList.setBackground(CARD_COLOR);
    partidoList.setSelectionBackground(PRIMARY_COLOR);
    partidoList.setSelectionForeground(Color.WHITE);
    partidoList.setBorder(new EmptyBorder(10, 10, 10, 10));

    try {
        List<PartidoDTO> partidos = PartidoController.getInstance().getAllPartidosDTO();
        if (partidos.isEmpty()) {
            partidoListModel.addElement("No hay partidos disponibles en este momento");
        } else {
            for (PartidoDTO p : partidos) {
                partidoListModel.addElement("🏃 ID: " + p.getId() + " | " + p.getDeporte().getNombre() +
                        " | 📍 " + p.getUbicacion() + " | 📅 " + p.getComienzo());
            }
        }
    } catch (Exception e) {
        partidoListModel.addElement("❌ Error al cargar los partidos");
    }

    JScrollPane scrollPane = new JScrollPane(partidoList);
    scrollPane.setBorder(null);
    scrollPane.getViewport().setBackground(CARD_COLOR);

    listPanel.add(scrollPane, BorderLayout.CENTER);
    panel.add(listPanel, BorderLayout.CENTER);

    return panel;
}

    private JPanel createHistorialPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BACKGROUND_COLOR);
    panel.setBorder(new EmptyBorder(30, 30, 30, 30));

    JLabel titleLabel = new JLabel("Historial de Partidos");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
    titleLabel.setForeground(TEXT_PRIMARY);

    JLabel subtitleLabel = new JLabel("Revisa tu actividad deportiva pasada");
    subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    subtitleLabel.setForeground(TEXT_SECONDARY);

    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setBackground(BACKGROUND_COLOR);
    headerPanel.add(titleLabel);
    headerPanel.add(Box.createVerticalStrut(5));
    headerPanel.add(subtitleLabel);

    panel.add(headerPanel, BorderLayout.NORTH);

    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(CARD_COLOR);
    contentPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
        new EmptyBorder(20, 20, 20, 20)
    ));

    DefaultListModel<String> historialListModel = new DefaultListModel<>();
    JList<String> historialList = new JList<>(historialListModel);
    historialList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    historialList.setBackground(CARD_COLOR);
    historialList.setSelectionBackground(PRIMARY_COLOR);
    historialList.setSelectionForeground(Color.WHITE);
    historialList.setBorder(new EmptyBorder(10, 10, 10, 10));

    try {
        List<PartidoDTO> historial = PartidoController.getInstance().getPartidosDeUsuario(usuarioActual.getId());
        if (historial.isEmpty()) {
            historialListModel.addElement("No has participado en partidos aún");
        } else {
            for (PartidoDTO p : historial) {
                historialListModel.addElement("✔️ ID: " + p.getId() + " | " + p.getDeporte().getNombre() +
                        " | " + p.getUbicacion() + " | " + p.getComienzo());
            }
        }
    } catch (Exception e) {
        historialListModel.addElement("❌ Error al cargar el historial");
    }

    JScrollPane scrollPane = new JScrollPane(historialList);
    scrollPane.setBorder(null);
    scrollPane.getViewport().setBackground(CARD_COLOR);

    contentPanel.add(scrollPane, BorderLayout.CENTER);
    panel.add(contentPanel, BorderLayout.CENTER);

    return panel;
    }

    private void cerrarSesion(ActionEvent e) {
        // Dialog personalizado
        UIManager.put("OptionPane.background", CARD_COLOR);
        UIManager.put("Panel.background", CARD_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        
        int option = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cerrar sesión?", 
            "Confirmar Cierre de Sesión", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                new LoginView().setVisible(true);
                this.dispose();
            });
        }
    }
}