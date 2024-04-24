package com.simonky.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simonky.usuarios.exceptions.UsuarioNotFoundException;
import com.simonky.usuarios.model.Usuario;
import com.simonky.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepositoryTest;

    @Test
    public void guardarUsuarioTest(){
        Usuario usuario = new Usuario();
       
        usuario.setEmail("email@gmail.com");
        usuario.setPassword("password123#");
        usuario.setNombre("Simon");

        when(usuarioRepositoryTest.save(any())).thenReturn(usuario);

        Usuario res = usuarioService.createUsuario(usuario);

        assertEquals("email@gmail.com", res.getEmail());
        assertEquals("Simon", res.getNombre());
        assertEquals("password123#", res.getPassword());
        assertEquals(new ArrayList<>(), res.getRoles());
        assertEquals(new ArrayList<>(), res.getDirecciones());
    }


    @Test
    public void eliminarUsuarioExistenteTest() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
       
        usuario.setEmail("email@gmail.com");
        usuario.setPassword("password123#");
        usuario.setNombre("Simon");

        when(usuarioRepositoryTest.findById(usuarioId)).thenReturn(Optional.of(usuario));

        usuarioService.deleteUsuario(usuarioId);

        verify(usuarioRepositoryTest, times(1)).deleteById(usuarioId);
    }

    @Test
    public void eliminarUsuarioNoExistenteTest() {
        
        Long idUsuarioNoExistente = 999L;

        when(usuarioRepositoryTest.findById(idUsuarioNoExistente)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.deleteUsuario(idUsuarioNoExistente));

        verify(usuarioRepositoryTest, never()).deleteById(idUsuarioNoExistente);
    }
}
