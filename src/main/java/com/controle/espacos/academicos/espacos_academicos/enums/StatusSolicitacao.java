package com.controle.espacos.academicos.espacos_academicos.enums;

/**
 * Representa os estados do fluxo de moderação das solicitações de reserva e de software.
 *
 * <b>Requisitos associados:</b>
 * <ul>
 *   <li>Análise e moderação pelo colaborador.</li>
 *   <li>Registro de recusa com justificativa obrigatória.</li>
 *   <li>Confirmação de liberação pelo usuário docente.</li>
 * </ul>
 *
 */
public enum StatusSolicitacao {

    /** Solicitação criada pelo docente e aguardando moderação. */
    PENDENTE,

    /** Solicitação analisada e aprovada pelo colaborador. */
    APROVADA,

    /** Solicitação reprovada pelo colaborador com justificativa registrada. */
    REPROVADA,

    /** Uso do espaço concluído e liberado pelo docente solicitante. */
    FINALIZADA
}
