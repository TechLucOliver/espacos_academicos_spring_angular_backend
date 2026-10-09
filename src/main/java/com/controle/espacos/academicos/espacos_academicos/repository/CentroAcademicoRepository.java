package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.CentroAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface de acesso a dados para a entidade {@link CentroAcademico}.
 * <p>
 * Disponibiliza operações de persistência e consultas derivadas baseadas
 * no vínculo com a IES e no estado de cadastramento.
 * </p>
 */
public interface CentroAcademicoRepository extends JpaRepository<CentroAcademico, Long> {

    /**
     * Recupera todos os Centros Acadêmicos vinculados a uma determinada IES.
     *
     * @param iesId Identificador primário da Instituição de Ensino Superior.
     * @return Lista de Centros Acadêmicos pertencentes à instituição informada.
     */
    List<CentroAcademico> findByInstituicaoId(Long iesId);

    /**
     * Recupera Centros Acadêmicos filtrados pelo status cadastral.
     *
     * @param status Situação cadastral desejada (ex.: ATIVO).
     * @return Lista de Centros Acadêmicos com o status informado.
     */
    List<CentroAcademico> findByStatus(StatusCadastro status);

    /**
     * Verifica se já existe um Centro Acadêmico com o mesmo nome dentro da mesma IES.
     *
     * @param nome  Nome do Centro Acadêmico.
     * @param iesId Identificador primário da IES.
     * @return {@code true} se já houver duplicidade na mesma instituição, senão {@code false}.
     */
    boolean existsByNomeIgnoreCaseAndInstituicaoId(String nome, Long iesId);

    /**
     * Verifica se já existe outro Centro Acadêmico com o mesmo nome na mesma IES,
     * ignorando o registro atual em edição.
     *
     * @param nome          Nome do Centro Acadêmico.
     * @param instituicaoId Identificador da IES vinculada.
     * @param id            Identificador primário do CA em edição.
     * @return {@code true} se outro CA na mesma IES já usar esse nome; {@code false} caso contrário.
     */
    boolean existsByNomeIgnoreCaseAndInstituicaoIdAndIdNot(String nome, Long instituicaoId, Long id);
}
