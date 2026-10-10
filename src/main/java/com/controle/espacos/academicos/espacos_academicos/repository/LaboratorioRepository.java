package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.model.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de acesso a dados especializada para a subclasse {@link Laboratorio}.
 * <p>
 * Herda as consultas da hierarquia e provê operações direcionadas exclusivamente
 * aos laboratórios de informática e práticos.
 * </p>
 *
 */
@Repository
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

    /**
     * Recupera todos os laboratórios vinculados a uma determinada unidade acadêmica.
     *
     * @param centroAcademicoId Identificador primário do Centro Acadêmico.
     * @return Lista de laboratórios pertencentes à unidade informada.
     */
    List<Laboratorio> findByCentroAcademicoId(Long centroAcademicoId);
}
