package com.findamatch.model.dto;

import java.util.List;

import com.findamatch.model.UsuarioDeporte;

public class UsuarioDTO {

    private int id;
    private String nombreUsuario;
    private String mail;
    private String contrasena;
    private String ubicacion;
    private List<UsuarioDeporteDTO> deportesDTO;

    // Constructor
    public UsuarioDTO() {
    }

    public UsuarioDTO(String nombreUsuario, String mail, String contrasena, String ubicacion) {
        this.nombreUsuario = nombreUsuario;
        this.mail = mail;
        this.contrasena = contrasena;
        this.ubicacion = ubicacion;
    }

    public UsuarioDTO(int id,
            String nombreUsuario,
            String mail,
            String contrasena,
            String ubicacion,
            List<UsuarioDeporteDTO> deportesDTO) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.mail = mail;
        this.contrasena = contrasena;
        this.ubicacion = ubicacion;
        this.deportesDTO = deportesDTO;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<UsuarioDeporteDTO> getDeportes() {
        return deportesDTO;
    }

    public void setDeportes(List<UsuarioDeporteDTO> deportesDTO) {
        this.deportesDTO = deportesDTO;
    }

    // ToString
    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", mail='" + mail + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }

}
