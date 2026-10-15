package com.controle.espacos.academicos.espacos_academicos.dto.relatorio;

import java.util.List;

/**
 * DTO para o relatório de docentes vinculados às suas respectivas unidades/Centros Acadêmicos.
 */
public record RelatorioDocenteDTO(
        EmissorRelatorioDTO emissor,
        List<DocenteItemDTO> docentes
) {
    public record DocenteItemDTO(
        Long id,
        String matricula,
        String nomeCompleto,
        String email,
        String telefone,
        String centroAcademico,
        String status,
        List<String> perfis
    ){}
}
