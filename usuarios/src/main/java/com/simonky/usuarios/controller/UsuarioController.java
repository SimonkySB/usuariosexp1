package com.simonky.usuarios.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simonky.usuarios.model.Direccion;
import com.simonky.usuarios.model.Rol;
import com.simonky.usuarios.model.Usuario;
import com.simonky.usuarios.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping(value = "/usuarios")
@RestController
@Validated
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getUsuarios() {
        var usuarios = this.usuarioService.getUsuarios();

        List<EntityModel<Usuario>> dataRes = usuarios.stream()
            .map(p -> EntityModel.of(p,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(p.getId())).withSelfRel()
            )).collect(Collectors.toList());
        
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarios());
        CollectionModel<EntityModel<Usuario>> recursos = CollectionModel.of(dataRes, linkTo.withRel("usuarios"));

        return ResponseEntity.ok(recursos); 
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> getUsuarioById(@PathVariable("id") Long id) {
        EntityModel<Usuario> res = EntityModel.of(this.usuarioService.getUsuarioById(id),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(id)).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarios()).withRel("usuarios")
        );
        return ResponseEntity.ok(res);
    }


    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> createUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioCreado = this.usuarioService.createUsuario(usuario);
        EntityModel<Usuario> res = EntityModel.of(usuarioCreado,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(usuarioCreado.getId())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarios()).withRel("peliculas")
        );

        return ResponseEntity.ok(res);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable("id") Long id) {
        this.usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> putUsuario(@PathVariable("id") Long id, @Valid @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = this.usuarioService.updateUsuario(id, usuario);
        EntityModel<Usuario> res = EntityModel.of(usuarioActualizado,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(usuarioActualizado.getId())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarios()).withRel("peliculas")
        );

        return ResponseEntity.ok(res);
    }


    @GetMapping("/{id}/direcciones")
    public ResponseEntity<List<Direccion>> getDireccionUsuario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.usuarioService.getDireccionesUsuario(id));
    }


    @PostMapping("/{id}/direcciones")
    public ResponseEntity<Direccion> createDireccionUsuario(@PathVariable("id") Long id, @Valid @RequestBody Direccion direccion) {
        return ResponseEntity.ok(this.usuarioService.createDireccionUsuario(id, direccion));
    }

    @GetMapping("/{id}/direcciones/{dirId}")
    public ResponseEntity<Direccion> getDireccionUsuarioById(@PathVariable("id") Long id, @PathVariable("dirId") Long dirId) {
        return ResponseEntity.ok(this.usuarioService.getDireccionUsuarioById(id, dirId));
    }

    @DeleteMapping("/{id}/direcciones/{dirId}")
    public ResponseEntity<Void> deleteDireccionUsuario(@PathVariable("id") Long id, @PathVariable("dirId") Long dirId) {
        this.usuarioService.deleteDireccionUsuario(id, dirId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/direcciones/{dirId}")
    public ResponseEntity<Direccion> putDireccionUsuario(@PathVariable("id") Long id, @PathVariable("dirId") Long dirId, @Valid @RequestBody Direccion direccion) {
        return ResponseEntity.ok(this.usuarioService.updateDireccionUsuario(id, dirId, direccion));
    }
   


    @GetMapping("/{id}/roles")
    public ResponseEntity<List<Rol>> getRolesUsuario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.usuarioService.getRolesUsuario(id));
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<List<Rol>> cambiarRoles(@PathVariable("id") Long id,  @RequestBody List<Rol> roles) {
        return ResponseEntity.ok(this.usuarioService.cambiarRoles(id, roles));
    }

    

}
