package com.controle.espacos.academicos.espacos_academicos.dto.curso;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.enums.Turno;
import com.controle.espacos.academicos.espacos_academicos.model.Curso;

import java.time.LocalDate;

/**
 * Objeto de Transferência de Dados para resposta serializada em JSON contendo as informações do Curso.
 *
 * @param id                  Identificador primário do curso.
 * @param sigla               Sigla de identificação.
 * @param descricao           Nome por extenso do curso.
 * @param turno               Turno de funcionamento.
 * @param nomeCoordenador     Docente responsável pela coordenação.
 * @param dataCadastramento   Data de registro original no sistema.
 * @param status              Situação cadastral (ATIVO ou INATIVO).
 * @param centroAcademicoId   Identificador do Centro Acadêmico mantenedor.
 * @param centroAcademicoNome Nome do Centro Acadêmico mantenedor para renderização em tela.
 *
 */
public record CursoResponseDTO(
        Long id,
        String sigla,
        String descricao,
        Turno turno,
        String nomeCoordenador,
        LocalDate dataCadastramento,
        StatusCadastro status,
        Long centroAcademicoId,
        String centroAcademicoNome
) {
    /**
     * Converte uma entidade de domínio {@link Curso} para o formato de resposta da API.
     *
     * @param curso Entidade gerenciada recuperada do banco de dados.
     * @return DTO com os atributos do curso e dados descritivos da unidade vinculada.
     */
    public static CursoResponseDTO fromEntity(Curso curso){
        return new CursoResponseDTO(
                curso.getId(),
                curso.getSigla(),
                curso.getDescricao(),
                curso.getTurno(),
                curso.getNomeCoordenador(),
                curso.getDataCadastramento(),
                curso.getStatus(),
                curso.getCentroAcademico().getId(),
                curso.getCentroAcademico().getNome()
        );
    }
}
