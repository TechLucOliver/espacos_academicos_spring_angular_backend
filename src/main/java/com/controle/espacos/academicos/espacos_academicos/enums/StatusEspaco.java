package com.controle.espacos.academicos.espacos_academicos.enums;

/**
 * Representa os estados operacionais possíveis de um espaço acadêmico (sala, laboratório ou auditório).
 * <p>
 * O ciclo de transição de status é gerenciado automaticamente pelas regras de reserva e manutenção.
 * </p>
 *
 * <b>Requisitos associados:</b>
 * <ul>
 *   <li>Status "DISPONIVEL" atribuído automaticamente na criação.</li>
 *   <li>Transição para "INATIVO" em casos de manutenção.</li>
 *   <li>Transição para "INDISPONIVEL" após aprovação de reserva.</li>
 *   <li>Retorno para "DISPONIVEL" após liberação do usuário.</li>
 * </ul>
 *
 */
public enum StatusEspaco {
    /**
     * O espaço está liberado e apto a receber novas solicitações de reserva.
     */
    DISPONIVEL,

    /**
     * O espaço possui uma reserva aprovada em andamento para o intervalo de tempo.
     */
    INDISPONIVEL,

    /**
     * O espaço está temporariamente interditado (ex.: obras, manutenções).
     */
    INATIVO
}
