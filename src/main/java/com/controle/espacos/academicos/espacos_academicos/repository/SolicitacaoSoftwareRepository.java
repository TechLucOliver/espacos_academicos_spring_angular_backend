package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.model.solicitacoes.SolicitacaoSoftware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoSoftwareRepository extends JpaRepository<SolicitacaoSoftware, Long> {

    /**
     * Lista solicitações de software por laboratório de destino.
     */
    List<SolicitacaoSoftware> findByLaboratorioId(Long laboratorioId);

    /**
     * Lista solicitações de software feitas por um determinado docente/solicitante.
     */
    List<SolicitacaoSoftware> findBySolicitanteId(Long solicitanteId);

    /**
     * Lista solicitações de software filtradas pelo status (ex: PENDENTE).
     */
    List<SolicitacaoSoftware> findByStatus(StatusSolicitacao status);
}