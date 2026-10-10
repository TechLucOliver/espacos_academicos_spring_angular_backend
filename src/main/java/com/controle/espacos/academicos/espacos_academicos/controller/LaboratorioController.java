package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.laboratorio.LaboratorioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.laboratorio.LaboratorioResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratorios")
@RequiredArgsConstructor
public class LaboratorioController {

    private final LaboratorioService labService;

    @GetMapping
    public ResponseEntity<List<LaboratorioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(labService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratorioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(labService.buscarPorId(id));
    }

    @GetMapping("/centro-academico/{centroAcademicoId}")
    public ResponseEntity<List<LaboratorioResponseDTO>> listarPorCentroAcademico(@PathVariable Long centroAcademicoId) {
        return ResponseEntity.ok(labService.listarPorCentroAcademico(centroAcademicoId));
    }

    @PostMapping
    public ResponseEntity<LaboratorioResponseDTO> cadastrar(@Valid @RequestBody LaboratorioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labService.cadastrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratorioResponseDTO> editar(
            @PathVariable Long id,
            @Valid @RequestBody LaboratorioRequestDTO dto) {
        return ResponseEntity.ok(labService.editar(id, dto));
    }

    @PatchMapping("/{id}/manutencao")
    public ResponseEntity<Void> colocarEmManutencao(@PathVariable Long id) {
        labService.colocarEmManutencao(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Void> liberarManutencao(@PathVariable Long id) {
        labService.liberarManutencao(id);
        return ResponseEntity.noContent().build();
    }
}