package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.dto.instituicao.InstituicaoEnsinoSuperiorRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.instituicao.InstituicaoEnsinoSuperiorResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.InstituicaoEnsinoSuperior;
import com.controle.espacos.academicos.espacos_academicos.model.ProReitoria;
import com.controle.espacos.academicos.espacos_academicos.repository.InstituicaoEnsinoSuperiorRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço responsável por coordenar a lógica de negócio, transações e validações
 * associadas à gestão das Instituições de Ensino Superior (IES).
 * <p>
 * Atua como mediadora entre os controladores REST e a camada de acesso a dados (Repositório).
 * </p>
 *
 */
@Service
@AllArgsConstructor 
public class InstituicaoEnsinoSuperiorService {

    private final InstituicaoEnsinoSuperiorRepository iesRepositorio;

    /**
     * Realiza o registo de uma nova Instituição de Ensino Superior.
     * <p>
     * Valida previamente se a sigla informada já não se encontra em uso no sistema.
     * </p>
     *
     * @param requestDTO Dados de entrada submetidos pelo utilizador.
     * @return A instituição persistida convertida em DTO de resposta.
     * @throws IllegalArgumentException Caso a sigla informada já esteja registada.
     */
    @Transactional
    public InstituicaoEnsinoSuperiorResponseDTO cadastrar(InstituicaoEnsinoSuperiorRequestDTO requestDTO){
        if (iesRepositorio.existsBySiglaIgnoreCase(requestDTO.sigla())){
            throw new IllegalArgumentException("Já existe uma IES cadastrada com a sigla informada.");
        }

        InstituicaoEnsinoSuperior novaIES = InstituicaoEnsinoSuperior.builder()
                .sigla(requestDTO.sigla().toUpperCase())
                .nome(requestDTO.nome())
                .emailCentral(requestDTO.emailCentral())
                .telefoneCentral(requestDTO.telefoneCentral())
                .nomeReitor(requestDTO.nomeReitor())
                .build();

        if(requestDTO.proReitorias() != null && !requestDTO.proReitorias().isEmpty()){
            requestDTO.proReitorias().forEach(proReitoriaDTO -> {
                ProReitoria proReitoria = ProReitoria.builder()
                        .nome(proReitoriaDTO.nome())
                        .nomeResponsavel(proReitoriaDTO.nomeResponsavel())
                        .instituicao(novaIES)
                        .build();
                novaIES.adicionarProReitoria(proReitoria);
            });

        }

        InstituicaoEnsinoSuperior salva = iesRepositorio.save(novaIES);
        return InstituicaoEnsinoSuperiorResponseDTO.fromEntity(salva);
    }

    /**
     * Atualiza os dados cadastrais editáveis de uma IES existente e sincroniza sua lista de pró-reitorias.
     *
     * <p>
     * Valida se a nova sigla não colide com outra IES existente.
     * Graças ao orphanRemoval = true, pró-reitorias removidas da lista são excluídas do banco.
     * </p>
     * @param id Identificador da instituição a ser editada.
     * @param requestDTO Novos dados validados.
     * @return DTO de resposta atualizado.
     * @throws RuntimeException se a IES não for localizada pelo ID.
     * 
     */
    @Transactional
    public InstituicaoEnsinoSuperiorResponseDTO editar(Long id, InstituicaoEnsinoSuperiorRequestDTO requestDTO){
        InstituicaoEnsinoSuperior instituicao = iesRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada com o id: "+id));

        if (iesRepositorio.existsBySiglaIgnoreCaseAndIdNot(requestDTO.sigla(), id)){
            throw new IllegalArgumentException("Já existe outra Instituição cadastrada com a sigla informada");
        }

        instituicao.setSigla(requestDTO.sigla().toUpperCase());
        instituicao.setNome(requestDTO.nome());
        instituicao.setEmailCentral(requestDTO.emailCentral());
        instituicao.setTelefoneCentral(requestDTO.telefoneCentral());
        instituicao.setNomeReitor(requestDTO.nomeReitor());

        if(requestDTO.proReitorias() != null){
            instituicao.getProReitorias().clear();
            requestDTO.proReitorias().forEach(proReitoriaDTO -> {
                ProReitoria proReitoria = ProReitoria.builder()
                        .nome(proReitoriaDTO.nome())
                        .nomeResponsavel(proReitoriaDTO.nomeResponsavel())
                        .instituicao(instituicao)
                        .build();
                instituicao.adicionarProReitoria(proReitoria);
            });
        }

        InstituicaoEnsinoSuperior instituicaoAtualizada = iesRepositorio.save(instituicao);
        return InstituicaoEnsinoSuperiorResponseDTO.fromEntity(instituicaoAtualizada);
    }

    /**
     * Localiza uma instituição específica a partir do seu identificador primário.
     *
     * @param id Identificador da instituição desejada.
     * @return {@link InstituicaoEnsinoSuperiorResponseDTO} correspondente ao registo encontrado.
     * @throws RuntimeException Caso a instituição com o ID informado não exista na base.
     */
    @Transactional(readOnly = true)
    public InstituicaoEnsinoSuperiorResponseDTO buscarPorId(Long id){
        InstituicaoEnsinoSuperior ies = iesRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("IES não encontrada como id: "+id));
        return InstituicaoEnsinoSuperiorResponseDTO.fromEntity(ies);
    }

    /**
     * Consulta todas as IES registadas no sistema e transforma os resultados em DTOs.
     *
     * @return Lista contendo todas as instituições persistidas.
     */
    @Transactional(readOnly = true)
    public List<InstituicaoEnsinoSuperiorResponseDTO> listarTodas(){
        return iesRepositorio.findAll()
                .stream()
                .map(InstituicaoEnsinoSuperiorResponseDTO::fromEntity)
                .toList();
    }
    /**
     * Uma alternativa para as consultas. A consulta listarTodas inclui as
     * instituição inativas, poderia ser melhorada com o uso de um caixa, ou
     * duas, no front onde poderia ser escolhido instituições ativas, inativas ou
     * todas
     *
     * segue abaixo a implementação
     */
    @Transactional (readOnly = true)
    public List<InstituicaoEnsinoSuperiorResponseDTO> listar(StatusCadastro status){
        List<InstituicaoEnsinoSuperior> lista = (status == null)
                //cria a lista com todas as instituições caso não seja escolhido
                //nenhum status
                ? iesRepositorio.findAll()

                : iesRepositorio.findByStatus(status);
        return lista.stream().map(InstituicaoEnsinoSuperiorResponseDTO::fromEntity)
                .toList();
    }



    /**
     * Realiza a inativação lógica da instituição (soft delete), preservando o histórico.
     *
     * @param id Identificador da IES que será inativada.
     * @throws RuntimeException Caso a instituição não seja encontrada.
     */
    @Transactional
    public void inativar(Long id){
        InstituicaoEnsinoSuperior ies = iesRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("IES não encontrada com o id: "+id));

        ies.inativar();
        iesRepositorio.save(ies);
    }

    /**
     * Reativa uma Instituição de Ensino Superior previamente inativada.
     *
     * @param id Identificador numérico da IES.
     * @throws RuntimeException se a IES não for encontrada.
     */
    @Transactional
    public void reativar(Long id) {
        InstituicaoEnsinoSuperior ies = iesRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("IES não encontrada com o ID: " + id));

        ies.reativar();
        iesRepositorio.save(ies);
    }
}
