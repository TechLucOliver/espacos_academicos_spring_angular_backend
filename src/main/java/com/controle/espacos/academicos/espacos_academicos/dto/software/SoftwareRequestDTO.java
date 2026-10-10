package com.controle.espacos.academicos.espacos_academicos.dto.software;

import com.controle.espacos.academicos.espacos_academicos.enums.TipoSoftware;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de entrada para cadastro e atualização cadastral de softwares acadêmicos.
 *
 * @param nome                  Nome comercial do software ou pacote.
 * @param urlDownload           URL ou repositório oficial para download do instalador.
 * @param tipo                  Classificação da licença (PROPRIETARIO ou SOFTWARE_LIVRE).
 * @param objetivoUso           Finalidade pedagógica ou técnica do programa.
 * @param disciplinasVinculadas Componentes curriculares que demandam a ferramenta (opcional).
 *
 */
public record SoftwareRequestDTO(
        @NotBlank(message = "O nome do software é obrigatório.")
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
        String nome,

        @NotBlank(message = "A URL de download é obrigatória.")
        @Size(max = 255, message = "A URL de download não pode exceder 255 caracteres.")
        String urlDownload,

        @NotNull(message = "O tipo de licenciamento é obrigatório.")
        TipoSoftware tipo,

        @NotBlank(message = "O objetivo de uso é obrigatório.")
        @Size(max = 255, message = "O objetivo de uso não pode exceder 255 caracteres.")
        String objetivoUso,

        @Size(max = 255, message = "As disciplinas vinculadas não podem exceder 255 caracteres.")
        String disciplinasVinculadas
) {
}
