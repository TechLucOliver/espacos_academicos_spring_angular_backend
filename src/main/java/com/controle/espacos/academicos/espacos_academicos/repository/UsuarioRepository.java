package com.controle.espacos.academicos.espacos_academicos.repository;

import java.util.List;
import java.util.Optional;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import org.springframework.data.jpa.repository.JpaRepository;

import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByMatricula(String matricula);

    List<Usuario> findByStatus(StatusCadastro status);

    List<Usuario> findByCentroAcademicoId(Long centroAcademicoId);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByMatricula(String matricula);

    boolean existsByMatriculaAndIdNot(String matricula, Long id);

}
