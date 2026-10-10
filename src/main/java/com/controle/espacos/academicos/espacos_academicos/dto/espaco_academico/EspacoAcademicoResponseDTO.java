package com.controle.espacos.academicos.espacos_academicos.dto.espaco_academico;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import com.controle.espacos.academicos.espacos_academicos.model.EspacoAcademico;

import java.time.LocalDate;

/**
 * Payload de resposta contendo os dados consolidados de um Espaço Acadêmico.
 *
 * @param id                    Identificador primário do espaço.
 * @param sigla                 Sigla ou código da sala/espaço (ex.: LAB-01, AUD-B).
 * @param nome                  Nome por extenso do espaço.
 * @param tipo                  Classificação da estrutura (SALA, LABORATORIO, AUDITORIO).
 * @param descricaoComplementar Observações sobre recursos do espaço (projetor, ar-condicionado).
 * @param capacidadeMaxima      Lotação máxima permitida no espaço em número de pessoas.
 * @param localizacao           Bloco, prédio ou andar da localização física.
 * @param dataCadastro          Data de cadastramento do espaço.
 * @param status                Estado de disponibilidade operacional do espaço.
 * @param centroAcademicoId     ID do Centro Acadêmico responsável pelo espaço.
 * @param centroAcademicoNome   Nome do Centro Acadêmico responsável.
 *
 */
public record EspacoAcademicoResponseDTO(
        Long id,
        String sigla,
        String nome,
        TipoEspaco tipo,
        String descricaoComplementar,
        int capacidadeMaxima,
        String localizacao,
        LocalDate dataCadastro,
        StatusEspaco status,
        Long centroAcademicoId,
        String centroAcademicoNome
) {
    public static EspacoAcademicoResponseDTO fromEntity(EspacoAcademico espaco){
        return new EspacoAcademicoResponseDTO(
                espaco.getId(),
                espaco.getSigla(),
                espaco.getNome(),
                espaco.getTipo(),
                espaco.getDescricaoComplementar(),
                espaco.getCapacidadeMaxima(),
                espaco.getLocalizacao(),
                espaco.getDataCadastro(),
                espaco.getStatus(),
                espaco.getCentroAcademico().getId(),
                espaco.getCentroAcademico().getNome()
        );
    }
}
