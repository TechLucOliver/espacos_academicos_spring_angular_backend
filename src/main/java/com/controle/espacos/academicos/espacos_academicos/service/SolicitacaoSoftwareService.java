package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.config.logs.Auditavel;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software.ReprovarSolicitacaoSoftwareDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software.SolicitacaoSoftwareRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software.SolicitacaoSoftwareResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.model.Laboratorio;
import com.controle.espacos.academicos.espacos_academicos.model.Software;
import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import com.controle.espacos.academicos.espacos_academicos.model.solicitacoes.SolicitacaoSoftware;
import com.controle.espacos.academicos.espacos_academicos.repository.LaboratorioRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.SolicitacaoSoftwareRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.SoftwareRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoSoftwareService {

    private final SolicitacaoSoftwareRepository solicitacaoSoftwareRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final SoftwareRepository softwareRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<SolicitacaoSoftwareResponseDTO> listarTodas() {
        return solicitacaoSoftwareRepository.findAll()
                .stream()
                .map(SolicitacaoSoftwareResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoSoftwareResponseDTO buscarPorId(Long id) {
        SolicitacaoSoftware solicitacao = solicitacaoSoftwareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de software não encontrada com o ID: " + id));
        return SolicitacaoSoftwareResponseDTO.fromEntity(solicitacao);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoSoftwareResponseDTO> listarPorLaboratorio(Long laboratorioId) {
        return solicitacaoSoftwareRepository.findByLaboratorioId(laboratorioId)
                .stream()
                .map(SolicitacaoSoftwareResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoSoftwareResponseDTO> listarPorSolicitante(Long solicitanteId) {
        return solicitacaoSoftwareRepository.findBySolicitanteId(solicitanteId)
                .stream()
                .map(SolicitacaoSoftwareResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoSoftwareResponseDTO> listarPorStatus(StatusSolicitacao status) {
        return solicitacaoSoftwareRepository.findByStatus(status)
                .stream()
                .map(SolicitacaoSoftwareResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    @Auditavel(acao = "CADASTRAR_SOLICITACAO_SOFTWARE")
    public SolicitacaoSoftwareResponseDTO solicitar(SolicitacaoSoftwareRequestDTO dto) {
        Laboratorio lab = laboratorioRepository.findById(dto.laboratorioId())
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado com o ID: " + dto.laboratorioId()));

        if (lab.getStatus() == StatusEspaco.INATIVO) {
            throw new IllegalStateException("Não é possível solicitar software para um laboratório inativo/em manutenção.");
        }

        Software software = softwareRepository.findById(dto.softwareId())
                .orElseThrow(() -> new RuntimeException("Software não encontrado com o ID: " + dto.softwareId()));

        if (lab.getSoftwaresInstalados().contains(software)) {
            throw new IllegalStateException("O software informado já se encontra instalado neste laboratório.");
        }

        Usuario solicitante = usuarioRepository.findById(dto.solicitanteId())
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado com o ID: " + dto.solicitanteId()));

        SolicitacaoSoftware solicitacao = SolicitacaoSoftware.builder()
                .laboratorio(lab)
                .software(software)
                .solicitante(solicitante)
                .build();

        SolicitacaoSoftware salva = solicitacaoSoftwareRepository.save(solicitacao);
        return SolicitacaoSoftwareResponseDTO.fromEntity(salva);
    }

    @Transactional
    @Auditavel(acao = "APROVAR_SOLICITACAO_SOFTWARE")
    public SolicitacaoSoftwareResponseDTO aprovar(Long id, Long avaliadorId) {
        SolicitacaoSoftware solicitacao = solicitacaoSoftwareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de software não encontrada com o ID: " + id));

        Usuario avaliador = usuarioRepository.findById(avaliadorId)
                .orElseThrow(() -> new RuntimeException("Usuário avaliador não encontrado com o ID: " + avaliadorId));

        solicitacao.aprovar(avaliador);
        SolicitacaoSoftware salva = solicitacaoSoftwareRepository.save(solicitacao);
        return SolicitacaoSoftwareResponseDTO.fromEntity(salva);
    }

    @Transactional
    @Auditavel(acao = "REPROVAR_SOLICITACAO_SOFTWARE")
    public SolicitacaoSoftwareResponseDTO reprovar(Long id, ReprovarSolicitacaoSoftwareDTO dto) {
        SolicitacaoSoftware solicitacao = solicitacaoSoftwareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de software não encontrada com o ID: " + id));

        Usuario avaliador = usuarioRepository.findById(dto.avaliadorId())
                .orElseThrow(() -> new RuntimeException("Usuário avaliador não encontrado com o ID: " + dto.avaliadorId()));

        solicitacao.reprovar(avaliador, dto.motivo());
        SolicitacaoSoftware salva = solicitacaoSoftwareRepository.save(solicitacao);
        return SolicitacaoSoftwareResponseDTO.fromEntity(salva);
    }
}
