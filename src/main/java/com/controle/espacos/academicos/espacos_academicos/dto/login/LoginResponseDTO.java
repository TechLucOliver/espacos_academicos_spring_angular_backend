package com.controle.espacos.academicos.espacos_academicos.dto.login;

import com.controle.espacos.academicos.espacos_academicos.enums.PerfilAcesso;

import java.util.Set;

/**
 * Objeto de Transferência de Dados (DTO) devolvido após a autenticação bem-sucedida.
 *
 * @param token Token JWT compactado assinado digitalmente.
 * @param tipo Prefixo do esquema de autenticação HTTP (padrão {@code Bearer}).
 * @param usuarioId Identificador primário do usuário na base de dados.
 * @param nome Nome completo do docente ou administrador logado.
 * @param email Endereço de e-mail institucional registrado.
 * @param matricula Código da matrícula funcional.
 * @param perfis Conjunto de perfis e autoridades de acesso concedidos ao usuário.
 */
public record LoginResponseDTO(
        String token,
        String tipo,
        Long usuarioId,
        String nome,
        String email,
        String matricula,
        Set<PerfilAcesso> perfis
) {
}
