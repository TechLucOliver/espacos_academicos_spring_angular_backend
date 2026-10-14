package com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Payload de entrada para submissão de um pedido de instalação de software em laboratório.
 */
public record SolicitacaoSoftwareRequestDTO(
        @NotNull(message = "O ID do laboratório é obrigatório.")
        Long laboratorioId,

        @NotNull(message = "O ID do software é obrigatório.")
        Long softwareId,

        @NotNull(message = "O ID do solicitante é obrigatório.")
        Long solicitanteId,

        @NotBlank(message = "A justificativa de uso é obrigatória.")
        @Size(max = 255, message = "A justificativa não pode exceder 255 caracteres.")
        String justificativaUso,

        @Size(max = 100, message = "O nome da disciplina não pode exceder 100 caracteres.")
        String disciplina,

        @FutureOrPresent(message = "A data limite da necessidade deve ser hoje ou uma data futura.")
        LocalDate dataLimiteNecessidade
) {}
