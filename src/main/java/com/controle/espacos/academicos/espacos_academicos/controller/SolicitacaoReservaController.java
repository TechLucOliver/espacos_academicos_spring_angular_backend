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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitacaoReservaResponseDTO> solicitar(@Valid @RequestBody SolicitacaoReservaRequestDTO dto) {
        SolicitacaoReservaResponseDTO resposta = reservaService.solicitar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    /**
     * Ponto de extremidade para listagem de solicitações de reserva, com suporte a filtro opcional por estado.
     * <p>
     * Acessível a utilizadores autenticados com os perfis ADMINISTRADOR, COLABORADOR ou USUARIO.
     * </p>
     *
     * @param status Estado opcional para filtragem dos registos (ex.: PENDENTE, APROVADA).
     * @return Lista com os DTOs das solicitações localizadas.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<List<SolicitacaoReservaResponseDTO>> listarTodas(@RequestParam(required = false) StatusSolicitacao status) {
        if (status != null) {
            return ResponseEntity.ok(reservaService.listarPorStatus(status));
        }
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    /**
     * Ponto de extremidade para recuperação dos detalhes de uma solicitação de reserva por ID.
     * <p>
     * Acessível a administradores, colaboradores e utilizadores autenticados.
     * </p>
     *
     * @param id Identificador primário da solicitação de reserva.
     * @return DTO correspondente à solicitação localizada.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<SolicitacaoReservaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    /**
     * Ponto de extremidade para consulta do histórico de solicitações associadas a um docente específico.
     * <p>
     * Permite ao docente acompanhar os seus pedidos e à equipa administrativa auditar o histórico de um solicitante.
     * </p>
     *
     * @param solicitanteId Identificador primário do docente solicitante.
     * @return Lista com as solicitações submetidas pelo docente.
     */
    @GetMapping("/solicitante/{solicitanteId}")
    @PreAuthorize("hasAnyRole('USUARIO', 'COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<List<SolicitacaoReservaResponseDTO>> listarPorSolicitante(@PathVariable Long solicitanteId) {
        return ResponseEntity.ok(reservaService.listarPorSolicitante(solicitanteId));
    }

    /**
     * Endpoint para aprovar uma solicitação de reserva.
     */
    @PatchMapping("/{id}/aprovar")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitacaoReservaResponseDTO> aprovar(
            @PathVariable Long id,
            @RequestParam Long avaliadorId) {
        return ResponseEntity.ok(reservaService.aprovar(id, avaliadorId));
    }

    /**
     * Endpoint para reprovar uma solicitação de reserva com justificativa.
     */
    @PatchMapping("/{id}/reprovar")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitacaoReservaResponseDTO> reprovar(
            @PathVariable Long id,
            @Valid @RequestBody ReprovarSolicitacaoDTO dto) {
        return ResponseEntity.ok(reservaService.reprovar(id, dto));
    }

    /**
     * Endpoint para finalizar o uso do espaço reservado.
     */
    @PatchMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    public ResponseEntity<SolicitacaoReservaResponseDTO> finalizarUso(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.finalizarUso(id));
    }
}
