package com.controle.espacos.academicos.espacos_academicos.enums;

/**
 * Representa os estados possíveis do ciclo de vida cadastral das entidades do sistema.
 * <p>
 * Utilizado para assegurar o mecanismo de inativação lógica (soft delete)
 * sem perda de histórico de relacionamentos.
 * </p>
 */
public enum StatusCadastro {
    /**
     * Indica que o registo está ativo e apto para operações regulares no ecossistema.
     */
    ATIVO,

    /**
     * Indica que o registo foi inativado logicamente, permanecendo na base de dados
     * apenas para preservação do histórico institucional e de auditoria.
     */
    INATIVO
}
