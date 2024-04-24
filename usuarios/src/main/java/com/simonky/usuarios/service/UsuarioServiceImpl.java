package com.simonky.usuarios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simonky.usuarios.exceptions.DireccionNotFoundException;
import com.simonky.usuarios.exceptions.EmailExistsException;
import com.simonky.usuarios.exceptions.UsuarioNotFoundException;
import com.simonky.usuarios.model.Direccion;
import com.simonky.usuarios.model.Rol;
import com.simonky.usuarios.model.Usuario;
import com.simonky.usuarios.repository.DireccionRepository;
import com.simonky.usuarios.repository.RolRepository;
import com.simonky.usuarios.repository.UsuarioRepository;



@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Override
    public List<Usuario> getUsuarios() {
        
        return this.usuarioRepository.findAll();
    }

    @Override
    public Usuario getUsuarioById(Long id) {
        return this.usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Override
    public void deleteUsuario(Long Id) {
        this.usuarioRepository.findById(Id)
            .orElseThrow(() -> new UsuarioNotFoundException(Id));

        this.usuarioRepository.deleteById(Id);
    }

    

    @Override
    public Usuario createUsuario(Usuario usuario) {
        this.usuarioRepository.findByEmail(usuario.getEmail())
            .ifPresent(user -> {
                throw new EmailExistsException(usuario.getEmail());
            });

        usuario.setId(null);
        usuario.setDirecciones(new ArrayList<>());
        usuario.setRoles(new ArrayList<>());
        return this.usuarioRepository.save(usuario);
    }

    @Override
    public Usuario updateUsuario(Long Id, Usuario usuario) {
        Usuario dbUser = this.usuarioRepository.findById(Id)
            .orElseThrow(() -> new UsuarioNotFoundException(Id));

        Optional<Usuario> userEmail = this.usuarioRepository.findByEmail(usuario.getEmail());

        

        if(userEmail.isPresent() && dbUser.getId() != userEmail.get().getId()) {
            throw new EmailExistsException(usuario.getEmail());
        }

        dbUser.setEmail(usuario.getEmail());
        dbUser.setNombre(usuario.getNombre());
        dbUser.setPassword(usuario.getPassword());
        return this.usuarioRepository.save(dbUser);
    }




    @Override
    public Direccion createDireccionUsuario(Long id, Direccion direccion) {
        Usuario dbUser = this.usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));

        direccion.setId(null);
        direccion.setUsuarioId(dbUser.getId());
        
        return direccionRepository.save(direccion);
    }

    @Override
    public void deleteDireccionUsuario(Long id, Long dirId) {
        
        Direccion direccion = direccionRepository.findByUsuarioId(id)
            .stream()
            .filter(d -> d.getId() == dirId)
            .findFirst()
            .orElseThrow(() -> new DireccionNotFoundException(dirId));

        direccionRepository.delete(direccion);
    }

    @Override
    public Direccion updateDireccionUsuario(Long id, Long dirId, Direccion nuevaDireccion) {
 
        Direccion direccion = direccionRepository.findByUsuarioId(id).stream()
            .filter(d -> d.getId() == dirId)
            .findFirst()
            .orElseThrow(() -> new DireccionNotFoundException(dirId));

        
        direccion.setCiudad(nuevaDireccion.getCiudad());
        direccion.setCalle(nuevaDireccion.getCalle());
        direccion.setPais(nuevaDireccion.getPais());
        direccion.setZipcode(nuevaDireccion.getZipcode());

        return this.direccionRepository.save(direccion);
    }

    @Override
    public List<Direccion> getDireccionesUsuario(Long usuarioId) {
        return direccionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Direccion getDireccionUsuarioById(Long usuarioId, Long dirId) {
        return direccionRepository.findByUsuarioId(usuarioId).stream()
        .filter(d -> d.getId() == dirId)
        .findFirst()
        .orElseThrow(() -> new DireccionNotFoundException(dirId));
    }


    @Override
    public List<Rol> cambiarRoles(Long id, List<Rol> roles) {
        Usuario dbUser = this.usuarioRepository.findById(id)
        .orElseThrow(() -> new UsuarioNotFoundException(id));

        Set<Long> rolIdsNuevos = roles.stream()
            .map(r -> r.getId())
            .collect(Collectors.toSet());


        List<Rol> rolesUsuario = this.rolRepository.findAll().stream()
            .filter(rol -> rolIdsNuevos.contains(rol.getId()))
            .collect(Collectors.toList());

        dbUser.setRoles(rolesUsuario);
        
        return this.usuarioRepository.save(dbUser).getRoles();
    }

    @Override
    public List<Rol> getRolesUsuario(Long id) {
        Usuario dbUser = this.usuarioRepository.findById(id)
        .orElseThrow(() -> new UsuarioNotFoundException(id));
        return dbUser.getRoles();
    }

}
