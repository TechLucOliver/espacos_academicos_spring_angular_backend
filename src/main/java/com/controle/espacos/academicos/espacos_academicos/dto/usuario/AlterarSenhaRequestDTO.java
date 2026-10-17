package com.controle.espacos.academicos.espacos_academicos.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlterarSenhaRequestDTO(
        @NotBlank(message = "A palavra-passe atual é obrigatória.")
        String senhaAtual,

        @NotBlank(message = "A nova palavra-passe é obrigatória.")
        @Size(min = 6, message = "A nova palavra-passe deve conter pelo menos 6 caracteres.")
        String novaSenha
) {}