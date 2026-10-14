package com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_reserva;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.model.solicitacoes.SolicitacaoReserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Payload de resposta contendo os dados consolidados de uma solicitação de reserva.
 */
public record SolicitacaoReservaResponseDTO(
        Long id,
        LocalDate dataUtilizacao,
        LocalTime horaInicio,
        LocalTime horaFim,
        LocalDateTime dataSolicitacao,
        StatusSolicitacao status,
        String justificativaReprovacao,
        Long espacoId,
        String espacoNome,
        String espacoSigla,
        Long solicitanteId,
        String solicitanteNome,
        Long avaliadorId,
        String avaliadorNome
) {
    public static SolicitacaoReservaResponseDTO fromEntity(SolicitacaoReserva solicitacao) {
        return new SolicitacaoReservaResponseDTO(
                solicitacao.getId(),
                solicitacao.getDataUtilizacao(),
                solicitacao.getHoraInicio(),
                solicitacao.getHoraFim(),
                solicitacao.getDataSolicitacao(),
                solicitacao.getStatus(),
                solicitacao.getJustificativaReprovacao(),
                solicitacao.getEspaco() != null ? solicitacao.getEspaco().getId() : null,
                solicitacao.getEspaco() != null ? solicitacao.getEspaco().getNome() : null,
                solicitacao.getEspaco() != null ? solicitacao.getEspaco().getSigla() : null,
                solicitacao.getSolicitante() != null ? solicitacao.getSolicitante().getId() : null,
                solicitacao.getSolicitante() != null ? solicitacao.getSolicitante().getNomeCompleto() : null,
                solicitacao.getAvaliador() != null ? solicitacao.getAvaliador().getId() : null,
                solicitacao.getAvaliador() != null ? solicitacao.getAvaliador().getNomeCompleto() : null
        );
    }
}
