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
        return ResponseEntity.ok(usuarioCollectionHateoas(usuarios)); 
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> getUsuarioById(@PathVariable("id") Long id) {
        var usuario = this.usuarioService.getUsuarioById(id);
        var res = this.usuarioHateoas(usuario);
        return ResponseEntity.ok(res);
    }


    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> createUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioCreado = this.usuarioService.createUsuario(usuario);
        var res = this.usuarioHateoas(usuarioCreado);
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
        var res = this.usuarioHateoas(usuarioActualizado);
        return ResponseEntity.ok(res);
    }


    @GetMapping("/{id}/direcciones")
    public ResponseEntity<CollectionModel<EntityModel<Direccion>>> getDireccionUsuario(@PathVariable("id") Long id) {
        var direcciones = usuarioService.getDireccionesUsuario(id);
        var res = direccionCollectionHateoas(id, direcciones);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/{id}/direcciones")
    public ResponseEntity<EntityModel<Direccion>> createDireccionUsuario(@PathVariable("id") Long id, @Valid @RequestBody Direccion direccion) {
        var direccionCreada = usuarioService.createDireccionUsuario(id, direccion);
        var res = direccionHateoas(id, direccionCreada);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/direcciones/{dirId}")
    public ResponseEntity<EntityModel<Direccion>> getDireccionUsuarioById(@PathVariable("id") Long id, @PathVariable("dirId") Long dirId) {
        var direccion = this.usuarioService.getDireccionUsuarioById(id, dirId);
        var res = direccionHateoas(id, direccion);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}/direcciones/{dirId}")
    public ResponseEntity<Void> deleteDireccionUsuario(@PathVariable("id") Long id, @PathVariable("dirId") Long dirId) {
        this.usuarioService.deleteDireccionUsuario(id, dirId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/direcciones/{dirId}")
    public ResponseEntity<EntityModel<Direccion>> putDireccionUsuario(@PathVariable("id") Long id, @PathVariable("dirId") Long dirId, @Valid @RequestBody Direccion direccion) {
        var direccionActualizada = this.usuarioService.updateDireccionUsuario(id, dirId, direccion);
        var res = direccionHateoas(id, direccionActualizada);
        return ResponseEntity.ok(res);
    }
   


    @GetMapping("/{id}/roles")
    public ResponseEntity<List<Rol>> getRolesUsuario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.usuarioService.getRolesUsuario(id));
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<List<Rol>> cambiarRoles(@PathVariable("id") Long id,  @RequestBody List<Rol> roles) {
        return ResponseEntity.ok(this.usuarioService.cambiarRoles(id, roles));
    }

    


    private EntityModel<Usuario> usuarioHateoas(Usuario entity){
        return EntityModel.of(entity,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(entity.getId())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarios()).withRel("usuarios")
        );
    }

    private CollectionModel<EntityModel<Usuario>> usuarioCollectionHateoas(List<Usuario> pacientes){
        List<EntityModel<Usuario>> dataRes = pacientes.stream()
            .map(p -> EntityModel.of(p,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(p.getId())).withSelfRel()
            )).collect(Collectors.toList());
        
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarios());
        CollectionModel<EntityModel<Usuario>> recursos = CollectionModel.of(dataRes, linkTo.withRel("usuarios"));
        return recursos;
    }


    private EntityModel<Direccion> direccionHateoas(Long usuarioId, Direccion entity){
        return EntityModel.of(entity,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getDireccionUsuarioById(usuarioId, entity.getId())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getDireccionUsuario(usuarioId)).withRel("direcciones")
        );
    }

    private CollectionModel<EntityModel<Direccion>> direccionCollectionHateoas(Long usuarioId, List<Direccion> entities) {
        List<EntityModel<Direccion>> dataRes = entities.stream()
        .map(p -> EntityModel.of(p,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getDireccionUsuarioById(usuarioId, p.getId())).withSelfRel()
        )).collect(Collectors.toList());
    
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getDireccionUsuario(usuarioId));
        CollectionModel<EntityModel<Direccion>> recursos = CollectionModel.of(dataRes, linkTo.withRel("direcciones"));
        return recursos;
    }

}
