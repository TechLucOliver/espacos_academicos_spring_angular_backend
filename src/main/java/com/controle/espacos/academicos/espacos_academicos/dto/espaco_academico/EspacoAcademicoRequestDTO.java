package com.controle.espacos.academicos.espacos_academicos.dto.espaco_academico;

import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de entrada para cadastro e atualização integral de Espaços Acadêmicos genéricos (salas, auditórios).
 *
 * @param nome              Identificação ou nome da sala/espaço (ex.: "Sala 101 - Pavilhão I").
 * @param capacidadeMaxima  Lotação máxima de assentos/alunos (mínimo de 1 pessoa).
 * @param localizacao       Descrição física do pavilhão, andar ou bloco.
 * @param tipo              Categoria da sala (ex.: SALA_AULA, AUDITORIO).
 * @param centroAcademicoId Identificador do Centro Acadêmico responsável pelo espaço.
 *
 */
public record EspacoAcademicoRequestDTO(
        @NotBlank(message = "A sigla ou código do espaço é obrigatória.")
        @Size(max = 20, message = "A sigla não pode exceder 20 caracteres.")
        String sigla,

        @NotBlank(message = "O nome do espaço é obrigatório.")
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
        String nome,

        @NotNull(message = "O tipo do espaço acadêmico é obrigatório.")
        TipoEspaco tipo,

        @Size(max = 255, message = "A descrição complementar não pode exceder 255 caracteres.")
        String descricaoComplementar,

        @NotNull(message = "A capacidade máxima é obrigatória.")
        @Min(value = 1, message = "A capacidade deve ser de no mínimo 1 pessoa.")
        Integer capacidadeMaxima,

        @NotBlank(message = "A localização do espaço é obrigatória.")
        @Size(max = 150, message = "A localização não pode exceder 150 caracteres.")
        String localizacao,

        @NotNull(message = "O identificador do Centro Acadêmico é obrigatório.")
        Long centroAcademicoId
) {
}
