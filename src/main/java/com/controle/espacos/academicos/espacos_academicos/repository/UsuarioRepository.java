package com.controle.espacos.academicos.espacos_academicos.repository;

import java.util.List;
import java.util.Optional;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import org.springframework.data.jpa.repository.JpaRepository;

import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Localiza um usuário pelo seu e-mail institucional ou pelo número de matrícula funcional.
     * <p>
     * Utilizado pelo fluxo de autenticação para permitir acesso com qualquer uma das duas chaves únicas.
     * </p>
     *
     * @param login E-mail ou matrícula informado no formulário de acesso.
     * @return {@link Optional} contendo o {@link Usuario} correspondente, caso exista.
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:login) OR UPPER(u.matricula) = UPPER(:login)")
    Optional<Usuario> findByEmailOrMatricula(@Param("login") String login);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByMatricula(String matricula);

    List<Usuario> findByStatus(StatusCadastro status);

    List<Usuario> findByCentroAcademicoId(Long centroAcademicoId);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByMatricula(String matricula);

    boolean existsByMatriculaAndIdNot(String matricula, Long id);

}
