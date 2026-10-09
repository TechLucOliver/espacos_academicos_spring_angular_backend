package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.CentroAcademicoRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.CentroAcademicoResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.service.CentroAcademicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centros-academicos")
public class CentroAcademicoController {
    private final CentroAcademicoService centroAcademicoService;

    /**
     * Construtor para injeção do serviço de domínio de Centros Acadêmicos.
     *
     * @param centroAcademicoService Serviço contendo regras de negócio de Centros Acadêmicos.
     */
    public CentroAcademicoController(CentroAcademicoService centroAcademicoService) {
        this.centroAcademicoService = centroAcademicoService;
    }

    /**
     * Endpoint para realização de novo cadastro de Centro Acadêmico.
     *
     * @param requestDTO Dados de entrada validados do novo centro.
     * @return Centro Acadêmico criado e código HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<CentroAcademicoResponseDTO> cadastrar(@Valid @RequestBody CentroAcademicoRequestDTO requestDTO){
        CentroAcademicoResponseDTO responseDTO = centroAcademicoService.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Endpoint para listagem de todos os Centros Acadêmicos cadastrados.
     *
     * @return Lista de Centros Acadêmicos e código HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<CentroAcademicoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(centroAcademicoService.listarTodos());
    }

    /**
     * Endpoint para consultar Centros Acadêmicos subordinados a uma IES específica.
     *
     * @param iesId Identificador primário da IES mantenedora.
     * @return Lista de Centros Acadêmicos filtrados por IES e código HTTP 200 (OK).
     */
    @GetMapping("/ies/{iesId}")
    public ResponseEntity<List<CentroAcademicoResponseDTO>> listarPorInstituicao(@PathVariable Long iesId){
        return ResponseEntity.ok(centroAcademicoService.listarPorInstituicao(iesId));
    }

    /**
     * Endpoint para busca de um Centro Acadêmico específico por seu ID.
     *
     * @param id Identificador primário do centro.
     * @return Dados do Centro Acadêmico e código HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<CentroAcademicoResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(centroAcademicoService.buscarPorId(id));
    }

    /**
     * Endpoint para inativação lógica do cadastro de um Centro Acadêmico (RF-010).
     *
     * @param id Identificador numérico do centro a ser inativado.
     * @return Resposta sem corpo e código HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        centroAcademicoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para reativação do cadastro de um Centro Acadêmico.
     *
     * @param id Identificador primário do centro a ser reativado.
     * @return Resposta sem corpo com código HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        centroAcademicoService.reativar(id);
        return ResponseEntity.noContent().build();
    }

}
