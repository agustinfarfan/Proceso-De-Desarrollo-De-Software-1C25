package com.findamatch.model.dto;

public class UsuarioDTO {
    private String nombreUsuario;
    private String mail;
    private String contraseña;
    private int edad;

    // Constructor
    public UsuarioDTO() {}

    public UsuarioDTO(String nombreUsuario, String mail, String contraseña, int edad) {
        this.nombreUsuario = nombreUsuario;
        this.mail = mail;
        this.contraseña = contraseña;
        this.edad = edad;
    }

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
