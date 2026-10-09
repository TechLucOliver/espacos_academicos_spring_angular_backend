package com.controle.espacos.academicos.espacos_academicos.dto.instituicao;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.InstituicaoEnsinoSuperior;

import java.time.LocalDate;

/**
 * Objeto de Transferência de Dados (DTO) para envio dos dados da IES nas respostas da API.
 * <p>
 * Evita a exposição direta da entidade JPA e suas anotações internas para a camada cliente.
 * </p>
 *
 * @param id                Identificador único da IES na base de dados.
 * @param sigla             Sigla oficial da IES.
 * @param nome              Nome por extenso da instituição.
 * @param emailCentral      E-mail principal de atendimento da IES.
 * @param telefoneCentral   Telefone central de contacto.
 * @param nomeReitor        Nome do reitor vinculado.
 * @param dataCadastramento Data em que o cadastro foi efetuado.
 * @param status            Situação cadastral (ATIVO ou INATIVO).
 */
public record InstituicaoEnsinoSuperiorResponseDTO(
    Long id,
    String sigla,
    String nome,
    String emailCentral,
    String telefoneCentral,
    String nomeReitor,
    LocalDate dataCadastramento,
    StatusCadastro status
) {
    /**
     * Método de conveniência que converte a entidade JPA {@link InstituicaoEnsinoSuperior}
     * no respetivo DTO de saída pronto para ser serializado em JSON.
     *
     * @param ies Entidade persistida vinda do repositório.
     * @return Instância de {@link InstituicaoEnsinoSuperiorResponseDTO} populada.
     */
    public static InstituicaoEnsinoSuperiorResponseDTO fromEntity(InstituicaoEnsinoSuperior ies){
        return  new InstituicaoEnsinoSuperiorResponseDTO(
          ies.getId(), ies.getSigla(), ies.getNome(), ies.getEmailCentral(), ies.getTelefoneCentral(), ies.getNomeReitor(), ies.getDataCadastramento(), ies.getStatus()
        );
    }
}
