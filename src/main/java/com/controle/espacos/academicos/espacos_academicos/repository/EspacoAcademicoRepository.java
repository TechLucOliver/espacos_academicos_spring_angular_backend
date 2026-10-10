package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import com.controle.espacos.academicos.espacos_academicos.model.EspacoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de persistência de dados para a entidade base {@link EspacoAcademico}.
 * <p>
 * Fornece métodos para consultas polimórficas de espaços acadêmicos, filtrando
 * por centro acadêmico de vínculo, tipo, estado operacional e status cadastral.
 * </p>
 *
 */

@Repository
public interface EspacoAcademicoRepository extends JpaRepository<EspacoAcademico, Long> {

    /**
     * Localiza todos os espaços acadêmicos vinculados a um determinado Centro Acadêmico.
     *
     * @param centroAcademicoId Identificador primário do Centro Acadêmico mantenedor.
     * @return Lista de espaços vinculados à unidade informada.
     */
    List<EspacoAcademico> findByCentroAcademicoId(Long centroAcademicoId);

    /**
     * Filtra espaços acadêmicos por tipo estrutural (SALA_AULA, LABORATORIO, AUDITORIO).
     *
     * @param tipoEspaco Categoria do espaço físico.
     * @return Lista de espaços da categoria informada.
     */
    List<EspacoAcademico> findByTipo(TipoEspaco tipoEspaco);

    /**
     * Recupera espaços pelo status cadastral de ciclo de vida.
     *
     * @param statusCadastro Situação cadastral (ATIVO ou INATIVO).
     * @return Lista de espaços cadastrados no status indicado.
     */
    List<EspacoAcademico> findByStatus(StatusCadastro statusCadastro);

    boolean existsBySiglaIgnoreCaseAndCentroAcademicoId(String sigla, Long centroAcademicoId);

    boolean existsBySiglaIgnoreCaseAndCentroAcademicoIdAndIdNot(String sigla, Long centroAcademicoId, Long id);

    /**
     * Verifica se já existe um espaço com a mesma identificação/nome no mesmo Centro Acadêmico.
     *
     * @param nome              Nome ou código da sala (ex.: "Sala 204 - Bloco B").
     * @param centroAcademicoId Identificador do Centro Acadêmico.
     * @return {@code true} se já houver duplicidade na mesma unidade; senão {@code false}.
     */
    boolean existsByNomeIgnoreCaseAndCentroAcademicoId(String nome, Long centroAcademicoId);

    /**
     * Valida duplicidade de identificação ignorando o próprio espaço em edição.
     *
     * @param nome              Nome ou código do espaço.
     * @param centroAcademicoId Identificador do Centro Acadêmico.
     * @param id                Identificador do espaço sob edição a ser desconsiderado.
     * @return {@code true} se outro registro utilizar o mesmo nome; senão {@code false}.
     */
    boolean existsByNomeIgnoreCaseAndCentroAcademicoIdAndIdNot(String nome, Long centroAcademicoId, Long id);
}
