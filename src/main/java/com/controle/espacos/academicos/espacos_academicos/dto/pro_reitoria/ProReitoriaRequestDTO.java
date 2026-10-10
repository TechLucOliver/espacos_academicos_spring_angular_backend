package com.controle.espacos.academicos.espacos_academicos.dto.pro_reitoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Objeto de Transferência de Dados (DTO) para requisição de cadastro e edição de Pró-Reitorias.
 * <p>
 * Implementado como um {@code record} imutável com anotações do Bean Validation
 * para validação estrutural dos dados recebidos pelo Front-End.
 * </p>
 *
 * @param nome                Nome oficial do órgão pró-reitorial (obrigatório, até 150 caracteres).
 * @param nomeResponsavel     Nome e titulação do responsável nomeado (obrigatório, até 100 caracteres).
 *
 */
public record ProReitoriaRequestDTO(
        @NotBlank(message = "O nome da pró-reitoria é obrigatório.")
        @Size(max = 150, message = "O nome não pode exceder 150 caracteres.")
        String nome,

        @NotBlank(message = "A nomeação do responsável é obrigatória.")
        @Size(max = 100, message = "O nome do responsável não pode exceder 100 caracteres.")
        String nomeResponsavel
) {
}
