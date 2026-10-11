package com.controle.espacos.academicos.espacos_academicos.dto.usuario;

import com.controle.espacos.academicos.espacos_academicos.enums.PerfilAcesso;
import jakarta.validation.constraints.*;

import java.util.Set;

public record UsuarioRequestDTO(
        @NotBlank(message = "A matrícula é obrigatória.")
        @Size(max = 30, message = "A matrícula não pode exceder 30 caracteres.")
        String matricula,

        @NotBlank(message = "O nome completo é obrigatório.")
        @Size(max = 150, message = "O nome completo não pode exceder 150 caracteres.")
        String nomeCompleto,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail informado é inválido.")
        @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
        String email,

        @NotBlank(message = "O telefone é obrigatório.")
        @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
        String telefone,

        @NotBlank(message = "A senha inicial é obrigatória.")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres.")
        String senha,

        @NotNull(message = "O Centro Acadêmico vinculado é obrigatório.")
        Long centroAcademicoId,

        @NotEmpty(message = "O usuário deve possuir ao menos um perfil de acesso.")
        Set<PerfilAcesso> perfis
) {
}
