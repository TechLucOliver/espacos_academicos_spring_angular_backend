package com.controle.espacos.academicos.espacos_academicos.dto.curso;

import com.controle.espacos.academicos.espacos_academicos.enums.Turno;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Objeto de Transferência de Dados para entrada (payload) no cadastro e edição integral de Cursos.
 *
 * @param sigla             Sigla de identificação do curso (ex.: BES, ADS, CCO).
 * @param descricao         Nome por extenso ou descrição formal do curso.
 * @param turno             Turno de oferta letiva (MATUTINO, VESPERTINO, NOTURNO, INTEGRAL).
 * @param nomeCoordenador   Nome completo do docente coordenador responsável.
 * @param centroAcademicoId Identificador da unidade acadêmica à qual o curso está vinculado.
 *
 */
public record CursoRequestDTO(
        @NotBlank(message = "A sigla do curso é obrigatória.")
        @Size(max = 20, message = "A sigla não pode exceder 20 caracteres.")
        String sigla,

        @NotBlank(message = "A descrição do curso é obrigatória.")
        @Size(max = 150, message = "A descrição não pode exceder 150 caracteres.")
        String descricao,

        @NotNull(message = "O turno do curso é obrigatório.")
        Turno turno,

        @NotBlank(message = "O nome do coordenador do curso é obrigatório.")
        @Size(max = 100, message = "O nome do coordenador não pode exceder 100 caracteres.")
        String nomeCoordenador,

        @NotNull(message = "O identificador do Centro Acadêmico de vínculo é obrigatório.")
        Long centroAcademicoId
) { }
