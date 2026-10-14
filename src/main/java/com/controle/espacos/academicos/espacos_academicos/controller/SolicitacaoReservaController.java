package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_reserva.ReprovarSolicitacaoDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_reserva.SolicitacaoReservaRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_reserva.SolicitacaoReservaResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.service.SolicitacaoReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestão de solicitações de reserva de espaços acadêmicos.
 */
@RestController
@RequestMapping("/api/solicitacoes-reservas")
@RequiredArgsConstructor
public class SolicitacaoReservaController {

    private final SolicitacaoReservaService reservaService;

    /**
     * Endpoint para submeter uma nova solicitação de reserva.
     */
    @PostMapping
    public ResponseEntity<SolicitacaoReservaResponseDTO> solicitar(@Valid @RequestBody SolicitacaoReservaRequestDTO dto) {
        SolicitacaoReservaResponseDTO resposta = reservaService.solicitar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    /**
     * Endpoint para listar solicitações (com filtro opcional por status).
     */
    @GetMapping
    public ResponseEntity<List<SolicitacaoReservaResponseDTO>> listarTodas(@RequestParam(required = false) StatusSolicitacao status) {
        if (status != null) {
            return ResponseEntity.ok(reservaService.listarPorStatus(status));
        }
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    /**
     * Endpoint para buscar uma solicitação por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoReservaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    /**
     * Endpoint para listar solicitações de um docente específico.
     */
    @GetMapping("/solicitante/{solicitanteId}")
    public ResponseEntity<List<SolicitacaoReservaResponseDTO>> listarPorSolicitante(@PathVariable Long solicitanteId) {
        return ResponseEntity.ok(reservaService.listarPorSolicitante(solicitanteId));
    }

    /**
     * Endpoint para aprovar uma solicitação de reserva.
     */
    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<SolicitacaoReservaResponseDTO> aprovar(
            @PathVariable Long id,
            @RequestParam Long avaliadorId) {
        return ResponseEntity.ok(reservaService.aprovar(id, avaliadorId));
    }

    /**
     * Endpoint para reprovar uma solicitação de reserva com justificativa.
     */
    @PatchMapping("/{id}/reprovar")
    public ResponseEntity<SolicitacaoReservaResponseDTO> reprovar(
            @PathVariable Long id,
            @Valid @RequestBody ReprovarSolicitacaoDTO dto) {
        return ResponseEntity.ok(reservaService.reprovar(id, dto));
    }

    /**
     * Endpoint para finalizar o uso do espaço reservado.
     */
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<SolicitacaoReservaResponseDTO> finalizarUso(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.finalizarUso(id));
    }
}
