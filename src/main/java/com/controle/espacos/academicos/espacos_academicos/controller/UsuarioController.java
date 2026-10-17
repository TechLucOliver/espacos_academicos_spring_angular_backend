package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.usuario.InativarUsuarioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.usuario.UsuarioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.usuario.UsuarioResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.usuario.UsuarioUpdateRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pela gestão e ciclo de vida cadastral dos usuários.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    /**
     * Cadastra um novo usuário no sistema.
     * Operação restrita a administradores.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(requestDTO));
    }

    /**
     * Localiza os dados de um usuário pelo identificador primário.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    /**
     * Lista todos os usuários cadastrados na base de dados.
     * Operação restrita ao perfil administrativo.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR')")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos(@RequestParam(required = false) StatusCadastro status){
        if (status != null){
            return ResponseEntity.ok(usuarioService.listarPorStatus(status));
        }

        return  ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * Atualiza as informações cadastrais de um usuário.
     * Operação restrita a administradores.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequestDTO requestDTO){
        return ResponseEntity.ok(usuarioService.editar(id, requestDTO));
    }

    /**
     * Inativa logicamente a conta de um usuário mediante justificativa obrigatória.
     * Impede logins futuros no Spring Security sem quebrar o histórico de auditoria e reservas.
     */
    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> inativar(@PathVariable Long id, @Valid @RequestBody InativarUsuarioRequestDTO requestDTO){
        usuarioService.inativar(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reativa o cadastro de um usuário previamente inativado.
     * Operação restrita a administradores.
     */
    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> reativar(@PathVariable Long id){
        usuarioService.reativar(id);
        return ResponseEntity.noContent().build();
    }

}
