package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.espaco_academico.EspacoAcademicoRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.espaco_academico.EspacoAcademicoResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.service.EspacoAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pela exposição dos pontos de extremidade (endpoints)
 * de gestão de Espaços Académicos convencionais (salas de aula, auditórios).
 *
 */
@RestController
@RequestMapping("/api/espacos-academicos")
@RequiredArgsConstructor
public class EspacoAcademicoController {
    private final EspacoAcademicoService espacoService;

    /**
     * Regista um novo espaço académico no sistema.
     *
     * @param dto Objeto de transferência com os dados validados do novo espaço.
     * @return {@link ResponseEntity} contendo o DTO do espaço criado e o código HTTP 201 (Created).
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<EspacoAcademicoResponseDTO> cadastrar(@Valid @RequestBody EspacoAcademicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(espacoService.cadastrar(dto));
    }

    /**
     * Recupera os detalhes de um espaço académico através do seu identificador único.
     *
     * @param id Identificador primário do espaço pretendido.
     * @return {@link ResponseEntity} contendo os dados do espaço localizado e o código HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<EspacoAcademicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(espacoService.buscarPorId(id));
    }

    /**
     * Lista todos os espaços académicos registados no campus universitário.
     *
     * @return {@link ResponseEntity} com a lista de todos os espaços e o código HTTP 200 (OK).
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<List<EspacoAcademicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(espacoService.listarTodos());
    }

    /**
     * Recupera todos os espaços físicos adstritos a um determinado Centro Académico.
     *
     * @param centroAcademicoId Identificador do Centro Académico mantenedor.
     * @return {@link ResponseEntity} com a lista de espaços filtrados e o código HTTP 200 (OK).
     */
    @GetMapping("/centro-academico/{centroAcademicoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<List<EspacoAcademicoResponseDTO>> listarPorCentroAcademico(@PathVariable Long centroAcademicoId) {
        return ResponseEntity.ok(espacoService.listarPorCentroAcademico(centroAcademicoId));
    }

    /**
     * Atualiza integralmente os dados cadastrais de um espaço académico existente.
     *
     * @param id  Identificador do espaço físico a atualizar.
     * @param dto Novos dados submetidos no corpo da requisição.
     * @return {@link ResponseEntity} contendo os dados atualizados e o código HTTP 200 (OK).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<EspacoAcademicoResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody EspacoAcademicoRequestDTO dto) {
        return ResponseEntity.ok(espacoService.editar(id, dto));
    }

    /**
     * Interdita o espaço académico, alterando o seu estado operacional para manutenção.
     *
     * @param id Identificador do espaço a interditar.
     * @return {@link ResponseEntity} sem corpo com código HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/manutencao")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<Void> colocarEmManutencao(@PathVariable Long id) {
        espacoService.colocarEmManutencao(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Liberta o espaço académico do estado de manutenção, restabelecendo a sua disponibilidade.
     *
     * @param id Identificador do espaço a desinterditar.
     * @return {@link ResponseEntity} sem corpo com código HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/liberar")
    @PreAuthorize("hasAnyRole('COLABORADOR', 'ADMINISTRADOR')")
    public ResponseEntity<Void> liberarManutencao(@PathVariable Long id) {
        espacoService.liberarManutencao(id);
        return ResponseEntity.noContent().build();
    }
}
