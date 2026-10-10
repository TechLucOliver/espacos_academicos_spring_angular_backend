package com.controle.espacos.academicos.espacos_academicos.dto.pro_reitoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProReitoriaRequestDTO(
        @NotBlank(message = "O nome da pró-reitoria é obrigatório.")
        @Size(max = 150, message = "O nome não pode exceder 150 caracteres.")
        String nome,

        @NotBlank(message = "A nomeação do responsável é obrigatória.")
        @Size(max = 100, message = "O nome do responsável não pode exceder 100 caracteres.")
        String nomeacaoResponsavel
) {
}
