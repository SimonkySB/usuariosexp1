package com.simonky.usuarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RequestMapping(value = "/usuarios")
@RestController
public class UsuarioController {

    private List<Usuario> usuarios = new ArrayList<>();

    public UsuarioController() {
        usuarios.add(new Usuario(
            1, 
            "simon",
            "simon@gmail.com", 
            "1234", 
            Arrays.asList("CLIENTE-C1"), 
            Arrays.asList(
                new Direccion(1, "San martín 123", "Chile", "Concecepción", "12314")
            )
        ));
        usuarios.add(new Usuario(
            2, 
            "marcela", 
            "marcela@gmail.com", 
            "1234", 
            Arrays.asList("CLIENTE-C1"), 
            Arrays.asList(
                new Direccion(2, "Carrera 123", "Chile", "Concepción", "1234")
            )
        ));
        usuarios.add(new Usuario(
            3, 
            "juan", 
            "juan@gmail.com", 
            "1234", 
            Arrays.asList("CLIENTE-C1", "ADMIN"), 
            Arrays.asList(
                new Direccion(3, "Miguel claro 123", "Chile", "Santiago", "1234"),
                new Direccion(4, "Matta 123 depto 9", "Chile", "Antofagasta", "1234")
            )

        ));
        usuarios.add(new Usuario(
            4, 
            "sofia", 
            "sofia@gmail.com", 
            "1234", 
            Arrays.asList("CLIENTE-C1", "CLIENTE-C2"), 
            Arrays.asList(
                new Direccion(5, "Lautaro 123 casa 2", "Chile", "Osorno", "1234"),
                new Direccion(6, "Las rejas 2", "Chile", "Valdivia", "1234"),
                new Direccion(7, "San agustinas 1234", "Chile", "Osorno", "1234")
            )

        ));
        usuarios.add(new Usuario(
            5, 
            "matias", 
            "matias@gmail.com", 
            "1234", 
            Arrays.asList("ADMIN"), 
            Arrays.asList(
                new Direccion(8, "Ongolmo 1235", "Chile", "Concepción", "1234")
            )
                
        ));
        
    }


    
    @GetMapping("")
    public List<Usuario> listarUsuarios() {
        return this.usuarios;
    }


    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable int id) {
        Usuario usuario = this.usuarios.stream()
            .filter(u -> u.getUsuarioId() == id)
            .findFirst()
            .orElse(null);

        if(usuario == null ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
   
        return usuario;
    }
    
    
}
