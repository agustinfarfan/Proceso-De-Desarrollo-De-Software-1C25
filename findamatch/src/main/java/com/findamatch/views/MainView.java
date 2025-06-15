package com.findamatch.views;

import com.findamatch.controller.UsuarioController;
import com.findamatch.controller.DeporteController;
import com.findamatch.controller.PartidoController;
import com.findamatch.model.dto.DeporteDTO;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.dto.UsuarioDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel homePanel;
    private JPanel crearPartidoPanel;
    private JPanel unirsePartidoPanel;
    private JPanel historialPanel;

    private UsuarioDTO usuarioActual;

    public MainView(UsuarioDTO usuarioDTO) {
        this.usuarioActual = usuarioDTO;
        setTitle("Find a Match - Panel Principal");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(40, 40, 40));

        JButton btnInicio = new JButton("Inicio");
        JButton btnCrearPartido = new JButton("Crear Partido");
        JButton btnUnirsePartido = new JButton("Unirse a Partido");
        JButton btnHistorial = new JButton("Historial de Partidos");
        JButton btnSalir = new JButton("Cerrar Sesión");

        sidebar.add(btnInicio);
        sidebar.add(btnCrearPartido);
        sidebar.add(btnUnirsePartido);
        sidebar.add(btnHistorial);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnSalir);

        add(sidebar, BorderLayout.WEST);

        // Panels principales
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        homePanel = createHomePanel();
        crearPartidoPanel = createCrearPartidoPanel();
        unirsePartidoPanel = createUnirsePartidoPanel();
        historialPanel = createHistorialPanel();

        mainPanel.add(homePanel, "inicio");
        mainPanel.add(crearPartidoPanel, "crear");
        mainPanel.add(unirsePartidoPanel, "unirse");
        mainPanel.add(historialPanel, "historial");

        add(mainPanel, BorderLayout.CENTER);

        // Acciones
        btnInicio.addActionListener(e -> cardLayout.show(mainPanel, "inicio"));
        btnCrearPartido.addActionListener(e -> cardLayout.show(mainPanel, "crear"));
        btnUnirsePartido.addActionListener(e -> cardLayout.show(mainPanel, "unirse"));
        btnHistorial.addActionListener(e -> cardLayout.show(mainPanel, "historial"));
        btnSalir.addActionListener(this::cerrarSesion);

        setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Bienvenido, " + usuarioActual.getNombreUsuario() + "!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(3, 1));

        try {
            int partidosJugados = PartidoController.getInstance().getAllPartidosDTO().size();
            List<DeporteDTO> deportes = DeporteController.getInstance().getAllDeportesDTOs();

            statsPanel.add(new JLabel("Partidos jugados: " + partidosJugados));
            statsPanel.add(new JLabel("Deportes disponibles: " + deportes.size()));
            statsPanel.add(new JLabel("Notificaciones: (en desarrollo)"));
        } catch (Exception e) {
            statsPanel.add(new JLabel("Error al cargar estadísticas"));
        }

        panel.add(statsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCrearPartidoPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Aquí va el formulario para crear un partido."));
        return panel;
    }

    private JPanel createUnirsePartidoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel title = new JLabel("Partidos disponibles para unirse", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        DefaultListModel<String> partidoListModel = new DefaultListModel<>();
        JList<String> partidoList = new JList<>(partidoListModel);
        JScrollPane scrollPane = new JScrollPane(partidoList);
        panel.add(scrollPane, BorderLayout.CENTER);

        try {
            List<PartidoDTO> partidos = PartidoController.getInstance().getAllPartidosDTO();
            for (PartidoDTO p : partidos) {
                partidoListModel.addElement("Partido ID: " + p.getId() + " | Deporte ID: " + p.getIdDeporte() +
                        " | Lugar: " + p.getUbicacion() + " | Fecha: " + p.getComienzo());
            }
        } catch (Exception e) {
            partidoListModel.addElement("Error al cargar los partidos");
        }

        return panel;
    }

    private JPanel createHistorialPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Aquí irá el historial de partidos del usuario."));
        return panel;
    }

    private void cerrarSesion(ActionEvent e) {
        dispose();
        new LoginView();
    }
}
