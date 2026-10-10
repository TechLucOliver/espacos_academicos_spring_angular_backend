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

/**
 * Controlador REST especializado na gestão de Laboratórios de Informática e Práticas Técnicas.
 *
 */
@RestController
@RequestMapping("/api/laboratorios")
@RequiredArgsConstructor
public class LaboratorioController {

    private final LaboratorioService labService;

    /**
     * Lista todos os laboratórios registados na instituição.
     *
     * @return {@link ResponseEntity} com a relação de todos os laboratórios e código HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<LaboratorioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(labService.listarTodos());
    }

    /**
     * Localiza um laboratório específico através do seu identificador primário.
     *
     * @param id Identificador do laboratório.
     * @return {@link ResponseEntity} com os dados consolidados do laboratório e código HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<LaboratorioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(labService.buscarPorId(id));
    }

    /**
     * Recupera os laboratórios pertencentes a uma unidade académica específica.
     *
     * @param centroAcademicoId Identificador primário do Centro Académico.
     * @return {@link ResponseEntity} com a lista de laboratórios vinculados e código HTTP 200 (OK).
     */
    @GetMapping("/centro-academico/{centroAcademicoId}")
    public ResponseEntity<List<LaboratorioResponseDTO>> listarPorCentroAcademico(@PathVariable Long centroAcademicoId) {
        return ResponseEntity.ok(labService.listarPorCentroAcademico(centroAcademicoId));
    }

    /**
     * Regista um novo laboratório de informática com especificações de hardware.
     *
     * @param dto Dados de entrada validados do laboratório.
     * @return {@link ResponseEntity} contendo o laboratório persistido e o código HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<LaboratorioResponseDTO> cadastrar(@Valid @RequestBody LaboratorioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labService.cadastrar(dto));
    }

    /**
     * Atualiza os dados de infraestrutura e localização de um laboratório existente.
     *
     * @param id  Identificador do laboratório a modificar.
     * @param dto Novos atributos do laboratório validados.
     * @return {@link ResponseEntity} com os dados atualizados e código HTTP 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<LaboratorioResponseDTO> editar(
            @PathVariable Long id,
            @Valid @RequestBody LaboratorioRequestDTO dto) {
        return ResponseEntity.ok(labService.editar(id, dto));
    }

    /**
     * Interdita o laboratório para manutenções preventivas, corretivas ou reparo de equipamentos.
     *
     * @param id Identificador do laboratório.
     * @return {@link ResponseEntity} sem conteúdo e código HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/manutencao")
    public ResponseEntity<Void> colocarEmManutencao(@PathVariable Long id) {
        labService.colocarEmManutencao(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Liberta o laboratório da manutenção, tornando-o novamente disponível para reservas.
     *
     * @param id Identificador do laboratório.
     * @return {@link ResponseEntity} sem conteúdo e código HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Void> liberarManutencao(@PathVariable Long id) {
        labService.liberarManutencao(id);
        return ResponseEntity.noContent().build();
    }
}