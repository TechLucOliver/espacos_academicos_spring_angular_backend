package com.controle.espacos.academicos.espacos_academicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controle.espacos.academicos.espacos_academicos.model.Usuario;

public interface UsuarioReposity extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

}
