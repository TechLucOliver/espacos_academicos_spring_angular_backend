package com.controle.espacos.academicos.espacos_academicos.repository;

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
     * Localiza todos os espaços académicos adstritos a um determinado Centro Académico.
     *
     * @param centroAcademicoId Identificador primário do Centro Académico mantenedor.
     * @return Lista de espaços vinculados à referida unidade.
     */
    List<EspacoAcademico> findByCentroAcademicoId(Long centroAcademicoId);

    /**
     * Filtra os espaços académicos de acordo com a sua categoria estrutural (SALA, LABORATORIO, AUDITORIO).
     *
     * @param tipo Categoria física do espaço.
     * @return Lista de espaços correspondentes à classificação pretendida.
     */
    List<EspacoAcademico> findByTipo(TipoEspaco tipo);

    /**
     * Recupera espaços de acordo com o seu estado de disponibilidade física e operacional.
     *
     * @param status Estado operacional (DISPONIVEL, INATIVO, INDISPONIVEL).
     * @return Lista de espaços com o estado especificado.
     */
    List<EspacoAcademico> findByStatus(StatusEspaco status);

    /**
     * Verifica se já existe um espaço com a mesma sigla no mesmo Centro Académico.
     *
     * @param sigla             Sigla ou código identificador do espaço.
     * @param centroAcademicoId Identificador primário do Centro Académico.
     * @return {@code true} caso exista duplicidade; {@code false} em caso contrário.
     */
    boolean existsBySiglaIgnoreCaseAndCentroAcademicoId(String sigla, Long centroAcademicoId);

    /**
     * Valida a duplicidade de sigla desconsiderando o próprio espaço em edição.
     *
     * @param sigla             Sigla ou código sob avaliação.
     * @param centroAcademicoId Identificador primário do Centro Académico.
     * @param id                Identificador do registo em edição a ignorar.
     * @return {@code true} se outro espaço já utilizar esta sigla; caso contrário {@code false}.
     */
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
