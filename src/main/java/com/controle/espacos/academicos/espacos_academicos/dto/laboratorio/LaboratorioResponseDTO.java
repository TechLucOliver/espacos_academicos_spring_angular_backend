package com.controle.espacos.academicos.espacos_academicos.dto.laboratorio;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import com.controle.espacos.academicos.espacos_academicos.model.Laboratorio;

import java.time.LocalDate;

/**
 * Payload de resposta contendo a totalidade dos dados consolidados de um Laboratório.
 *
 * @param id                    Identificador primário único do laboratório.
 * @param sigla                 Sigla ou código interno da sala.
 * @param nome                  Nome por extenso da infraestrutura.
 * @param tipo                  Classificação física do ambiente ({@link TipoEspaco#LABORATORIO}).
 * @param descricaoComplementar Detalhes estruturais e periféricos disponíveis.
 * @param capacidadeMaxima      Lotação máxima de utilizadores em simultâneo.
 * @param localizacao           Endereçamento físico do pavilhão ou andar.
 * @param dataCadastro          Data em que o espaço foi matriculado no sistema.
 * @param status                Estado operacional de uso (DISPONIVEL, MANUTENCAO, etc.).
 * @param tipoComputadores      Especificações técnicas das máquinas instaladas.
 * @param centroAcademicoId     Identificador primário da unidade académica titular.
 * @param centroAcademicoNome   Designação por extenso da unidade académica titular.
 *
 */
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
    /**
     * Converte uma entidade de domínio persistida {@link Laboratorio} no seu correspondente DTO.
     *
     * @param lab Entidade gerenciada recuperada da base de dados.
     * @return Instância imutável de {@link LaboratorioResponseDTO}.
     */
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
