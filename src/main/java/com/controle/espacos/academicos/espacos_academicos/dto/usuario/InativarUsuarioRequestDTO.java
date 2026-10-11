package com.controle.espacos.academicos.espacos_academicos.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InativarUsuarioRequestDTO(

        @NotBlank(message = "A justificativa de inativação é obrigatória.")
        @Size(max = 255, message = "A justificativa não pode exceder 255 caracteres.")
        String motivo
) {
}
