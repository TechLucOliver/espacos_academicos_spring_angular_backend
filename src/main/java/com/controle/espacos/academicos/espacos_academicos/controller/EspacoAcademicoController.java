package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.espaco_academico.EspacoAcademicoRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.espaco_academico.EspacoAcademicoResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.service.EspacoAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/espacos-academicos")
@RequiredArgsConstructor
public class EspacoAcademicoController {
    private final EspacoAcademicoService espacoService;

    @PostMapping
    public ResponseEntity<EspacoAcademicoResponseDTO> cadastrar(@Valid @RequestBody EspacoAcademicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(espacoService.cadastrar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspacoAcademicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(espacoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<EspacoAcademicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(espacoService.listarTodos());
    }

    @GetMapping("/centro-academico/{centroAcademicoId}")
    public ResponseEntity<List<EspacoAcademicoResponseDTO>> listarPorCentroAcademico(@PathVariable Long centroAcademicoId) {
        return ResponseEntity.ok(espacoService.listarPorCentroAcademico(centroAcademicoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspacoAcademicoResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody EspacoAcademicoRequestDTO dto) {
        return ResponseEntity.ok(espacoService.editar(id, dto));
    }

    @PatchMapping("/{id}/manutencao")
    public ResponseEntity<Void> colocarEmManutencao(@PathVariable Long id) {
        espacoService.colocarEmManutencao(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Void> liberarManutencao(@PathVariable Long id) {
        espacoService.liberarManutencao(id);
        return ResponseEntity.noContent().build();
    }
}
