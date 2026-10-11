package com.controle.espacos.academicos.espacos_academicos.dto.usuario;

import com.controle.espacos.academicos.espacos_academicos.enums.PerfilAcesso;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.Usuario;

import java.time.LocalDate;
import java.util.Set;

public record UsuarioResponseDTO(
        Long id,
        String matricula,
        String nomeCompleto,
        String email,
        String telefone,
        LocalDate dataCadastramento,
        StatusCadastro status,
        String motivoInativacao,
        Long centroAcademicoId,
        String centroAcademicoNome,
        Set<PerfilAcesso> perfis
) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getMatricula(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getDataCadastramento(),
                usuario.getStatus(),
                usuario.getMotivoInativacao(),
                usuario.getCentroAcademico() != null ? usuario.getCentroAcademico().getId() : null,
                usuario.getCentroAcademico() != null ? usuario.getCentroAcademico().getNome() : null,
                usuario.getPerfis()
        );
    }
}
