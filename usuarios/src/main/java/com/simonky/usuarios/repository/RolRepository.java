package com.simonky.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simonky.usuarios.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    
}
