package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.dto.centro_academico.CentroAcademicoRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.centro_academico.CentroAcademicoResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.CentroAcademico;
import com.controle.espacos.academicos.espacos_academicos.model.InstituicaoEnsinoSuperior;
import com.controle.espacos.academicos.espacos_academicos.repository.CentroAcademicoRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.InstituicaoEnsinoSuperiorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CentroAcademicoService {
    private final CentroAcademicoRepository centroAcademicoRepository;
    private final InstituicaoEnsinoSuperiorRepository iesRepository;

    /**
     * Construtor para injeção de dependências dos repositórios gerenciados pelo Spring.
     *
     * @param centroAcademicoRepository  Repositório de persistência de Centros Acadêmicos.
     * @param iesRepository Repositório de persistência de IES para validação referencial.
     */
    public CentroAcademicoService(CentroAcademicoRepository centroAcademicoRepository, InstituicaoEnsinoSuperiorRepository iesRepository){
        this.centroAcademicoRepository = centroAcademicoRepository;
        this.iesRepository = iesRepository;
    }

    /**
     * Registra um novo Centro Acadêmico aplicando regras de integridade e unicidade.
     *
     * @param requestDTO Dados de entrada validados do Centro Acadêmico.
     * @return DTO de resposta do Centro Acadêmico persistido.
     * @throws RuntimeException         se a IES informada não existir.
     * @throws IllegalArgumentException se já houver um CA com o mesmo nome na referida IES.
     */
    @Transactional
    public CentroAcademicoResponseDTO cadastrar(CentroAcademicoRequestDTO requestDTO){
        InstituicaoEnsinoSuperior ies = iesRepository.findById(requestDTO.iesId())
                .orElseThrow(() -> new RuntimeException("Ies não encontrada com o ID: "+requestDTO.iesId()));

        if (centroAcademicoRepository.existsByNomeIgnoreCaseAndInstituicaoId(requestDTO.nome(), requestDTO.iesId())){
            throw new IllegalArgumentException("Já existe um Centro Acadêmico com o nome informado");
        }

        CentroAcademico novoCentroAcademico = CentroAcademico.builder()
                .nome(requestDTO.nome())
                .emailCentral(requestDTO.emailCentral())
                .telefoneCentral(requestDTO.telefoneCentral())
                .nomeCoordenador(requestDTO.nomeCoordenador())
                .instituicao(ies)
                .build();

        CentroAcademico salvo = centroAcademicoRepository.save(novoCentroAcademico);
        return CentroAcademicoResponseDTO.fromEntity(salvo);
    }

    /**
     * Atualiza os dados de um Centro Acadêmico existente.
     *
     * @param id         Identificador do Centro Acadêmico.
     * @param requestDTO Novos dados validados.
     * @return DTO de resposta do CA atualizado.
     */
    @Transactional
    public CentroAcademicoResponseDTO editar(Long id, CentroAcademicoRequestDTO requestDTO){
        CentroAcademico centroAcademico = centroAcademicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro Academico não encontrado com id: "+id));

        if (centroAcademicoRepository.existsByNomeIgnoreCaseAndInstituicaoIdAndIdNot(requestDTO.nome(), requestDTO.iesId(), id)){
            throw new IllegalArgumentException("Já existe outro CentroAcademico com o nome: "+requestDTO.nome());
        }

        InstituicaoEnsinoSuperior instituicao = iesRepository.findById(requestDTO.iesId())
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada com o id: "+requestDTO.iesId()));

        centroAcademico.setNome(requestDTO.nome());
        centroAcademico.setEmailCentral(requestDTO.emailCentral());
        centroAcademico.setTelefoneCentral(requestDTO.telefoneCentral());
        centroAcademico.setNomeCoordenador(requestDTO.nomeCoordenador());
        centroAcademico.setInstituicao(instituicao);

        CentroAcademico centroAtualizado = centroAcademicoRepository.save(centroAcademico);
        return CentroAcademicoResponseDTO.fromEntity(centroAtualizado);
    }

    /**
     * Lista todos os Centros Acadêmicos cadastrados no sistema.
     *
     * @return Lista contendo os DTOs de resposta dos centros encontrados.
     */
    @Transactional(readOnly = true)
    public List<CentroAcademicoResponseDTO> listarTodos(){
        return  centroAcademicoRepository.findAll()
                .stream()
                .map(CentroAcademicoResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Busca um Centro Acadêmico específico por seu identificador.
     *
     * @param id Identificador primário do centro.
     * @return DTO correspondente ao registro localizado.
     * @throws RuntimeException se o registro não for localizado na base de dados.
     */
    @Transactional(readOnly = true)
    public  CentroAcademicoResponseDTO buscarPorId(Long id){
        CentroAcademico centroAcademico = centroAcademicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro Academico não encontrado com o ID: "+ id));

        return CentroAcademicoResponseDTO.fromEntity(centroAcademico);
    }

    /**
     * Lista todos os Centros Acadêmicos pertencentes a uma IES informada.
     *
     * @param iesId Identificador da Instituição de Ensino Superior.
     * @return Lista de Centros Acadêmicos vinculados à referida IES.
     */
    @Transactional(readOnly = true)
    public List<CentroAcademicoResponseDTO> listarPorInstituicao(Long iesId){
        return centroAcademicoRepository.findByInstituicaoId(iesId)
                .stream()
                .map(CentroAcademicoResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Executa a inativação lógica do Centro Acadêmico preservando dados históricos (RF-010).
     *
     * @param id Identificador primário do Centro Acadêmico a ser inativado.
     * @throws RuntimeException se o Centro Acadêmico não for localizado.
     */
    @Transactional
    public void inativar(Long id){
        CentroAcademico centroAcademico = centroAcademicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro Academico não encontrado com o ID: "+id));

        centroAcademico.inativar();
        centroAcademicoRepository.save(centroAcademico);
    }

    /**
     * Reativa o cadastro de um Centro Acadêmico.
     * <p>
     * Valida previamente se a Instituição mantenedora está ativa.
     * </p>
     *
     * @param id Identificador primário do Centro Acadêmico.
     * @throws RuntimeException se o Centro Acadêmico não for encontrado.
     * @throws IllegalStateException se a IES mantenedora estiver inativa.
     */
    @Transactional
    public void reativar(Long id) {
        CentroAcademico centroAcademico = centroAcademicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado com o ID: " + id));

        if (centroAcademico.getInstituicao().getStatus() == StatusCadastro.INATIVO) {
            throw new IllegalStateException("Não é possível reativar um Centro Acadêmico cujo vínculo com a IES esteja inativo.");
        }

        centroAcademico.reativar();
        centroAcademicoRepository.save(centroAcademico);
    }
}
