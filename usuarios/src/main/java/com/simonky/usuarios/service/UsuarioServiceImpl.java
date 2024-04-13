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
import com.simonky.usuarios.repository.RolRepository;
import com.simonky.usuarios.repository.UsuarioRepository;



@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

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
    public void Iniciar(){
        List<Usuario> usuarios = new ArrayList<>();
        
        for(int i = 0; i <= 5; i++) {
            Usuario user = new Usuario();
            user.setNombre("Usuario " + (i+1));
            user.setEmail("user" + (i+1) + "@email.com");
            user.setPassword("passwordSuperSergura#" + (i+1));
            usuarios.add(user);
        }
        
        this.usuarioRepository.saveAll(usuarios);

        var rol1 = new Rol();
        rol1.setNombre("ADMIN");

        var rol2 = new Rol();
        rol2.setNombre("CLIENTE");
        
        this.rolRepository.save(rol1);
        this.rolRepository.save(rol2);
    }


    @Override
    public Usuario createDireccionUsuario(Long id, Direccion direccion) {
        Usuario dbUser = this.usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));

        
        direccion.setId(null);
        dbUser.getDirecciones().add(direccion);
        
        return this.usuarioRepository.save(dbUser);
    }

    @Override
    public void deleteDireccionUsuario(Long id, Long dirId) {
        Usuario dbUser = this.usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));

        dbUser.getDirecciones()
            .stream()
            .filter(d -> d.getId() == dirId)
            .findFirst()
            .orElseThrow(() -> new DireccionNotFoundException(dirId));

        
        dbUser.getDirecciones().removeIf(d -> d.getId() == dirId);
        this.usuarioRepository.save(dbUser);
    }

    @Override
    public Usuario updateDireccionUsuario(Long id, Long dirId, Direccion nuevaDireccion) {
        Usuario dbUser = this.usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));


        
        Direccion direccion = dbUser.getDirecciones().stream()
            .filter(d -> d.getId() == dirId)
            .findFirst()
            .orElseThrow(() -> new DireccionNotFoundException(dirId));

        
        direccion.setCiudad(nuevaDireccion.getCiudad());
        direccion.setCalle(nuevaDireccion.getCalle());
        direccion.setPais(nuevaDireccion.getPais());
        direccion.setZipcode(nuevaDireccion.getZipcode());

        return this.usuarioRepository.save(dbUser);

    }

    @Override
    public Usuario cambiarRoles(Long id, List<Rol> roles) {
        Usuario dbUser = this.usuarioRepository.findById(id)
        .orElseThrow(() -> new UsuarioNotFoundException(id));

        Set<Long> rolIdsNuevos = roles.stream()
            .map(r -> r.getId())
            .collect(Collectors.toSet());


        List<Rol> rolesUsuario = this.rolRepository.findAll().stream()
            .filter(rol -> rolIdsNuevos.contains(rol.getId()))
            .collect(Collectors.toList());

        dbUser.setRoles(rolesUsuario);
        return this.usuarioRepository.save(dbUser);
    }

    
    
}
