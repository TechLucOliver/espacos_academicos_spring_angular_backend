package com.controle.espacos.academicos.espacos_academicos.dto.pro_reitoria;

import com.controle.espacos.academicos.espacos_academicos.model.ProReitoria;

/**
 * Objeto de Transferência de Dados (DTO) para envio dos dados consolidados de uma Pró-Reitoria.
 * <p>
 * Implementado como um {@code record} imutável pronto para serialização em JSON.
 * </p>
 *
 * @param id                  Identificador primário da pró-reitoria na base de dados.
 * @param nome                Nome do órgão pró-reitorial.
 * @param nomeResponsavel     Nome da autoridade nomeada para a pró-reitoria.
 *
 */
public record ProReitoriaResponseDTO(
        Long id,
        String nome,
        String nomeResponsavel
) {
    /**
     * Converte uma entidade JPA {@link ProReitoria} no respectivo DTO de saída.
     *
     * @param proReitoria Entidade de domínio persistida.
     * @return DTO com os atributos serializáveis da pró-reitoria.
     */
    public static ProReitoriaResponseDTO fromEntity(ProReitoria proReitoria) {
        return new ProReitoriaResponseDTO(
                proReitoria.getId(),
                proReitoria.getNome(),
                proReitoria.getNomeResponsavel()
        );
    }
}
