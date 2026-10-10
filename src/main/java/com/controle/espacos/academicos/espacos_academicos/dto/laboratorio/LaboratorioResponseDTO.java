package com.controle.espacos.academicos.espacos_academicos.dto.laboratorio;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import com.controle.espacos.academicos.espacos_academicos.model.Laboratorio;

import java.time.LocalDate;

public record LaboratorioResponseDTO(
        Long id,
        String sigla,
        String nome,
        TipoEspaco tipo,
        String descricaoComplementar,
        int capacidadeMaxima,
        String localizacao,
        LocalDate dataCadastro,
        StatusEspaco status,
        String tipoComputadores,
        Long centroAcademicoId,
        String centroAcademicoNome
) {
    public static LaboratorioResponseDTO fromEntity(Laboratorio lab) {
        return new LaboratorioResponseDTO(
                lab.getId(),
                lab.getSigla(),
                lab.getNome(),
                lab.getTipo(),
                lab.getDescricaoComplementar(),
                lab.getCapacidadeMaxima(),
                lab.getLocalizacao(),
                lab.getDataCadastro(),
                lab.getStatus(),
                lab.getTipoComputadores(),
                lab.getCentroAcademico().getId(),
                lab.getCentroAcademico().getNome()
        );
    }
}
