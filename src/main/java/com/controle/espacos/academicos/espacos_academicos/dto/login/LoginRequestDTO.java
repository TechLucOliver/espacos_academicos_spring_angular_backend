package com.controle.espacos.academicos.espacos_academicos.dto.login;

import jakarta.validation.constraints.NotBlank;

/**
 * Objeto de Transferência de Dados (DTO) para transporte das credenciais de acesso do usuário.
 *
 * @param login Identificador único do usuário (pode ser o e-mail ou a matrícula institucional).
 * @param senha Senha em texto simples submetida pelo cliente para conferência contra o hash.
 */
public record LoginRequestDTO(
        @NotBlank(message = "O login (e-mail ou matrícula) é obrigatório.")
        String login,

        @NotBlank(message = "A senha é obrigatória.")
        String senha
) {
}
