package com.findamatch.views;

import com.findamatch.controller.UsuarioController;
import com.findamatch.model.dto.UsuarioDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame {
    // Colores del tema - reutilizando la paleta de MainView
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private static final Color PRIMARY_DARK = new Color(25, 118, 210);
    private static final Color ACCENT_COLOR = new Color(255, 193, 7);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    private static final Color ERROR_COLOR = new Color(244, 67, 54);
    
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    public LoginView() {
        initializeFrame();
        createLoginPanel();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Find a Match - Iniciar Sesión");
        setSize(480, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Look and Feel moderno
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            // Continuar con el Look and Feel por defecto
        }
    }

    private void createLoginPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Logo y título
        mainPanel.add(createHeaderPanel());
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Formulario
        mainPanel.add(createFormPanel());
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Botones
        mainPanel.add(createButtonPanel());

        add(mainPanel);
    }

    private JDialog createLoadingDialog(String mensaje) {
        JDialog dialog = new JDialog(this, "Cargando", true);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(mensaje, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(label, BorderLayout.CENTER);

        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        panel.add(progress, BorderLayout.SOUTH);

        dialog.getContentPane().add(panel);
        dialog.setSize(300, 120);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        return dialog;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);

        // Logo/Icono
        JLabel logoLabel = new JLabel("⚽");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título principal
        JLabel titleLabel = new JLabel("Find a Match");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Conecta, juega y disfruta");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
            new EmptyBorder(30, 30, 30, 30)
        ));

        // Campo usuario
        formPanel.add(createFieldLabel("👤 Usuario"));
        txtUsuario = createStyledTextField();
        formPanel.add(txtUsuario);
        formPanel.add(Box.createVerticalStrut(20));

        // Campo contraseña
        formPanel.add(createFieldLabel("🔒 Contraseña"));
        txtContrasena = createStyledPasswordField();
        formPanel.add(txtContrasena);

        return formPanel;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        styleInputField(field);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        styleInputField(field);
        return field;
    }

    private void styleInputField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(0, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Efecto focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    new EmptyBorder(11, 14, 11, 14)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Botón login principal
        JButton btnLogin = createPrimaryButton("Iniciar Sesión");
        btnLogin.addActionListener(this::handleLogin);
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(Box.createVerticalStrut(15));

        // Botón registro secundario
        JButton btnRegistrar = createSecondaryButton("¿No tienes cuenta? Regístrate");
        btnRegistrar.addActionListener(e -> {
            dispose();
            new RegisterView();
        });
        
        buttonPanel.add(btnRegistrar);

        return buttonPanel;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(new EmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));

        addHoverEffect(button, PRIMARY_COLOR, PRIMARY_DARK);
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(BACKGROUND_COLOR);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        addHoverEffect(button, BACKGROUND_COLOR, new Color(235, 235, 235));
        return button;
    }

    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    private void handleLogin(ActionEvent e) {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            showStyledMessage("Por favor, complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog loadingDialog = createLoadingDialog("Verificando credenciales...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            UsuarioDTO u = null;
            Exception error = null;

            @Override
            protected Void doInBackground() {
                try {
                    u = UsuarioController.getInstance().getUsuarioByUsernameDTO(usuario);
                } catch (Exception ex) {
                    error = ex;
                }
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();

                if (error != null) {
                    showStyledMessage("Error de conexión: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    error.printStackTrace();
                    return;
                }

                if (u == null) {
                    showStyledMessage("El usuario no existe", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!u.getContrasena().equals(contrasena)) {
                    showStyledMessage("Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    showStyledMessage("¡Bienvenido, " + usuario + "!", "Inicio exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new MainView(u);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }    

    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", CARD_COLOR);
        UIManager.put("Panel.background", CARD_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}