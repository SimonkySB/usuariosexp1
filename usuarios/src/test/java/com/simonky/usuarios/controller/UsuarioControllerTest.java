package com.simonky.usuarios.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    public void eliminarUsuarioTest() throws Exception {
        Long idUsuario = 1L;

        doNothing().when(usuarioServiceMock).deleteUsuario(idUsuario);

        mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/{id}", idUsuario))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(usuarioServiceMock, times(1)).deleteUsuario(idUsuario);

    }
}
