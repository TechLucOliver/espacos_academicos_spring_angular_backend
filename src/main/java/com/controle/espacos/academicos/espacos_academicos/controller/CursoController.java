package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.curso.CursoRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.curso.CursoResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints HTTP para gestão de Cursos acadêmicos.
 * <p>
 * Mapeado sob o caminho base {@code /api/cursos}.
 * </p>
 *
 * @author Equipe de Desenvolvimento
 * @version 1.0
 */
@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    private final CursoService cursoService;

    /**
     * Construtor para injeção do serviço de domínio de Cursos.
     *
     * @param cursoService Serviço contendo as regras de negócio de Cursos.
     */
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    /**
     * Endpoint para listagem de todos os cursos cadastrados.
     *
     * @return Lista de cursos e código HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(cursoService.listarTodos());
    }

    /**
     * Endpoint para consultar um curso por seu ID.
     *
     * @param id Identificador primário do curso.
     * @return Dados do curso e código HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(cursoService.buscarPorId(id));
    }

    /**
     * Endpoint para consultar cursos subordinados a um Centro Acadêmico específico.
     *
     * @param centroAcademicoId Identificador primário do Centro Acadêmico.
     * @return Lista de cursos filtrados e código HTTP 200 (OK).
     */
    @GetMapping("/centro-academico/{centroAcademicoId}")
    public ResponseEntity<List<CursoResponseDTO>> listarPorCentroAcademico(@PathVariable Long centroAcademicoId){
        return ResponseEntity.ok(cursoService.listarPorCentroAcademico(centroAcademicoId));
    }

    /**
     * Endpoint para cadastrar um novo curso (RF-012).
     *
     * @param requestDTO Dados de entrada validados do novo curso.
     * @return Curso criado e código HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<CursoResponseDTO> cadastrar(@Valid @RequestBody CursoRequestDTO requestDTO){
        CursoResponseDTO responseDTO = cursoService.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Endpoint para atualização cadastral integral de um curso existente.
     *
     * @param id         Identificador primário do curso na URL.
     * @param requestDTO Novos dados validados.
     * @return Curso atualizado e código HTTP 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody CursoRequestDTO requestDTO){
        CursoResponseDTO cursoAtualizado = cursoService.editar(id, requestDTO);
        return ResponseEntity.ok(cursoAtualizado);
    }

    /**
     * Endpoint para inativação lógica do cadastro de um curso (RF-015).
     *
     * @param id Identificador primário do curso a ser inativado.
     * @return Resposta sem corpo e código HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<CursoResponseDTO> inativar(@PathVariable Long id){
        cursoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para reativação do cadastro de um curso.
     *
     * @param id Identificador primário do curso a ser reativado.
     * @return Resposta sem corpo e código HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<CursoResponseDTO> reativar(@PathVariable Long id){
        cursoService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
