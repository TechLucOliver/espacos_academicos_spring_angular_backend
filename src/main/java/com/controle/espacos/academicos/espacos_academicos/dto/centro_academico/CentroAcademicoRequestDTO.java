package com.controle.espacos.academicos.espacos_academicos.dto.centro_academico;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Objeto de Transferência de Dados para entrada (payload) no cadastro e edição de Centros Acadêmicos.
 *
 * @param nome            Nome completo do Centro Acadêmico ou Escola (obrigatório, até 150 caracteres).
 * @param emailCentral    E-mail institucional de atendimento central.
 * @param telefoneCentral Telefone de contato do Centro Acadêmico.
 * @param nomeCoordenador Nome do coordenador responsável (RF-008).
 * @param iesId           Identificador da IES à qual o Centro Acadêmico pertence (RF-007).
 *
 */
public record CentroAcademicoRequestDTO(
        @NotBlank(message = "Informar o nome do Centro Academico é obrigatório")
        @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres")
        String nome,

        @NotBlank(message = "Informar o email central é obrigatório")
        @Email(message = "Formato de email inválido")
        String emailCentral,

        @NotBlank(message = "Informar o telefone central é obrigatório")
        @Size(max = 20, message = "O telefone não pode ter mais de 20 caracteres")
        String telefoneCentral,

        @NotBlank(message = "Informar o nome do coordenador é obrigatório")
        @Size(max = 100, message = "O nome do coordenador não pode exceder 100 caracteres")
        String nomeCoordenador,

        @NotNull(message = "O Id da IES vinculada é obrigatório")
        Long iesId
) {
}
