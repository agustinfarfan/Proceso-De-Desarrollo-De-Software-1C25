package com.findamatch.views;

import com.findamatch.controller.UsuarioController;
import com.findamatch.controller.DeporteController;
import com.findamatch.controller.PartidoController;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.model.dto.DeporteDTO;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.dto.UsuarioDeporteDTO;
import com.findamatch.model.enums.Nivel;
import com.findamatch.model.estado.FactoryEstado;
import com.findamatch.model.estado.IEstadoPartido;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.geocoding.GoogleGeocoderAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class MainView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel homePanel;
    private JPanel crearPartidoPanel;
    private JPanel unirsePartidoPanel;
    private JPanel historialPanel;
    private JPanel abmDeportesPanel;
    private JPanel configuracionPanel;

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

    public MainView(UsuarioDTO usuarioActual) {
        this.usuarioActual = usuarioActual;
        initializeFrame();
        createComponents();
        setupLayout();

        setVisible(true);

        showPanel("loading");

        SwingUtilities.invokeLater(() -> {
            refrescarHomePanel();
            showPanel("inicio");
        });
    }   

    private void initializeFrame() {
        setTitle("Find a Match");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel loadingPanel = new JPanel();
        loadingPanel.setBackground(BACKGROUND_COLOR);
        loadingPanel.add(new JLabel("Cargando..."));
        mainPanel.add(loadingPanel, "loading");

        add(mainPanel); 
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
        JButton btnABMDeportes = createSidebarButton("🏋️‍♂️ ABM Deportes", "abm_deportes");
        JButton btnConfiguracion = createSidebarButton("🔧 Configuracion", "configuracion");

        sidebar.add(btnABMDeportes);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnInicio);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnCrearPartido);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnUnirsePartido);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnHistorial);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnConfiguracion);

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
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
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

            switch (action) {
                case "inicio":
                    refrescarHomePanel();
                    break;
                case "crear":
                    refrescarCrearPartidoPanel();
                    break;
                case "unirse":
                    refrescarUnirsePartidoPanel();
                    break;
                case "historial":
                    refrescarHistorialPanel();
                    break;
                case "abm_deportes":
                    refrescarABMDeportesPanel();
                    break;
                case "configuracion":
                    refrescarConfiguracionPanel();
                    break;
            }

            cardLayout.show(mainPanel, action);
        });        

        return button;
    }

    private void refrescarHomePanel() {
        JDialog loadingDialog = createLoadingDialog("Cargando inicio...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                if (homePanel != null) {
                    mainPanel.remove(homePanel);
                }
                homePanel = createHomePanel();
                return null;
            }

            @Override
            protected void done() {
                mainPanel.add(homePanel, "inicio");
                loadingDialog.dispose();
            }
        };
        worker.execute();
        loadingDialog.setVisible(true);
    }      
    
    private void refrescarCrearPartidoPanel() {
        JDialog loadingDialog = createLoadingDialog("Cargando formulario de creación...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                if (crearPartidoPanel != null) {
                    mainPanel.remove(crearPartidoPanel);
                }
                crearPartidoPanel = createCrearPartidoPanel();
                return null;
            }

            @Override
            protected void done() {
                mainPanel.add(crearPartidoPanel, "crear");
                loadingDialog.dispose();
            }
        };
        worker.execute();
        loadingDialog.setVisible(true);
    }    
    
    private void refrescarUnirsePartidoPanel() {
        JDialog loadingDialog = createLoadingDialog("Buscando partidos disponibles...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                if (unirsePartidoPanel != null) {
                    mainPanel.remove(unirsePartidoPanel);
                }
                unirsePartidoPanel = createUnirsePartidoPanel();
                return null;
            }

            @Override
            protected void done() {
                mainPanel.add(unirsePartidoPanel, "unirse");
                loadingDialog.dispose();
            }
        };
        worker.execute();
        loadingDialog.setVisible(true);
    }     
    
    private void refrescarHistorialPanel() {
        JDialog loadingDialog = createLoadingDialog("Cargando historial...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                if (historialPanel != null) {
                    mainPanel.remove(historialPanel);
                }
                historialPanel = createHistorialPanel();
                return null;
            }

            @Override
            protected void done() {
                mainPanel.add(historialPanel, "historial");
                loadingDialog.dispose();
            }
        };
        worker.execute();
        loadingDialog.setVisible(true);
    }

    private void refrescarConfiguracionPanel() {
        JDialog loadingDialog = createLoadingDialog("Cargando configuracion...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                if (configuracionPanel != null) {
                    mainPanel.remove(configuracionPanel);
                }
                configuracionPanel = createConfiguracionPanel();
                return null;
            }

            @Override
            protected void done() {
                mainPanel.add(configuracionPanel, "configuracion");
                loadingDialog.dispose();
            }
        };
        worker.execute();
        loadingDialog.setVisible(true);
    }

    private JButton createLogoutButton() {
        JButton btnSalir = new JButton("🚪 Cerrar Sesión");
        btnSalir.setPreferredSize(new Dimension(200, 40));
        btnSalir.setMaximumSize(new Dimension(200, 40));
        btnSalir.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
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
            /*
             * int partidosJugados =
             * PartidoController.getInstance().getAllPartidosDTO().size();
             * List<DeporteDTO> deportes =
             * DeporteController.getInstance().getAllDeportesDTOs();
             */

            statsPanel.add(createStatCard("⚽", "Partidos Jugados",
                    String.valueOf(this.usuarioActual.getPartidos().size()), PRIMARY_COLOR));
            statsPanel.add(createStatCard("🏃", "Deportes Disponibles",
                    String.valueOf(this.usuarioActual.getDeportes().size()), SUCCESS_COLOR));
            statsPanel.add(createStatCard("🔔", "Notificaciones", "En desarrollo", WARNING_COLOR));
            statsPanel.add(createStatCard("🏆", "Próximamente", "Estadísticas avanzadas", ACCENT_COLOR));
        } catch (Exception e) {
            statsPanel.add(
                    createStatCard("❌", "Error", "No se pudieron cargar las estadísticas", new Color(244, 67, 54)));
        }

        return statsPanel;
    }

    private JPanel createStatCard(String icon, String title, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 0, 0, 0), 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto sombra simulado
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
                new EmptyBorder(20, 20, 20, 20)));

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
        try {
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

            JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
            formPanel.setBackground(CARD_COLOR);
            formPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
                    new EmptyBorder(20, 20, 20, 20)));

            JLabel lblUbicacion = new JLabel("Ubicación:");
            JTextField txtUbicacion = new JTextField();

            JLabel lblFecha = new JLabel("Fecha y hora:");

            LocalDateTime now = LocalDateTime.now();
            JSpinner spDia = new JSpinner(new SpinnerNumberModel(now.getDayOfMonth(), 1, 31, 1));
            JSpinner spMes = new JSpinner(new SpinnerNumberModel(now.getMonthValue(), 1, 12, 1));
            JSpinner spAnio = new JSpinner(new SpinnerNumberModel(now.getYear(), 2025, 2026, 1));
            JSpinner spHora = new JSpinner(new SpinnerNumberModel(now.getHour(), 0, 23, 1));
            JSpinner spMin = new JSpinner(new SpinnerNumberModel(now.getMinute(), 0, 59, 1));

            Dimension smallSpinnerSize = new Dimension(45, 24);
            for (JSpinner sp : List.of(spDia, spMes, spAnio, spHora, spMin)) {
                sp.setPreferredSize(smallSpinnerSize);
                sp.setMinimumSize(smallSpinnerSize);
                sp.setMaximumSize(smallSpinnerSize);
                sp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                sp.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                sp.setBackground(Color.WHITE);
                sp.setOpaque(true);
                ((JSpinner.DefaultEditor) sp.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);
            }

            JLabel slash1 = new JLabel("/");
            JLabel slash2 = new JLabel("/");
            JLabel colon = new JLabel(":");

            for (JLabel lbl : List.of(slash1, slash2, colon)) {
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }

            JPanel fechaPanel = new JPanel();
            fechaPanel.setLayout(new BoxLayout(fechaPanel, BoxLayout.X_AXIS));
            fechaPanel.setBackground(CARD_COLOR);

            Function<String, JLabel> createLabel = text -> {
                JLabel label = new JLabel(text);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                label.setForeground(TEXT_SECONDARY);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                return label;
            };

            Function<JSpinner, JPanel> spinnerColumn = (spinner) -> {
                JPanel panelCol = new JPanel();
                panelCol.setLayout(new BoxLayout(panelCol, BoxLayout.Y_AXIS));
                panelCol.setOpaque(false);
                panelCol.add(spinner);
                panelCol.add(Box.createVerticalStrut(3));
                return panelCol;
            };

            JPanel diaCol = spinnerColumn.apply(spDia);
            diaCol.add(createLabel.apply("Día"));

            JPanel mesCol = spinnerColumn.apply(spMes);
            mesCol.add(createLabel.apply("Mes"));

            JPanel anioCol = spinnerColumn.apply(spAnio);
            anioCol.add(createLabel.apply("Año"));

            JPanel horaCol = spinnerColumn.apply(spHora);
            horaCol.add(createLabel.apply("Hora"));

            JPanel minCol = spinnerColumn.apply(spMin);
            minCol.add(createLabel.apply("Min"));

            fechaPanel.add(diaCol);
            fechaPanel.add(Box.createHorizontalStrut(10));
            fechaPanel.add(mesCol);
            fechaPanel.add(Box.createHorizontalStrut(10));
            fechaPanel.add(anioCol);
            fechaPanel.add(Box.createHorizontalStrut(20));
            fechaPanel.add(horaCol);
            fechaPanel.add(Box.createHorizontalStrut(5));
            fechaPanel.add(new JLabel(":"));
            fechaPanel.add(Box.createHorizontalStrut(5));
            fechaPanel.add(minCol);

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
                JDialog loading = createLoadingDialog((JFrame) SwingUtilities.getWindowAncestor(panel),
                        "Creando partido...");

                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    private int partidoId = -1;
                    private Exception error = null;

                    @Override
                    protected Void doInBackground() {
                        try {
                            LocalDateTime comienzo = LocalDateTime.of(
                                    (Integer) spAnio.getValue(),
                                    (Integer) spMes.getValue(),
                                    (Integer) spDia.getValue(),
                                    (Integer) spHora.getValue(),
                                    (Integer) spMin.getValue());

                            if (!esFechaValida(comienzo)) {
                                throw new IllegalArgumentException("La fecha y hora deben ser futuras.");
                            }

                            PartidoDTO partidoDTO = new PartidoDTO();
                            partidoDTO.setCreador(usuarioActual);
                            partidoDTO.setUbicacion(new Ubicacion(txtUbicacion.getText()).getDireccion());
                            partidoDTO.setComienzo(comienzo);
                            partidoDTO.setDuracion(Integer.parseInt(txtDuracion.getText()));
                            partidoDTO.setMinimoPartidosJugados(Integer.parseInt(txtMinPartidos.getText()));

                            String nombreDeporte = (String) comboDeporte.getSelectedItem();
                            DeporteDTO deporteSeleccionado = deportes.stream()
                                    .filter(d -> d.getNombre().equals(nombreDeporte))
                                    .findFirst()
                                    .orElse(null);
                            partidoDTO.setDeporte(deporteSeleccionado);

                            partidoDTO.setJugadores(List.of(usuarioActual));
                            partidoDTO.setEstado("1");

                            partidoId = PartidoController.getInstance().createPartido(partidoDTO);
                            usuarioActual = UsuarioController.getInstance().getUsuarioByIdDTO(usuarioActual.getId());

                        } catch (Exception ex) {
                            this.error = ex;
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        loading.dispose();
                        if (error != null) {
                            if (error instanceof IllegalArgumentException) {
                                JOptionPane.showMessageDialog(panel, error.getMessage(), "Fecha inválida",
                                        JOptionPane.WARNING_MESSAGE);
                            } else {
                                error.printStackTrace();
                                JOptionPane.showMessageDialog(panel, "Error al crear el partido: " + error.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(panel, "Partido creado con ID: " + partidoId);
                        }
                    }
                };

                worker.execute();
                SwingUtilities.invokeLater(() -> loading.setVisible(true));
            });                       

            formPanel.add(lblUbicacion);
            formPanel.add(txtUbicacion);
            formPanel.add(lblFecha);
            formPanel.add(fechaPanel);
            formPanel.add(lblDuracion);
            formPanel.add(txtDuracion);
            formPanel.add(lblMinPartidos);
            formPanel.add(txtMinPartidos);
            formPanel.add(lblDeporte);
            formPanel.add(comboDeporte);
            formPanel.add(new JLabel());
            formPanel.add(btnCrear);

            panel.add(formPanel, BorderLayout.CENTER);

        } catch (DateTimeException ex) {
            JOptionPane.showMessageDialog(panel, "La fecha ingresada no es válida (por ejemplo, 31/02 no existe).",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, "Error al crear el partido: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return panel;
    }

    private boolean esFechaValida(LocalDateTime fecha) {
        return fecha.isAfter(LocalDateTime.now());
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

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(CARD_COLOR);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
                new EmptyBorder(20, 20, 20, 20)));

        String[] columnNames = { "ID", "Deporte", "Ubicación", "Fecha" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable partidoTable = new JTable(tableModel);
        partidoTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        partidoTable.setRowHeight(28);
        partidoTable.setBackground(CARD_COLOR);
        partidoTable.setForeground(TEXT_PRIMARY);
        partidoTable.setSelectionBackground(PRIMARY_COLOR);
        partidoTable.setSelectionForeground(Color.WHITE);
        partidoTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        partidoTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(partidoTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(CARD_COLOR);

        try {

            List<PartidoDTO> disponibles = UsuarioController.getInstance().buscarPartidos(this.usuarioActual);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            if (disponibles.isEmpty()) {
                tableModel.addRow(new Object[] { "", "No hay partidos disponibles", "", "" });
            } else {
                for (PartidoDTO p : disponibles) {
                    tableModel.addRow(new Object[] {
                            p.getId(),
                            p.getDeporte().getNombre(),
                            p.getUbicacion(),
                            p.getComienzo().format(formatter)
                    });
                }
            }

        } catch (Exception e) {
            tableModel.addRow(new Object[] { "", "❌ Error al cargar los partidos", "", "" });
        }

        JButton unirseButton = new JButton("Unirse al partido");
        unirseButton.setBackground(PRIMARY_COLOR);
        unirseButton.setForeground(Color.WHITE);
        unirseButton.setFocusPainted(false);
        unirseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        unirseButton.addActionListener(e -> {
            int selectedRow = partidoTable.getSelectedRow();
            if (selectedRow != -1) {
                Object value = tableModel.getValueAt(selectedRow, 0);
                if (value instanceof Integer) {
                    int partidoId = (int) value;
                    try {
                        PartidoDTO seleccionado = PartidoController.getInstance()
                                .getAllPartidosDTO()
                                .stream()
                                .filter(p -> p.getId() == partidoId)
                                .findFirst()
                                .orElse(null);

                        if (seleccionado != null) {
                            PartidoController.getInstance().agregarJugadorAPartido(seleccionado, usuarioActual);
                            JOptionPane.showMessageDialog(panel, "¡Te uniste al partido exitosamente!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "Error al unirse al partido: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccioná un partido primero.");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(unirseButton);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

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

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);

        String[] columnasHistorial = { "ID", "Deporte", "Ubicación", "Fecha", "Estado" };
        String[] columnasCreados = { "ID", "Deporte", "Ubicación", "Fecha", "Estado" };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        JLabel labelHistorial = new JLabel("Partidos en los que participaste:");
        labelHistorial.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelHistorial.setForeground(TEXT_PRIMARY);
        labelHistorial.setBorder(new EmptyBorder(10, 0, 5, 0));

        DefaultTableModel modeloHistorial = new DefaultTableModel(columnasHistorial, 0);
        JTable tablaHistorial = new JTable(modeloHistorial);
        configurarTabla(tablaHistorial);

        JLabel labelCreados = new JLabel("Partidos que creaste:");
        labelCreados.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelCreados.setForeground(TEXT_PRIMARY);
        labelCreados.setBorder(new EmptyBorder(20, 0, 5, 0));

        DefaultTableModel modeloCreados = new DefaultTableModel(columnasCreados, 0);
        JTable tablaCreados = new JTable(modeloCreados) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column != 4)
                    return false;

                String estadoActual = getValueAt(row, column).toString();
                IEstadoPartido estado = FactoryEstado.getEstadoByName(estadoActual);
                return estado != null && estado.getTransicionesValidas() != null
                        && !estado.getTransicionesValidas().isEmpty();
            }
        };
        
        configurarTabla(tablaCreados);
            
        try {
            List<PartidoDTO> todos = this.usuarioActual.getPartidos();

            if (todos.isEmpty()) {
                modeloHistorial.addRow(new Object[] { "❌", "Error al cargar", "-", "-" });
            } else {
                for (PartidoDTO p : todos) {
                    Object[] filaBase = new Object[] {
                            p.getId(),
                            p.getDeporte().getNombre(),
                            p.getUbicacion(),
                            p.getComienzo().format(formatter),
                            p.getEstado()
                    };                    

                    if (p.getCreador() != null && p.getCreador().getId() == usuarioActual.getId()) {
                        Object[] filaCreador = Arrays.copyOf(filaBase, 5);
                        filaCreador[4] = p.getEstado();
                        modeloCreados.addRow(filaCreador);
                    } else {
                        modeloHistorial.addRow(filaBase);
                    }
                }

                if (modeloHistorial.getRowCount() == 0)
                    modeloHistorial.addRow(new Object[] { "-", "No participaste en ningún partido", "-", "-" });
                if (modeloCreados.getRowCount() == 0)
                    modeloCreados.addRow(new Object[] { "-", "No creaste ningún partido", "-", "-", "-" });
            }
        } catch (Exception e) {
            modeloHistorial.addRow(new Object[] { "❌", "Error al cargar", "-", "-", "-" });
            modeloCreados.addRow(new Object[] { "❌", "Error al cargar", "-", "-", "-" });
        }

        TableColumn estadoColumn = tablaCreados.getColumnModel().getColumn(4);
        estadoColumn.setCellEditor(new EstadoPartidoCellEditor(tablaCreados));
        estadoColumn.setCellRenderer(getEstadoRenderer());

        contentPanel.add(labelHistorial);
        contentPanel.add(crearScrollPane(tablaHistorial));
        contentPanel.add(labelCreados);
        contentPanel.add(crearScrollPane(tablaCreados));

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createConfiguracionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Tus Deportes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Seleccioná tu nivel, favorito y estrategia de emparejamiento");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel deportesPanel = new JPanel();
        deportesPanel.setLayout(new BoxLayout(deportesPanel, BoxLayout.Y_AXIS));
        deportesPanel.setBackground(BACKGROUND_COLOR);

        List<DeporteDTO> deportes;
        List<UsuarioDeporteDTO> misDeportesDTO;
        try {
            deportes = DeporteController.getInstance().getAllDeportesDTOs();
            usuarioActual = UsuarioController.getInstance().getUsuarioByIdDTO(usuarioActual.getId());
            misDeportesDTO = usuarioActual.getDeportes();
        } catch (Exception e) {
            deportesPanel.add(new JLabel("❌ Error al cargar los deportes"));
            panel.add(deportesPanel, BorderLayout.CENTER);
            return panel;
        }

        Map<DeporteDTO, JComboBox<Nivel>> nivelMap = new HashMap<>();
        Map<DeporteDTO, JCheckBox> favoritoMap = new HashMap<>();

        for (DeporteDTO deporte : deportes) {
            JPanel deporteRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            deporteRow.setBackground(BACKGROUND_COLOR);

            JLabel lbl = new JLabel(deporte.getNombre());
            lbl.setPreferredSize(new Dimension(120, 25));
            lbl.setForeground(TEXT_PRIMARY);

            JComboBox<Nivel> comboNivel = new JComboBox<>(Nivel.values());
            JCheckBox chkFavorito = new JCheckBox("Favorito");
            chkFavorito.setBackground(BACKGROUND_COLOR);

            UsuarioDeporteDTO existente = misDeportesDTO.stream()
                    .filter(ud -> ud.getDeporte().getId() == deporte.getId())
                    .findFirst()
                    .orElse(null);

            if (existente != null) {
                comboNivel.setSelectedItem(existente.getNivel());
                chkFavorito.setSelected(existente.isEsFavorito());
            } else {
                comboNivel.setSelectedItem(Nivel.PRINCIPIANTE);
                chkFavorito.setSelected(false);
            }

            nivelMap.put(deporte, comboNivel);
            favoritoMap.put(deporte, chkFavorito);

            deporteRow.add(lbl);
            deporteRow.add(comboNivel);
            deporteRow.add(chkFavorito);
            deportesPanel.add(deporteRow);
        }

        JScrollPane scrollPane = new JScrollPane(deportesPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ComboBox de Estrategia
        JPanel estrategiaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        estrategiaPanel.setBackground(BACKGROUND_COLOR);
        JLabel estrategiaLabel = new JLabel("Estrategia de emparejamiento:");
        estrategiaLabel.setForeground(TEXT_PRIMARY);
        estrategiaPanel.add(estrategiaLabel);

        String[] estrategias = { "HISTORIAL", "CERCANIA", "NIVEL" };
        JComboBox<String> estrategiaCombo = new JComboBox<>(estrategias);
        estrategiaCombo.setSelectedItem(usuarioActual.getEstrategia());
        estrategiaPanel.add(estrategiaCombo);

        deportesPanel.add(Box.createVerticalStrut(20));
        deportesPanel.add(estrategiaPanel);

        // Botón guardar
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(e -> {
            try {
                boolean huboCambios = false;

                for (DeporteDTO d : deportes) {
                    Nivel nivelSeleccionado = (Nivel) nivelMap.get(d).getSelectedItem();
                    boolean favoritoSeleccionado = favoritoMap.get(d).isSelected();

                    UsuarioDeporteDTO existente = misDeportesDTO.stream()
                            .filter(ud -> ud.getDeporte().getId() == d.getId())
                            .findFirst()
                            .orElse(null);

                    boolean hayCambios = false;

                    if (existente == null) {
                        hayCambios = true;
                    } else {
                        hayCambios = !existente.getNivel().equals(nivelSeleccionado)
                                || existente.isEsFavorito() != favoritoSeleccionado;
                    }

                    if (hayCambios) {
                        UsuarioDeporte ud = new UsuarioDeporte();
                        ud.setUsuario(UsuarioController.getInstance().dtoToUsuario(usuarioActual));
                        ud.setDeporte(DeporteController.getInstance().dtoToDeporte(d));
                        ud.setNivelJuego(nivelSeleccionado);
                        ud.setEsFavorito(favoritoSeleccionado);

                        UsuarioController.getInstance().guardarOActualizarUsuarioDeporte(ud);
                        huboCambios = true;
                    }
                }

                String estrategiaSeleccionada = (String) estrategiaCombo.getSelectedItem();
                if (!estrategiaSeleccionada.equals(usuarioActual.getEstrategia())) {
                    usuarioActual.setEstrategia(estrategiaSeleccionada);
                    UsuarioController.getInstance().updateUsuario(usuarioActual);
                    huboCambios = true;
                }

                if (huboCambios) {
                    JOptionPane.showMessageDialog(panel, "Cambios guardados con éxito.");
                } else {
                    JOptionPane.showMessageDialog(panel, "No se detectaron cambios.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error al guardar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBackground(BACKGROUND_COLOR);
        southPanel.add(btnGuardar);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }


    private TableCellRenderer getEstadoRenderer() {
    return new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            String estadoActual = value != null ? value.toString() : "";
            IEstadoPartido estado = FactoryEstado.getEstadoByName(estadoActual);

            if (estado != null && estado.getTransicionesValidas() != null && !estado.getTransicionesValidas().isEmpty()) {
                JComboBox<String> comboBox = new JComboBox<>();
                comboBox.addItem(estadoActual);
                for (String t : estado.getTransicionesValidas()) {
                    if (!t.equals(estadoActual)) comboBox.addItem(t);
                }
                comboBox.setEnabled(false);
                comboBox.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                return comboBox;
            } else {
                // Estado final, se muestra como texto
                JLabel label = new JLabel(estadoActual);
                label.setOpaque(true);
                label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                label.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        }
    };
}

    class EstadoPartidoCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JComboBox<String> comboBox;
        private JTable tabla;
        private int fila;
        private String estadoAnterior;

        public EstadoPartidoCellEditor(JTable tabla) {
            this.tabla = tabla;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            this.fila = row;
            this.estadoAnterior = (value != null) ? value.toString() : "";
            comboBox = new JComboBox<>();

            IEstadoPartido estado = FactoryEstado.getEstadoByName(estadoAnterior);

            if (estado == null || estado.getTransicionesValidas().isEmpty()) {
                comboBox.addItem(estadoAnterior);
                comboBox.setEnabled(false);
                return comboBox;
            }            

            comboBox.addItem(estadoAnterior);
            for (String transicion : estado.getTransicionesValidas()) {
                if (!transicion.equals(estadoAnterior)) {
                    comboBox.addItem(transicion);
                }
            }

            comboBox.setSelectedItem(estadoAnterior);
            comboBox.addActionListener(e -> {
                String nuevoEstado = (String) comboBox.getSelectedItem();
                if (!nuevoEstado.equals(estadoAnterior)) {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "¿Querés cambiar el estado del partido de '" + estadoAnterior + "' a '" + nuevoEstado
                                    + "'?",
                            "Confirmar cambio de estado",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm != JOptionPane.YES_OPTION) {
                        comboBox.setSelectedItem(estadoAnterior);
                    } else {
                        JDialog loading = createLoadingDialog((JFrame) SwingUtilities.getWindowAncestor(tabla),"Actualizando Estado...");
                        SwingWorker<Void, Void> worker = new SwingWorker<>() {
                            @Override
                            protected Void doInBackground() throws Exception {
                                int partidoId = (int) tabla.getValueAt(fila, 0);
                                PartidoController controller = PartidoController.getInstance();

                                switch (nuevoEstado.toUpperCase()) {
                                    case "EN_CURSO":
                                        controller.comenzarPartido(partidoId);
                                        break;
                                    case "CONFIRMADO":
                                        controller.confirmarPartido(partidoId);
                                        break;
                                    case "CANCELADO":
                                        controller.cancelarPartido(partidoId);
                                        break;
                                    case "FINALIZADO":
                                        controller.finalizarPartido(partidoId);
                                        break;
                                    default:
                                        throw new IllegalStateException("Estado no soportado: " + nuevoEstado);
                                }
                                return null;
                            }

                            @Override
                            protected void done() {
                                loading.dispose();
                                tabla.setValueAt(nuevoEstado, fila, 4);
                                JOptionPane.showMessageDialog(null, "Estado actualizado correctamente.");
                                stopCellEditing();
                            }
                        };

                        worker.execute();
                        loading.setVisible(true);
                    }
                }
            });            
            return comboBox;
        }        

        @Override
        public Object getCellEditorValue() {
            return comboBox.getSelectedItem();
        }
    }

    private void configurarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.setBackground(CARD_COLOR);
        tabla.setForeground(TEXT_PRIMARY);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(BACKGROUND_COLOR);
        tabla.getTableHeader().setForeground(TEXT_PRIMARY);
    }

    private JScrollPane crearScrollPane(JTable tabla) {
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(CARD_COLOR);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 0, 20)),
                new EmptyBorder(10, 0, 10, 0)));
        return scroll;
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

    private JDialog createLoadingDialog(String mensaje) {
        JDialog loadingDialog = new JDialog(this, "Por favor espere...", true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel label = new JLabel(mensaje);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel.add(label, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        loadingDialog.setContentPane(panel);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        return loadingDialog;
    }
    
    public void showPanel(String nombrePanel) {
        cardLayout.show(mainPanel, nombrePanel);
    }

    private JDialog createLoadingDialog(JFrame parent, String mensaje) {
        JDialog dialog = new JDialog(parent, "Cargando", true);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(mensaje != null ? mensaje : "Cargando...");
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setBackground(new Color(30, 30, 30));

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(progressBar);

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        return dialog;
    }
    private void refrescarABMDeportesPanel() {
        JDialog loadingDialog = createLoadingDialog("Cargando deportes...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                if (abmDeportesPanel != null) {
                    mainPanel.remove(abmDeportesPanel);
                }
                abmDeportesPanel = createABMDeportesPanel();
                return null;
            }

            @Override
            protected void done() {
                mainPanel.add(abmDeportesPanel, "abm_deportes");
                loadingDialog.dispose();
            }
        };
        worker.execute();
        loadingDialog.setVisible(true);
    }

    private JPanel createABMDeportesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Gestión de Deportes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Agregá, editá o eliminá deportes disponibles en el sistema");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listDeportes = new JList<>(listModel);
        listDeportes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(listDeportes);

        JTextField txtNombre = new JTextField(20);
        JTextField txtDescripcion = new JTextField(20);
        JSpinner spMin = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JSpinner spMax = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        try {
            List<DeporteDTO> deportes = DeporteController.getInstance().getAllDeportesDTOs();
            for (DeporteDTO d : deportes) {
                listModel.addElement(d.getNombre());
            }
        } catch (Exception e) {
            listModel.addElement("❌ Error al cargar deportes");
        }

        listDeportes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String seleccionado = listDeportes.getSelectedValue();
                if (seleccionado != null) {
                    try {
                        List<DeporteDTO> deportes = DeporteController.getInstance().getAllDeportesDTOs();
                        DeporteDTO dep = deportes.stream()
                                .filter(d -> d.getNombre().equals(seleccionado))
                                .findFirst().orElse(null);
                        if (dep != null) {
                            txtNombre.setText(dep.getNombre());
                            txtDescripcion.setText(dep.getDescripcion());
                            spMin.setValue(dep.getCantMinJugadores());
                            spMax.setValue(dep.getCantMaxJugadores());
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Error al cargar datos del deporte.");
                    }
                }
            }
        });

        btnAgregar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String desc = txtDescripcion.getText().trim();
                int min = (int) spMin.getValue();
                int max = (int) spMax.getValue();

                if (nombre.isEmpty())
                    throw new Exception("El nombre no puede estar vacío");

                DeporteDTO nuevo = new DeporteDTO(nombre, min, max, desc);
                DeporteController.getInstance().createDeporte(nuevo);

                listModel.addElement(nombre);
                JOptionPane.showMessageDialog(panel, "Deporte creado con éxito.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        btnEditar.addActionListener(e -> {
            String nombreSeleccionado = listDeportes.getSelectedValue();
            if (nombreSeleccionado == null) {
                JOptionPane.showMessageDialog(panel, "Seleccioná un deporte para editar.");
                return;
            }

            try {
                String nombre = txtNombre.getText().trim();
                String desc = txtDescripcion.getText().trim();
                int min = (int) spMin.getValue();
                int max = (int) spMax.getValue();

                if (nombre.isEmpty())
                    throw new Exception("El nombre no puede estar vacío.");

                List<DeporteDTO> deportes = DeporteController.getInstance().getAllDeportesDTOs();
                DeporteDTO original = deportes.stream()
                        .filter(d -> d.getNombre().equals(nombreSeleccionado))
                        .findFirst()
                        .orElseThrow(() -> new Exception("No se encontró el deporte original."));

                DeporteDTO actualizado = new DeporteDTO(
                        original.getId(),
                        nombre,
                        min,
                        max,
                        desc);

                DeporteController.getInstance().updateDeporte(actualizado);

                int index = listDeportes.getSelectedIndex();
                listModel.set(index, nombre);
                listDeportes.setSelectedIndex(index);

                JOptionPane.showMessageDialog(panel, "Deporte actualizado correctamente.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error al actualizar: " + ex.getMessage());
            }
        });

        btnEliminar.addActionListener(e -> {
            String seleccionado = listDeportes.getSelectedValue();
            if (seleccionado == null)
                return;

            int confirm = JOptionPane.showConfirmDialog(panel, "¿Eliminar deporte '" + seleccionado + "'?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    List<DeporteDTO> deportes = DeporteController.getInstance().getAllDeportesDTOs();
                    DeporteDTO deporte = deportes.stream()
                            .filter(d -> d.getNombre().equals(seleccionado))
                            .findFirst()
                            .orElseThrow(() -> new Exception("No se encontró el deporte seleccionado."));

                    DeporteController.getInstance().deleteDeporte(deporte.getId());
                    listModel.removeElement(seleccionado);
                    JOptionPane.showMessageDialog(panel, "Deporte eliminado.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            }
        });        

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(new EmptyBorder(20, 0, 0, 0));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Descripción:"));
        form.add(txtDescripcion);
        form.add(new JLabel("Mínimo Jugadores:"));
        form.add(spMin);
        form.add(new JLabel("Máximo Jugadores:"));
        form.add(spMax);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        form.add(new JLabel(""));
        form.add(botones);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }    
        
}