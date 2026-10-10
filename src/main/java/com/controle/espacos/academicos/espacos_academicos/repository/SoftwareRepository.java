package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.enums.TipoSoftware;
import com.controle.espacos.academicos.espacos_academicos.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de acesso a dados para a entidade {@link Software}.
 * <p>
 * Fornece métodos de consulta para filtragem por modalidade de licenciamento
 * e validação de unicidade de nomenclatura comercial.
 * </p>
 *
 */
@Repository
public interface SoftwareRepository extends JpaRepository<Software, Long> {

    /**
     * Localiza softwares catalogados por categoria de licenciamento.
     *
     * @param tipo Categoria da licença (PROPRIETARIO ou SOFTWARE_LIVRE).
     * @return Lista de softwares correspondentes à categoria.
     */
    List<Software> findByTipo(TipoSoftware tipo);

    /**
     * Verifica a existência de um software com o mesmo nome na base.
     *
     * @param nome Nome do software a verificar.
     * @return {@code true} se o nome já estiver em uso; caso contrário {@code false}.
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Valida a duplicidade de nome ignorando o próprio registro sob edição.
     *
     * @param nome Nome comercial avaliado.
     * @param id   Identificador do registro em edição a ser desconsiderado.
     * @return {@code true} se outro software já usar o mesmo nome; caso contrário {@code false}.
     */
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}