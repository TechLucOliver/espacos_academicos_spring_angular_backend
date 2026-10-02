package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.InstituicaoEnsinoSuperiorRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.InstituicaoEnsinoSuperiorResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.service.InstituicaoEnsinoSuperiorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controlador REST que expõe os endpoints HTTP para operações sobre Instituições de Ensino Superior.
 * <p>
 * Ponto de entrada das requisições disparadas pela interface Front-End em Angular.
 * Mapeado sob o caminho base {@code /api/ies}.
 * </p>
 *
 */
@RestController
@RequestMapping("/api/ies")
public class InstituicaoEnsinoSuperiorController {

    private final InstituicaoEnsinoSuperiorService iesService;

    /**
     * Construtor para injeção do serviço de domínio da IES.
     *
     * @param iesService Serviço contendo as regras de negócio de IES.
     */
    public InstituicaoEnsinoSuperiorController(InstituicaoEnsinoSuperiorService iesService){
        this.iesService = iesService;
    }

    /**
     * Endpoint para registar uma nova instituição de ensino.
     *
     * @param requestDTO Objeto contendo os dados validados da nova instituição.
     * @return {@link ResponseEntity} com a instituição cadastrada e status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<InstituicaoEnsinoSuperiorResponseDTO> cadastrar(@Valid @RequestBody InstituicaoEnsinoSuperiorRequestDTO requestDTO){
        InstituicaoEnsinoSuperiorResponseDTO responseCriada = iesService.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseCriada);
    }

    /**
     * Endpoint para listar todas as instituições de ensino superior.
     *
     * @return {@link ResponseEntity} contendo a lista de IES e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<InstituicaoEnsinoSuperiorResponseDTO>> listarTodas(){
        return ResponseEntity.ok(iesService.listarTodas());
    }

    /**
     * Endpoint para consultar uma IES específica a partir do seu ID.
     *
     * @param id Identificador numérico da IES informado na URL.
     * @return {@link ResponseEntity} com os dados da instituição e status HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoEnsinoSuperiorResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(iesService.buscarPorId(id));
    }

    /**
     * Endpoint para realizar a inativação lógica de uma IES.
     *
     * @param id Identificador da instituição que deve ser inativada.
     * @return {@link ResponseEntity} sem corpo com status HTTP 204 (No Content).
     */
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id){
        iesService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
