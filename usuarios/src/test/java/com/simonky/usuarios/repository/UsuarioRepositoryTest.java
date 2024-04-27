package com.simonky.usuarios.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.simonky.usuarios.model.Usuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {
    

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void guardarUsaurio(){
        Usuario usuario = new Usuario();
       
        usuario.setEmail("email@gmail.com");
        usuario.setPassword("password123#");
        usuario.setNombre("Simon");
        
        Usuario res = usuarioRepository.save(usuario);

        assertNotNull(res.getId());
        assertEquals("email@gmail.com", res.getEmail());
        assertEquals("Simon", res.getNombre());
        assertEquals("password123#", res.getPassword());
        assertEquals(new ArrayList<>(), res.getRoles());
        assertEquals(new ArrayList<>(), res.getDirecciones());
    }

    @Test
    public void eliminarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@gmail.com");
        usuario.setPassword("password123#");
        usuario.setNombre("Simon");

        
        Usuario res = usuarioRepository.save(usuario);

        assertNotNull(res.getId());

        usuarioRepository.deleteById(res.getId());

        Optional<Usuario> deletedData = usuarioRepository.findById(res.getId());
        assertFalse(deletedData.isPresent());
    }
}
