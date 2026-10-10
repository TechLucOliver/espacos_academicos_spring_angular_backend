package com.controle.espacos.academicos.espacos_academicos.dto.laboratorio;

import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LaboratorioRequestDTO(
        @NotBlank(message = "A sigla do laboratório é obrigatória.")
        @Size(max = 20, message = "A sigla não pode exceder 20 caracteres.")
        String sigla,

        @NotBlank(message = "O nome do laboratório é obrigatório.")
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
        String nome,

        @Size(max = 255, message = "A descrição complementar não pode exceder 255 caracteres.")
        String descricaoComplementar,

        @NotNull(message = "A capacidade máxima é obrigatória.")
        @Min(value = 1, message = "A capacidade deve ser de no mínimo 1 pessoa.")
        Integer capacidadeMaxima,

        @NotBlank(message = "A localização do laboratório é obrigatória.")
        @Size(max = 150, message = "A localização não pode exceder 150 caracteres.")
        String localizacao,

        @NotBlank(message = "O tipo ou especificação dos computadores é obrigatório.")
        @Size(max = 150, message = "A descrição dos computadores não pode exceder 150 caracteres.")
        String tipoComputadores,

        @NotNull(message = "O identificador do Centro Acadêmico é obrigatório.")
        Long centroAcademicoId
) {
}
