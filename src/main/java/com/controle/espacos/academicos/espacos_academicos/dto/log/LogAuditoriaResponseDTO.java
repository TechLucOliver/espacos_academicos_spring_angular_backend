package com.controle.espacos.academicos.espacos_academicos.dto.log;

import com.controle.espacos.academicos.espacos_academicos.model.log.LogAuditoria;

import java.time.LocalDateTime;

/**
 * Payload de resposta contendo os dados de um registro de auditoria.
 *
 * @param id          Identificador único do log.
 * @param dataHora    Data e hora em que a ação ocorreu.
 * @param acao        Operação executada (ex.: INATIVACAO_USUARIO).
 * @param entidade    Nome da entidade afetada (ex.: Usuario).
 * @param detalhes    Descrição detalhada ou resumo do evento.
 * @param usuarioId   ID do usuário que executou ou sofreu a ação.
 * @param usuarioNome Nome do usuário para exibição direta em tela.
 */

public record LogAuditoriaResponseDTO(
        Long id,
        LocalDateTime dataHora,
        String acao,
        String entidade,
        String detalhes,
        Long usuarioId,
        String usuarioNome
) {
    public static LogAuditoriaResponseDTO fromEntity(LogAuditoria log){
        return new LogAuditoriaResponseDTO(
                log.getId(),
                log.getDataHora(),
                log.getAcao(),
                log.getEntidade(),
                log.getDetalhes(),
                log.getUsuario() !=null ? log.getUsuario().getId(): null,
                log.getUsuario() != null ? log.getUsuario().getNomeCompleto() : null
        );

    }
}
