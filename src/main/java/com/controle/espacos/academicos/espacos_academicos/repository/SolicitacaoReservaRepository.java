package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.model.solicitacoes.SolicitacaoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SolicitacaoReservaRepository extends JpaRepository<SolicitacaoReserva, Long> {
    /**
     * Lista solicitações feitas por um determinado docente/usuário.
     */
    List<SolicitacaoReserva>findBySolicitanteId(Long solicitanteId);

    /**
     * Lista solicitações por status (ex: PENDENTE).
     */
    List<SolicitacaoReserva> findByStatus(StatusSolicitacao status);

    /**
      Verifica se existe alguma reserva APROVADA que conflite com o dia e horário desejados.
     */

    @Query("""
        SELECT COUNT(s) > 0 FROM SolicitacaoReserva s
        WHERE s.espaco.id = :espacoId
          AND s.dataUtilizacao = :data
          AND s.status = 'APROVADA'
          AND :horaInicio < s.horaFim
          AND :horaFim > s.horaInicio
    """)
    boolean existeConflitoHorario(
            @Param("espacoId") Long espacoId,
            @Param("data") LocalDate data,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim
    );


}
