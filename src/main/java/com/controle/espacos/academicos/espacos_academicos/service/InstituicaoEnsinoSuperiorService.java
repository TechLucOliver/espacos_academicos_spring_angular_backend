package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.dto.instituicao.InstituicaoEnsinoSuperiorRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.instituicao.InstituicaoEnsinoSuperiorResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.model.InstituicaoEnsinoSuperior;
import com.controle.espacos.academicos.espacos_academicos.repository.InstituicaoEnsinoSuperiorRepository;
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
public class InstituicaoEnsinoSuperiorService {

    private final InstituicaoEnsinoSuperiorRepository iesRepositorio;

    /**
     * Construtor para injeção de dependência do repositório.
     *
     * @param iesRepositorio Repositório de persistência de IES gerido pelo Spring.
     */
    public InstituicaoEnsinoSuperiorService(InstituicaoEnsinoSuperiorRepository iesRepositorio){
        this.iesRepositorio = iesRepositorio;
    }

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

        InstituicaoEnsinoSuperior salva = iesRepositorio.save(novaIES);
        return InstituicaoEnsinoSuperiorResponseDTO.fromEntity(salva);
    }

    /**
     * Atualiza os dados cadastrais editáveis de uma IES existente.
     *
     * @param id Identificador da instituição a ser editada.
     * @param requestDTO Novos dados validados.
     * @return DTO de resposta atualizado.
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
