package com.controle.espacos.academicos.espacos_academicos.dto.instituicao;

import com.controle.espacos.academicos.espacos_academicos.dto.pro_reitoria.ProReitoriaRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

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
 * @param proReitorias    Lista com as pró-reitorias e respectivas nomeações vinculadas à IES.
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
        @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
        String emailCentral,

        @NotBlank(message = "Informar o telefone central é obrigatório")
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
        String telefoneCentral,

        @NotBlank(message = "Informar o nome do reitor é obrigatório")
        @Size(max = 100, message = "O nome do Reitor deve ter no máximo 100 caracteres.")
        String nomeReitor,

        @Valid
        List<ProReitoriaRequestDTO> proReitorias
) { }
