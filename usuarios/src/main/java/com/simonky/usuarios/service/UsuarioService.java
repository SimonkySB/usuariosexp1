package com.simonky.usuarios.service;

import java.util.List;


import com.simonky.usuarios.model.Direccion;
import com.simonky.usuarios.model.Rol;
import com.simonky.usuarios.model.Usuario;

public interface UsuarioService {
    

    List<Usuario> getUsuarios();
    Usuario getUsuarioById(Long id);

    void deleteUsuario(Long Id);
    Usuario createUsuario(Usuario usuario);
    Usuario updateUsuario(Long Id, Usuario usuario);

    void Iniciar();
    Usuario createDireccionUsuario(Long id, Direccion direccion);
    void deleteDireccionUsuario(Long id, Long dirId);
    Usuario updateDireccionUsuario(Long id, Long dirId, Direccion direccion);
    Usuario cambiarRoles(Long id, List<Rol> roles);
}
