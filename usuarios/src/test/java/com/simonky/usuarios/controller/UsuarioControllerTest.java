package com.simonky.usuarios.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonky.usuarios.exceptions.UsuarioNotFoundException;
import com.simonky.usuarios.model.Usuario;
import com.simonky.usuarios.service.UsuarioServiceImpl;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioServiceImpl usuarioServiceMock;

    @Test
    public void listarUsuariosTest() throws Exception {
        Usuario usua1 = new Usuario();
        usua1.setEmail("email@gmail.com");
        usua1.setPassword("password123#");
        usua1.setNombre("Simon");

        Usuario usua2 = new Usuario();
        usua2.setEmail("email2@gmail.com");
        usua2.setPassword("password123#");
        usua2.setNombre("Simon2");


        List<Usuario> pelis = Arrays.asList(usua1, usua2);
        when(usuarioServiceMock.getUsuarios()).thenReturn(pelis);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList[0].email").value("email@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList[0].nombre").value("Simon"))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList[0].password").value("password123#"))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList[1].email").value("email2@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList[1].nombre").value("Simon2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList[1].password").value("password123#"))
            ;
    }

    @Test
    public void obtenerUsuarioNoExistenteTest() throws Exception {
        Long idUsuario = -1L; 
        when(usuarioServiceMock.getUsuarioById(idUsuario)).thenThrow(new UsuarioNotFoundException(idUsuario));

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/{id}", idUsuario))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsuarioNotFoundException));

        verify(usuarioServiceMock, times(1)).getUsuarioById(idUsuario);
    }

    @Test
    public void obtenerUsuarioExistenteTest() throws Exception {
        Long idUsuarioExistente = 1L;

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(idUsuarioExistente);
        usuarioExistente.setEmail("email@gmail.com");
        usuarioExistente.setPassword("password123#");
        usuarioExistente.setNombre("Simon");

        when(usuarioServiceMock.getUsuarioById(idUsuarioExistente)).thenReturn(usuarioExistente);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/{id}", idUsuarioExistente))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idUsuarioExistente.intValue()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Simon"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("password123#"));

        verify(usuarioServiceMock, times(1)).getUsuarioById(idUsuarioExistente);
    }


    @Test
    public void eliminarUsuarioTest() throws Exception {
        Long idUsuario = 1L;

        doNothing().when(usuarioServiceMock).deleteUsuario(idUsuario);

        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/{id}", idUsuario))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(usuarioServiceMock, times(1)).deleteUsuario(idUsuario);

    }


    @Test
    public void crearUsuarioTest() throws Exception {
        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setEmail("email@gmail.com");
        usuarioNuevo.setPassword("password123#");
        usuarioNuevo.setNombre("Simon");

        when(usuarioServiceMock.createUsuario(any(Usuario.class))).thenReturn(usuarioNuevo);

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(usuarioNuevo)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@gmail.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Simon"));

        
        verify(usuarioServiceMock, times(1)).createUsuario(any(Usuario.class));
    }

    
}
