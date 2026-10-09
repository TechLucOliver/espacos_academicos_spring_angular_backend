package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de acesso a dados e persistência para a entidade {@link Curso}.
 * <p>
 * Provê consultas derivadas para localização de cursos por vínculo hierárquico
 * com Centros Acadêmicos, status cadastral e validações de unicidade de sigla.
 * </p>
 *
 */
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Localiza todos os cursos vinculados a um determinado Centro Acadêmico.
     *
     * @param centroAcademicoId Identificador primário do Centro Acadêmico mantenedor.
     * @return Lista contendo os cursos associados à unidade informada.
     */
    List<Curso> findByCentroAcademicoId(Long centroAcademicoId);

    /**
     * Recupera todos os cursos filtrados pelo status cadastral.
     *
     * @param status Situação de cadastramento desejada (ex.: ATIVO).
     * @return Lista de cursos com o status informado.
     */
    List<Curso> findByStatus(StatusCadastro status);

    /**
     * Verifica se já existe um curso com a mesma sigla no mesmo Centro Acadêmico.
     *
     * @param sigla             Sigla do curso a verificar.
     * @param centroAcademicoId Identificador do Centro Acadêmico de vínculo.
     * @return {@code true} se já houver duplicidade na mesma unidade; senão {@code false}.
     */
    boolean existsBySiglaIgnoreCaseAndCentroAcademicoId(String sigla, Long centroAcademicoId);

    /**
     * Verifica se existe outro curso utilizando a mesma sigla na mesma unidade acadêmica,
     * desconsiderando o ID do próprio curso sob edição.
     *
     * @param sigla             Sigla do curso a verificar.
     * @param centroAcademicoId Identificador do Centro Acadêmico de vínculo.
     * @param id                Identificador do curso em edição a ser desconsiderado.
     * @return {@code true} se outro registro utilizar a mesma sigla; senão {@code false}.
     */
    boolean existsBySiglaIgnoreCaseAndCentroAcademicoIdAndIdNot(String sigla, Long centroAcademicoId, Long id);
}
