package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software.ReprovarSolicitacaoSoftwareDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software.SolicitacaoSoftwareRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_software.SolicitacaoSoftwareResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.service.SolicitacaoSoftwareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável por gerenciar as solicitações de instalação de softwares em laboratórios.
 */
@RestController
@RequestMapping("/api/solicitacoes-softwares")
@RequiredArgsConstructor
public class SolicitacaoSoftwareController {

    private final SolicitacaoSoftwareService solicitacaoSoftwareService;

    /**
     * Submete um novo pedido de instalação de software para um laboratório.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitacaoSoftwareResponseDTO> solicitar(@Valid @RequestBody SolicitacaoSoftwareRequestDTO dto) {
        SolicitacaoSoftwareResponseDTO resposta = solicitacaoSoftwareService.solicitar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    /**
     * Lista todas as solicitações de software, permitindo filtro opcional por status.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<List<SolicitacaoSoftwareResponseDTO>> listarTodas(@RequestParam(required = false) StatusSolicitacao status) {
        if (status != null) {
            return ResponseEntity.ok(solicitacaoSoftwareService.listarPorStatus(status));
        }
        return ResponseEntity.ok(solicitacaoSoftwareService.listarTodas());
    }

    /**
     * Busca uma solicitação de software pelo seu ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<SolicitacaoSoftwareResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoSoftwareService.buscarPorId(id));
    }

    /**
     * Lista as solicitações de software associadas a um determinado laboratório.
     */
    @GetMapping("/laboratorio/{laboratorioId}")
    @PreAuthorize("hasAnyRole('USUARIO', 'COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<List<SolicitacaoSoftwareResponseDTO>> listarPorLaboratorio(@PathVariable Long laboratorioId) {
        return ResponseEntity.ok(solicitacaoSoftwareService.listarPorLaboratorio(laboratorioId));
    }

    /**
     * Lista as solicitações de software efetuadas por um docente/solicitante específico.
     */
    @GetMapping("/solicitante/{solicitanteId}")
    @PreAuthorize("hasAnyRole('USUARIO', 'COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<List<SolicitacaoSoftwareResponseDTO>> listarPorSolicitante(@PathVariable Long solicitanteId) {
        return ResponseEntity.ok(solicitacaoSoftwareService.listarPorSolicitante(solicitanteId));
    }

    /**
     * Aprova uma solicitação de instalação de software.
     */
    @PatchMapping("/{id}/aprovar")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitacaoSoftwareResponseDTO> aprovar(
            @PathVariable Long id,
            @RequestParam Long avaliadorId) {
        return ResponseEntity.ok(solicitacaoSoftwareService.aprovar(id, avaliadorId));
    }

    /**
     * Reprova uma solicitação de instalação de software com justificativa técnica.
     */
    @PatchMapping("/{id}/reprovar")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitacaoSoftwareResponseDTO> reprovar(
            @PathVariable Long id,
            @Valid @RequestBody ReprovarSolicitacaoSoftwareDTO dto) {
        return ResponseEntity.ok(solicitacaoSoftwareService.reprovar(id, dto));
    }
}
