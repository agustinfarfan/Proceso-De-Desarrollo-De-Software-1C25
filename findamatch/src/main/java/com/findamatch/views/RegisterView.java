package com.findamatch.views;

import com.findamatch.controller.UsuarioController;
import com.findamatch.model.dto.UsuarioDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField txtUsuario;
    private JTextField txtMail;
    private JPasswordField txtContrasena;
    private JTextField txtUbicacion;

    public RegisterView() {
        setTitle("Registro - Find a Match");
        setSize(420, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Crear cuenta", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        formPanel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        formPanel.add(txtUsuario);

        formPanel.add(new JLabel("Email:"));
        txtMail = new JTextField();
        formPanel.add(txtMail);

        formPanel.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        formPanel.add(txtContrasena);

        formPanel.add(new JLabel("Ubicación:"));
        txtUbicacion = new JTextField();
        formPanel.add(txtUbicacion);

        JButton btnRegistrar = new JButton("Registrarse");
        JButton btnAtras = new JButton("Volver");

        btnRegistrar.setBackground(new Color(30, 144, 255));
        btnRegistrar.setForeground(Color.WHITE);
        btnAtras.setBackground(new Color(220, 20, 60));
        btnAtras.setForeground(Color.WHITE);

        formPanel.add(btnRegistrar);
        formPanel.add(btnAtras);

        add(formPanel, BorderLayout.CENTER);

        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrar();
            }
        });

        btnAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginView();
            }
        });

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    private void registrar() {
        UsuarioDTO nuevo = new UsuarioDTO();
        nuevo.setNombreUsuario(txtUsuario.getText());
        nuevo.setMail(txtMail.getText());
        nuevo.setContrasena(new String(txtContrasena.getPassword()));
        nuevo.setUbicacion(txtUbicacion.getText());

        int id = UsuarioController.getInstance().createUsuario(nuevo);
        nuevo.setId(id);

        JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente");
        dispose();
        new LoginView();
    }
}
