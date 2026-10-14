package com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.model.solicitacoes.SolicitacaoSoftware;

import java.time.LocalDateTime;

/**
 * Payload de resposta contendo os dados consolidados da solicitação de software.
 */
public record SolicitacaoSoftwareResponseDTO(
        Long id,
        LocalDateTime dataHoraSolicitacao,
        StatusSolicitacao status,
        String justificativaReprovacao,
        Long laboratorioId,
        String laboratorioNome,
        String laboratorioSigla,
        Long softwareId,
        String softwareNome,
        Long solicitanteId,
        String solicitanteNome,
        Long avaliadorId,
        String avaliadorNome
) {
    public static SolicitacaoSoftwareResponseDTO fromEntity(SolicitacaoSoftware solicitacao) {
        return new SolicitacaoSoftwareResponseDTO(
                solicitacao.getId(),
                solicitacao.getDataHoraSolicitacao(),
                solicitacao.getStatus(),
                solicitacao.getJustificativaReprovacao(),
                solicitacao.getLaboratorio() != null ? solicitacao.getLaboratorio().getId() : null,
                solicitacao.getLaboratorio() != null ? solicitacao.getLaboratorio().getNome() : null,
                solicitacao.getLaboratorio() != null ? solicitacao.getLaboratorio().getSigla() : null,
                solicitacao.getSoftware() != null ? solicitacao.getSoftware().getId() : null,
                solicitacao.getSoftware() != null ? solicitacao.getSoftware().getNome() : null,
                solicitacao.getSolicitante() != null ? solicitacao.getSolicitante().getId() : null,
                solicitacao.getSolicitante() != null ? solicitacao.getSolicitante().getNomeCompleto() : null,
                solicitacao.getAvaliador() != null ? solicitacao.getAvaliador().getId() : null,
                solicitacao.getAvaliador() != null ? solicitacao.getAvaliador().getNomeCompleto() : null
        );
    }
}
