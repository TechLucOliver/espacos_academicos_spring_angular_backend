package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.config.logs.Auditavel;
import com.controle.espacos.academicos.espacos_academicos.dto.espaco_academico.EspacoAcademicoRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.espaco_academico.EspacoAcademicoResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import com.controle.espacos.academicos.espacos_academicos.model.CentroAcademico;
import com.controle.espacos.academicos.espacos_academicos.model.EspacoAcademico;
import com.controle.espacos.academicos.espacos_academicos.repository.CentroAcademicoRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.EspacoAcademicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspacoAcademicoService {
    private final EspacoAcademicoRepository espacoRepository;
    private final CentroAcademicoRepository centroRepository;

    @Transactional(readOnly = true)
    public List<EspacoAcademicoResponseDTO> listarTodos(){
        return espacoRepository.findAll()
                .stream()
                .map(EspacoAcademicoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public EspacoAcademicoResponseDTO buscarPorId(Long id){
        EspacoAcademico espaco = espacoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espaço acadêmico como o id "+id+" não encontrado"));

        return EspacoAcademicoResponseDTO.fromEntity(espaco);
    }

    @Transactional(readOnly = true)
    public List<EspacoAcademicoResponseDTO> listarPorCentroAcademico(Long centroAcademicoId){
        return espacoRepository.findByCentroAcademicoId(centroAcademicoId)
                .stream()
                .map(EspacoAcademicoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    @Auditavel(acao = "CADASTRAR")
    public EspacoAcademicoResponseDTO cadastrar(EspacoAcademicoRequestDTO requestDTO){
        CentroAcademico centroAcademico = centroRepository.findById(requestDTO.centroAcademicoId())
                .orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado com o id: "+requestDTO.centroAcademicoId()));

        if (centroAcademico.getStatus() == StatusCadastro.INATIVO){
            throw new IllegalStateException("Não é possível associar um espaço a um Centro Acadêmico inativo.");
        }

        if (requestDTO.tipo() == TipoEspaco.LABORATORIO) {
            throw new IllegalArgumentException("Para cadastrar um laboratório, utilize o endpoint específico de laboratórios.");
        }

        if (espacoRepository.existsBySiglaIgnoreCaseAndCentroAcademicoId(requestDTO.sigla(), requestDTO.centroAcademicoId())) {
            throw new IllegalArgumentException("Já existe um espaço com esta sigla no Centro Acadêmico informado.");
        }

        EspacoAcademico novoEspaco = EspacoAcademico.builder()
                .sigla(requestDTO.sigla().toUpperCase())
                .nome(requestDTO.nome())
                .tipo(requestDTO.tipo())
                .descricaoComplementar(requestDTO.descricaoComplementar())
                .capacidadeMaxima(requestDTO.capacidadeMaxima())
                .localizacao(requestDTO.localizacao())
                .status(StatusEspaco.DISPONIVEL)
                .centroAcademico(centroAcademico)
                .build();

        EspacoAcademico espacoSalvo = espacoRepository.save(novoEspaco);
        return EspacoAcademicoResponseDTO.fromEntity(espacoSalvo);
    }

    @Transactional
    @Auditavel(acao = "EDITAR")
    public EspacoAcademicoResponseDTO editar(Long id, EspacoAcademicoRequestDTO requestDTO){
        EspacoAcademico espacoAcademico = espacoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espaço acadêmico não encontrado com o ID: " + id));

        CentroAcademico centroAcademico = centroRepository.findById(requestDTO.centroAcademicoId())
                .orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado com o ID: " + requestDTO.centroAcademicoId()));

        if (centroAcademico.getStatus() == StatusCadastro.INATIVO){
            throw new IllegalStateException("Não é possível associar o espaço a um Centro Acadêmico inativo.");
        }

        if (espacoRepository.existsBySiglaIgnoreCaseAndCentroAcademicoIdAndIdNot(requestDTO.sigla(), requestDTO.centroAcademicoId(), id)) {
            throw new IllegalArgumentException("Já existe outro espaço com esta sigla no Centro Acadêmico informado.");
        }

        espacoAcademico.setSigla(requestDTO.sigla().toUpperCase());
        espacoAcademico.setNome(requestDTO.nome());
        espacoAcademico.setTipo(requestDTO.tipo());
        espacoAcademico.setDescricaoComplementar(requestDTO.descricaoComplementar());
        espacoAcademico.setCapacidadeMaxima(requestDTO.capacidadeMaxima());
        espacoAcademico.setLocalizacao(requestDTO.localizacao());
        espacoAcademico.setCentroAcademico(centroAcademico);

        EspacoAcademico espacoAtualizado = espacoRepository.save(espacoAcademico);
        return EspacoAcademicoResponseDTO.fromEntity(espacoAtualizado);
    }

    /**
     * Interdita o espaço acadêmico para manutenções ou reparos físicos.
     *
     * @param id Identificador do espaço.
     */
    @Transactional
    @Auditavel(acao = "REATIVAR")
    public void colocarEmManutencao(Long id){
        EspacoAcademico espacoAcademico = espacoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espaço acadêmico não encontrado com o ID: " + id));

        if (espacoAcademico.getStatus() == StatusEspaco.INATIVO) {
            throw new IllegalStateException("O espaço já se encontra em manutenção/interditado.");
        }

        if (espacoAcademico.getStatus() == StatusEspaco.INDISPONIVEL) {
            throw new IllegalStateException("Não é possível colocar em manutenção um espaço com reserva ativa.");
        }

        espacoAcademico.setStatus(StatusEspaco.INATIVO);
        espacoRepository.save(espacoAcademico);
    }

    /**
     * Libera o espaço de manutenção, tornando-o novamente disponível para uso e reservas.
     *
     * @param id Identificador do espaço.
     */
    @Transactional
    @Auditavel(acao = "LIBERAR MANUTENCAO")
    public void liberarManutencao(Long id){
        EspacoAcademico espacoAcademico = espacoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espaço acadêmico não encontrado com o ID: " + id));

        if (espacoAcademico.getStatus() != StatusEspaco.INATIVO) {
            throw new IllegalStateException("Apenas espaços interditados ou em manutenção podem ser liberados.");
        }

        espacoAcademico.setStatus(StatusEspaco.DISPONIVEL);
        espacoRepository.save(espacoAcademico);
    }
}
