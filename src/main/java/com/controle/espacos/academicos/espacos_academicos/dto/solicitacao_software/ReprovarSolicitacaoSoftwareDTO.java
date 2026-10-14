package com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload para reprovação de solicitações de software com justificativa técnica.
 */
public record ReprovarSolicitacaoSoftwareDTO(
        @NotNull(message = "O ID do avaliador é obrigatório.")
        Long avaliadorId,

        @NotBlank(message = "A justificativa de reprovação é obrigatória.")
        @Size(max = 255, message = "A justificativa não pode exceder 255 caracteres.")
        String motivo
) {}
