package com.simonky.usuarios.controller;


import java.util.List;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simonky.usuarios.model.Direccion;
import com.simonky.usuarios.model.Rol;
import com.simonky.usuarios.model.Usuario;
import com.simonky.usuarios.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping(value = "/usuarios")
@RestController
public class UsuarioController {

    
    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private UsuarioService usuarioService;


    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.usuarioService.Iniciar();
    }


    @GetMapping
    public List<Usuario> getUsuarios() {
        log.info("GET /usuarios");
        return this.usuarioService.getUsuarios();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.usuarioService.getUsuarioById(id));
    }


    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(this.usuarioService.createUsuario(usuario));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable("id") Long id) {
        this.usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> putUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(this.usuarioService.updateUsuario(id, usuario));
    }

    @PostMapping("/{id}/direcciones")
    public ResponseEntity<Usuario> createDireccionUsuario(@PathVariable("id") Long id, @RequestBody Direccion direccion) {
        return ResponseEntity.ok(this.usuarioService.createDireccionUsuario(id, direccion));
    }

    @DeleteMapping("/{id}/direcciones/{dirId}")
    public ResponseEntity<Object> deleteDireccionUsuario(@PathVariable("id") Long id, @PathVariable("dirId") Long dirId) {
        this.usuarioService.deleteDireccionUsuario(id, dirId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/direcciones/{dirId}")
    public ResponseEntity<Usuario> putDireccionUsuario(@PathVariable("id") Long id, @PathVariable("dirId") Long dirId, @RequestBody Direccion direccion) {
        return ResponseEntity.ok(this.usuarioService.updateDireccionUsuario(id, dirId, direccion));
    }
   

    @PostMapping("/{id}/roles")
    public ResponseEntity<Usuario> cambiarRoles(@PathVariable("id") Long id,  @RequestBody List<Rol> roles) {
        return ResponseEntity.ok(this.usuarioService.cambiarRoles(id, roles));
    }
    

}
