package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.config.logs.Auditavel;
import com.controle.espacos.academicos.espacos_academicos.dto.software.SoftwareRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.software.SoftwareResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoSoftware;
import com.controle.espacos.academicos.espacos_academicos.model.Software;
import com.controle.espacos.academicos.espacos_academicos.repository.SoftwareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviços para gestão das regras de negócio do catálogo de Softwares.
 *
 * @author Equipe de Desenvolvimento
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SoftwareService {

    private final SoftwareRepository softwareRepository;

    /**
     * Cadastra um novo software no catálogo com verificação de duplicidade de nome.
     *
     * @param dto Dados de entrada validados.
     * @return DTO do software cadastrado.
     * @throws IllegalArgumentException se já houver um software com o mesmo nome.
     */
    @Transactional
    @Auditavel(acao = "CADASTRAR")
    public SoftwareResponseDTO cadastrar(SoftwareRequestDTO dto) {
        if (softwareRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new IllegalArgumentException("Já existe um software cadastrado com o nome informado.");
        }

        Software software = Software.builder()
                .nome(dto.nome())
                .urlDownload(dto.urlDownload())
                .tipo(dto.tipo())
                .objetivoUso(dto.objetivoUso())
                .disciplinasVinculadas(dto.disciplinasVinculadas())
                .build();

        Software salvo = softwareRepository.save(software);
        return SoftwareResponseDTO.fromEntity(salvo);
    }

    /**
     * Lista todos os softwares cadastrados no catálogo institucional.
     *
     * @return Lista contendo os DTOs de resposta dos softwares.
     */
    @Transactional(readOnly = true)
    public List<SoftwareResponseDTO> listarTodos() {
        return softwareRepository.findAll()
                .stream()
                .map(SoftwareResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Localiza um software específico pelo identificador primário.
     *
     * @param id Identificador do software.
     * @return DTO com as informações do software localizado.
     * @throws RuntimeException se o software não for encontrado.
     */
    @Transactional(readOnly = true)
    public SoftwareResponseDTO buscarPorId(Long id) {
        Software software = softwareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Software não encontrado com o ID: " + id));
        return SoftwareResponseDTO.fromEntity(software);
    }

    /**
     * Filtra softwares por modalidade de licenciamento (PROPRIETARIO ou SOFTWARE_LIVRE).
     *
     * @param tipo Tipo de licença pretendida.
     * @return Lista de softwares correspondentes.
     */
    @Transactional(readOnly = true)
    public List<SoftwareResponseDTO> listarPorTipo(TipoSoftware tipo) {
        return softwareRepository.findByTipo(tipo)
                .stream()
                .map(SoftwareResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Atualiza os dados de um software existente.
     *
     * @param id  Identificador do software a ser alterado.
     * @param dto Novos dados validados.
     * @return DTO com as informações atualizadas.
     * @throws RuntimeException         se o software não existir.
     * @throws IllegalArgumentException se o novo nome colidir com outro software.
     */
    @Transactional
    @Auditavel(acao = "EDITAR")
    public SoftwareResponseDTO editar(Long id, SoftwareRequestDTO dto) {
        Software software = softwareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Software não encontrado com o ID: " + id));

        if (softwareRepository.existsByNomeIgnoreCaseAndIdNot(dto.nome(), id)) {
            throw new IllegalArgumentException("Já existe outro software cadastrado com o nome informado.");
        }

        software.setNome(dto.nome());
        software.setUrlDownload(dto.urlDownload());
        software.setTipo(dto.tipo());
        software.setObjetivoUso(dto.objetivoUso());
        software.setDisciplinasVinculadas(dto.disciplinasVinculadas());

        Software atualizado = softwareRepository.save(software);
        return SoftwareResponseDTO.fromEntity(atualizado);
    }
}