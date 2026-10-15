package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.software.SoftwareRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.software.SoftwareResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoSoftware;
import com.controle.espacos.academicos.espacos_academicos.service.SoftwareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para o catálogo de Softwares acadêmicos.
 * <p>
 * Mapeado sob {@code /api/softwares}.
 * </p>
 */
@RestController
@RequestMapping("/api/softwares")
@RequiredArgsConstructor
public class SoftwareController {

    private final SoftwareService softwareService;

    /**
     * Cadastra um novo software no catálogo.
     *
     * @param dto Dados de entrada validados do software.
     * @return {@link ResponseEntity} com o software cadastrado e código HTTP 201 (Created).
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<SoftwareResponseDTO> cadastrar(@Valid @RequestBody SoftwareRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(softwareService.cadastrar(dto));
    }

    /**
     * Lista todos os softwares cadastrados no sistema.
     *
     * @return {@link ResponseEntity} com a lista de softwares e código HTTP 200 (OK).
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<List<SoftwareResponseDTO>> listarTodos() {
        return ResponseEntity.ok(softwareService.listarTodos());
    }

    /**
     * Recupera os dados de um software pelo seu identificador primário.
     *
     * @param id Identificador do software.
     * @return {@link ResponseEntity} com os dados do software e código HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<SoftwareResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(softwareService.buscarPorId(id));
    }

    /**
     * Filtra softwares por tipo de licenciamento (PROPRIETARIO ou SOFTWARE_LIVRE).
     *
     * @param tipo Tipo de licença do software.
     * @return {@link ResponseEntity} com a lista filtrada e código HTTP 200 (OK).
     */
    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<List<SoftwareResponseDTO>> listarPorTipo(@PathVariable TipoSoftware tipo) {
        return ResponseEntity.ok(softwareService.listarPorTipo(tipo));
    }

    /**
     * Atualiza os dados de um software cadastrado.
     *
     * @param id  Identificador do software a ser alterado.
     * @param dto Novos dados validados.
     * @return {@link ResponseEntity} com o software atualizado e código HTTP 200 (OK).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<SoftwareResponseDTO> editar(
            @PathVariable Long id,
            @Valid @RequestBody SoftwareRequestDTO dto) {
        return ResponseEntity.ok(softwareService.editar(id, dto));
    }
}