package com.controle.espacos.academicos.espacos_academicos.dto.relatorio;

import java.util.List;

/**
 * DTO para o relatório hierárquico da estrutura acadêmica (IES -> Centros Acadêmicos -> Cursos).
 */
public record RelatorioEstruturalDTO(
        EmissorRelatorioDTO emissor,
        List<InstituicaoItemDTO> instituicoes
) {
    public record InstituicaoItemDTO(
        Long id,
        String sigla,
        String nome,
        String reitor,
        String status,
        List<CentroItemDTO> centros
    ){ }

    public record CentroItemDTO(
        Long id,
        String nome,
        String coordenador,
        String status,
        List<CursoItemDTO> cursos
    ){}

    public record CursoItemDTO(
        Long id,
        String sigla,
        String descricao,
        String turno,
        String status
    ){}
}

