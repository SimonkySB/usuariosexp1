package com.simonky.usuarios.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simonky.usuarios.model.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long>{

    List<Direccion> findByUsuarioId(Long usuarioId);
} 
