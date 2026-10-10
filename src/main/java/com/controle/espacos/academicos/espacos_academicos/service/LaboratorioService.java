package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.dto.laboratorio.LaboratorioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.laboratorio.LaboratorioResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import com.controle.espacos.academicos.espacos_academicos.model.CentroAcademico;
import com.controle.espacos.academicos.espacos_academicos.model.Laboratorio;
import com.controle.espacos.academicos.espacos_academicos.repository.CentroAcademicoRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.EspacoAcademicoRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.LaboratorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviços para gestão de Laboratórios de Informática.
 *
 */
@Service
@RequiredArgsConstructor
public class LaboratorioService {

    private final LaboratorioRepository labRepository;
    private final EspacoAcademicoRepository espacoRepository;
    private final CentroAcademicoRepository caRepository;

    @Transactional(readOnly = true)
    public List<LaboratorioResponseDTO> listarTodos() {
        return labRepository.findAll()
                .stream()
                .map(LaboratorioResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public LaboratorioResponseDTO buscarPorId(Long id) {
        Laboratorio lab = labRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado com o ID: " + id));
        return LaboratorioResponseDTO.fromEntity(lab);
    }

    @Transactional(readOnly = true)
    public List<LaboratorioResponseDTO> listarPorCentroAcademico(Long centroAcademicoId) {
        return labRepository.findByCentroAcademicoId(centroAcademicoId)
                .stream()
                .map(LaboratorioResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public LaboratorioResponseDTO cadastrar(LaboratorioRequestDTO dto) {
        CentroAcademico ca = caRepository.findById(dto.centroAcademicoId())
                .orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado com o ID: " + dto.centroAcademicoId()));

        if (ca.getStatus() == StatusCadastro.INATIVO) {
            throw new IllegalStateException("Não é possível associar um laboratório a um Centro Acadêmico inativo.");
        }

        if (espacoRepository.existsBySiglaIgnoreCaseAndCentroAcademicoId(dto.sigla(), dto.centroAcademicoId())) {
            throw new IllegalArgumentException("Já existe um espaço ou laboratório cadastrado com esta sigla no Centro Acadêmico.");
        }

        Laboratorio novoLab = Laboratorio.builder()
                .sigla(dto.sigla().toUpperCase())
                .nome(dto.nome())
                .tipo(TipoEspaco.LABORATORIO)
                .descricaoComplementar(dto.descricaoComplementar())
                .capacidadeMaxima(dto.capacidadeMaxima())
                .localizacao(dto.localizacao())
                .status(StatusEspaco.DISPONIVEL)
                .tipoComputadores(dto.tipoComputadores())
                .centroAcademico(ca)
                .build();

        Laboratorio salvo = labRepository.save(novoLab);
        return LaboratorioResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public LaboratorioResponseDTO editar(Long id, LaboratorioRequestDTO dto) {
        Laboratorio lab = labRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado com o ID: " + id));

        CentroAcademico ca = caRepository.findById(dto.centroAcademicoId())
                .orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado com o ID: " + dto.centroAcademicoId()));

        if (ca.getStatus() == StatusCadastro.INATIVO) {
            throw new IllegalStateException("Não é possível vincular o laboratório a um Centro Acadêmico inativo.");
        }

        if (espacoRepository.existsBySiglaIgnoreCaseAndCentroAcademicoIdAndIdNot(dto.sigla(), dto.centroAcademicoId(), id)) {
            throw new IllegalArgumentException("Já existe outro espaço com esta sigla no Centro Acadêmico informado.");
        }

        lab.setSigla(dto.sigla().toUpperCase());
        lab.setNome(dto.nome());
        lab.setDescricaoComplementar(dto.descricaoComplementar());
        lab.setCapacidadeMaxima(dto.capacidadeMaxima());
        lab.setLocalizacao(dto.localizacao());
        lab.setTipoComputadores(dto.tipoComputadores());
        lab.setCentroAcademico(ca);

        Laboratorio atualizado = labRepository.save(lab);
        return LaboratorioResponseDTO.fromEntity(atualizado);
    }

    /**
     * Interdita o laboratório para reparos técnicos ou de máquinas.
     *
     * @param id Identificador do laboratório.
     */
    @Transactional
    public void colocarEmManutencao(Long id) {
        Laboratorio lab = labRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado com o ID: " + id));

        if (lab.getStatus() == StatusEspaco.INATIVO) {
            throw new IllegalStateException("O laboratório já se encontra em manutenção/interditado.");
        }

        if (lab.getStatus() == StatusEspaco.INDISPONIVEL) {
            throw new IllegalStateException("Não é possível colocar em manutenção um laboratório com reserva ativa.");
        }

        lab.setStatus(StatusEspaco.INATIVO);
        labRepository.save(lab);
    }

    /**
     * Libera o laboratório de manutenção física ou técnica.
     *
     * @param id Identificador do laboratório.
     */
    @Transactional
    public void liberarManutencao(Long id) {
        Laboratorio lab = labRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado com o ID: " + id));

        if (lab.getStatus() != StatusEspaco.INATIVO) {
            throw new IllegalStateException("Apenas laboratórios interditados ou em manutenção podem ser liberados.");
        }

        lab.setStatus(StatusEspaco.DISPONIVEL);
        labRepository.save(lab);
    }
}