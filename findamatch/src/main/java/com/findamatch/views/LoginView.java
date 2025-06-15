package com.findamatch.views;

import com.findamatch.controller.UsuarioController;
import com.findamatch.model.dto.UsuarioDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    public LoginView() {
        setTitle("Iniciar Sesión - Find a Match");
        setSize(420, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Bienvenido a Find a Match", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        formPanel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        formPanel.add(txtUsuario);

        formPanel.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        formPanel.add(txtContrasena);

        JButton btnLogin = new JButton("Ingresar");
        JButton btnRegistrar = new JButton("Registrarse");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegistrar);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener((ActionEvent e) -> login());
        btnRegistrar.addActionListener((ActionEvent e) -> {
            dispose();
            new RegisterView();
        });

        setVisible(true);
    }

    private void login() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (UsuarioDTO u : UsuarioController.getInstance().getAllUsuariosDTO()) {
            if (u.getNombreUsuario().equals(usuario) && u.getContrasena().equals(contrasena)) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainView(u);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
