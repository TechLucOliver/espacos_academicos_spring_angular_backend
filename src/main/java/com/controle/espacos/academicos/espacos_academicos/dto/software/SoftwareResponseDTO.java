package com.controle.espacos.academicos.espacos_academicos.dto.software;

import com.controle.espacos.academicos.espacos_academicos.enums.TipoSoftware;
import com.controle.espacos.academicos.espacos_academicos.model.Software;

/**
 * Payload de resposta contendo os dados consolidados de um Software cadastrado.
 *
 * @param id                    Identificador único do software.
 * @param nome                  Nome comercial do programa.
 * @param urlDownload           Endereço para download do instalador.
 * @param tipo                  Tipo de licença (PROPRIETARIO ou SOFTWARE_LIVRE).
 * @param objetivoUso           Propósito acadêmico da ferramenta.
 * @param disciplinasVinculadas Disciplinas que utilizam a ferramenta.
 *
 */
public record SoftwareResponseDTO(
        Long id,
        String nome,
        String urlDownload,
        TipoSoftware tipo,
        String objetivoUso,
        String disciplinasVinculadas
) {

    /**
     * Mapeia a entidade {@link Software} para o respectivo DTO imutável de saída.
     *
     * @param software Entidade de domínio gerenciada.
     * @return DTO com os atributos serializáveis.
     */
    public static SoftwareResponseDTO fromEntity(Software software) {
        return new SoftwareResponseDTO(
                software.getId(),
                software.getNome(),
                software.getUrlDownload(),
                software.getTipo(),
                software.getObjetivoUso(),
                software.getDisciplinasVinculadas()
        );
    }
}