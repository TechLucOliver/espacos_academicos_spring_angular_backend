package com.controle.espacos.academicos.espacos_academicos.dto.pro_reitoria;

import com.controle.espacos.academicos.espacos_academicos.model.ProReitoria;

public record ProReitoriaResponseDTO(
        Long id,
        String nome,
        String nomeacaoResponsavel
) {
    public static ProReitoriaResponseDTO fromEntity(ProReitoria pr) {
        return new ProReitoriaResponseDTO(
                pr.getId(),
                pr.getNome(),
                pr.getNomeResponsavel()
        );
    }
}
