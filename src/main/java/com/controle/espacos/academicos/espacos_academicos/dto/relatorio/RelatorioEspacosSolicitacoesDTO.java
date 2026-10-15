package com.controle.espacos.academicos.espacos_academicos.dto.relatorio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO consolidado contendo os espaços cadastrados e o histórico de solicitações (reservas e softwares).
 */
public record RelatorioEspacosSolicitacoesDTO(
        EmissorRelatorioDTO emissor,
        List<EspacoItemDTO> espacos,
        List<ReservaItemDTO> reservas,
        List<SoftwareItemDTO> solicitacoesSoftwares
) {
    public record EspacoItemDTO(
            Long id,
            String nome,
            String sigla,
            String tipo,
            Integer capacidade,
            String status,
            String centroAcademico
    ) {}

    public record ReservaItemDTO(
            Long id,
            String espacoNome,
            String solicitanteNome,
            LocalDate dataUtilizacao,
            LocalTime horaInicio,
            LocalTime horaFim,
            String status,
            String justificativaReprovacao,
            String avaliadorNome
    ) {}

    public record SoftwareItemDTO(
            Long id,
            String softwareNome,
            String laboratorioNome,
            String solicitanteNome,
            String status,
            String justificativaReprovacao,
            String avaliadorNome
    ) {}
}
