package com.findamatch.views;

import com.findamatch.controller.UsuarioController;
import com.findamatch.model.dto.UsuarioDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterView extends JFrame {
    // Colores del tema - consistentes con LoginView y MainView
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private static final Color PRIMARY_DARK = new Color(25, 118, 210);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    private static final Color CANCEL_COLOR = new Color(244, 67, 54);

    private JTextField txtUsuario;
    private JTextField txtMail;
    private JPasswordField txtContrasena;
    private JTextField txtUbicacion;

    public RegisterView() {
        initializeFrame();
        createRegisterPanel();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Find a Match - Crear Cuenta");
        setSize(480, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            // Continuar con el Look and Feel por defecto
        }
    }

    private void createRegisterPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        // Header
        mainPanel.add(createHeaderPanel());
        mainPanel.add(Box.createVerticalStrut(25));
        
        // Formulario
        mainPanel.add(createFormPanel());
        mainPanel.add(Box.createVerticalStrut(25));
        
        // Botones
        mainPanel.add(createButtonPanel());

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);

        // Icono
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        JLabel titleLabel = new JLabel("Crear Cuenta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Únete a la comunidad deportiva");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
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

        // Campos del formulario
        addFormField(formPanel, "👤 Nombre de Usuario", txtUsuario = createStyledTextField());
        addFormField(formPanel, "📧 Correo Electrónico", txtMail = createStyledTextField());
        addFormField(formPanel, "🔒 Contraseña", txtContrasena = createStyledPasswordField());
        addFormField(formPanel, "📍 Ubicación", txtUbicacion = createStyledTextField());

        return formPanel;
    }

    private void addFormField(JPanel parent, String labelText, JTextField field) {
        parent.add(createFieldLabel(labelText));
        parent.add(Box.createVerticalStrut(5));
        parent.add(field);
        parent.add(Box.createVerticalStrut(18));
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Botón registrar
        JButton btnRegistrar = createPrimaryButton("Crear Cuenta");
        btnRegistrar.addActionListener(this::handleRegistrar);

        // Botón volver
        JButton btnAtras = createSecondaryButton("Volver al Login");
        btnAtras.addActionListener(e -> {
            dispose();
            new LoginView();
        });

        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnAtras);

        return buttonPanel;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(SUCCESS_COLOR);
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addHoverEffect(button, SUCCESS_COLOR, new Color(67, 160, 71));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(CANCEL_COLOR);
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addHoverEffect(button, CANCEL_COLOR, new Color(211, 47, 47));
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

    private void handleRegistrar(ActionEvent e) {
        // Lógica original mantenida exactamente igual
        UsuarioDTO nuevo = new UsuarioDTO();
        nuevo.setNombreUsuario(txtUsuario.getText());
        nuevo.setMail(txtMail.getText());
        nuevo.setContrasena(new String(txtContrasena.getPassword()));
        nuevo.setUbicacion(txtUbicacion.getText());

        int id = UsuarioController.getInstance().createUsuario(nuevo);
        nuevo.setId(id);

        showStyledMessage("¡Cuenta creada exitosamente! Ya puedes iniciar sesión.", "Registro completado", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginView();
    }

    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", CARD_COLOR);
        UIManager.put("Panel.background", CARD_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}