package com.controle.espacos.academicos.espacos_academicos.dto;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.CentroAcademico;

import java.time.LocalDate;

/**
 * Objeto de Transferência de Dados para resposta serializada em JSON contendo as informações do Centro Acadêmico.
 *
 * @param id                Identificador único do Centro Acadêmico.
 * @param nome              Nome por extenso do centro.
 * @param emailCentral      E-mail principal do centro.
 * @param telefoneCentral   Telefone central de atendimento.
 * @param nomeCoordenador   Nome do coordenador atual.
 * @param dataCadastramento Data em que o registro foi realizado.
 * @param status            Situação cadastral (ATIVO ou INATIVO).
 * @param iesId             ID da instituição mantenedora.
 * @param iesSigla          Sigla da instituição mantenedora para exibição direta em tela.
 *
 */
public record CentroAcademicoResponseDTO(
        Long id,
        String nome,
        String emailCentral,
        String telefoneCentral,
        String nomeCoordenador,
        LocalDate dataCadastramento,
        StatusCadastro status,
        Long iesId,
        String iesSigla
) {

    /**
     * Mapeia uma entidade {@link CentroAcademico} persistida para o formato de DTO de resposta.
     *
     * @param centro Entidade gerenciada vinda do banco de dados.
     * @return DTO preenchido com dados do centro e dados essenciais da IES associada.
     */
    public static CentroAcademicoResponseDTO fromEntity(CentroAcademico centro) {
        return new CentroAcademicoResponseDTO(
                centro.getId(),
                centro.getNome(),
                centro.getEmailCentral(),
                centro.getTelefoneCentral(),
                centro.getNomeCoordenador(),
                centro.getDataCadastramento(),
                centro.getStatus(),
                centro.getInstituicao().getId(),
                centro.getInstituicao().getSigla()
        );
    }
}