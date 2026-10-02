package com.controle.espacos.academicos.espacos_academicos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Objeto de Transferência de Dados (DTO) para receção dos dados de cadastro e edição de uma IES.
 * <p>
 * Implementado como um {@code record} imutável. Contém as anotações do Bean Validation
 * para validação estrutural automática dos dados submetidos pelo Front-End.
 * </p>
 *
 * @param sigla           Sigla institucional (obrigatória, até 20 caracteres).
 * @param nome            Nome completo da instituição (obrigatório, até 150 caracteres).
 * @param emailCentral    Correio eletrónico institucional de contacto central.
 * @param telefoneCentral Número telefónico para comunicação central.
 * @param nomeReitor      Nome completo do reitor responsável pela IES.
 *
 */
public record InstituicaoEnsinoSuperiorRequestDTO(

        @NotBlank(message = "Informar a sigla é obrigatório")
        @Size(max = 20, message = "A sigla não pode passar de 20 caracteres")
        String sigla,

        @NotBlank(message = "Informar o nome é obrigatório")
        @Size(max = 150, message = "O nome não pode passar de 150 caracteres")
        String nome,

        @NotBlank(message = "Informar o email é obrigatório")
        @Email(message = "Formato de email inválido")
        String emailCentral,

        @NotBlank(message = "Informar o telefone central é obrigatório")
        String telefoneCentral,

        @NotBlank(message = "Informar o nome do reitor é obrigatório")
        String nomeReitor
) { }
