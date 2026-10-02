package com.controle.espacos.academicos.espacos_academicos.enums;

/**
 * Define os papéis de controle de acesso (RBAC) suportados pelo sistema.
 * <p>
 * Um usuário pode acumular múltiplos perfis simultaneamente.
 * </p>
 *
 */
public enum PerfilAcesso {
    /**
     * Responsável pelo gerenciamento de entidades globais (IES, CAs, Cursos, Docentes e Logs).
     */
    ADMINISTRADOR,

    /**
     * Docente coordenador responsável por gerir espaços e aprovar/reprovar solicitações.
     */
    COLABORADOR,

    /**
     * Docente que efetua solicitações de reservas de espaços e pedidos de software.
     */
    USUARIO
}
