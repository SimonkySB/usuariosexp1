package com.simonky.usuarios;

import java.util.List;

public class Usuario {

    private int usuarioId;
    private String nombre;
    private String email;
    private String password;

    private List<String> roles;
    private List<Direccion> direcciones;


    public Usuario(int usuarioId, String nombre, String email, String password, List<String> roles, List<Direccion> direcciones) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.direcciones = direcciones;
    }

    
    public int getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


    public List<String> getRoles() {
        return roles;
    }


    public List<Direccion> getDirecciones() {
        return direcciones;
    }


}
