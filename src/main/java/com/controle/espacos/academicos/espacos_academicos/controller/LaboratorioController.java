package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.laboratorio.LaboratorioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.laboratorio.LaboratorioResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.software.SoftwareResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
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
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
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
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
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
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
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
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
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
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
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
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<Void> liberarManutencao(@PathVariable Long id) {
        labService.liberarManutencao(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista os softwares homologados e instalados no laboratório.
     *
     * @param id Identificador do laboratório.
     * @return Lista de softwares instalados com código HTTP 200 (OK).
     */
    @GetMapping("/{id}/softwares")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<List<SoftwareResponseDTO>> listarSoftwares(@PathVariable Long id) {
        return ResponseEntity.ok(labService.listarSoftwaresDoLaboratorio(id));
    }

    /**
     * Vincula/instala um software às máquinas do laboratório.
     *
     * @param id         Identificador do laboratório.
     * @param softwareId Identificador do software a instalar.
     * @return Resposta sem corpo e código HTTP 204 (No Content).
     */
    @PostMapping("/{id}/softwares/{softwareId}")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<Void> instalarSoftware(@PathVariable Long id, @PathVariable Long softwareId) {
        labService.instalarSoftware(id, softwareId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove/desinstala um software das máquinas do laboratório.
     *
     * @param id         Identificador do laboratório.
     * @param softwareId Identificador do software a desinstalar.
     * @return Resposta sem corpo e código HTTP 204 (No Content).
     */
    @DeleteMapping("/{id}/softwares/{softwareId}")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<Void> desinstalarSoftware(@PathVariable Long id, @PathVariable Long softwareId) {
        labService.desinstalarSoftware(id, softwareId);
        return ResponseEntity.noContent().build();
    }
}