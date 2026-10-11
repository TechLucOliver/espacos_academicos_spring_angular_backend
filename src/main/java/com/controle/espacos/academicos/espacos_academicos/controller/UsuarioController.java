package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.usuario.InativarUsuarioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.usuario.UsuarioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.usuario.UsuarioResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos(@RequestParam(required = false) StatusCadastro status){
        if (status != null){
            return ResponseEntity.ok(usuarioService.listarPorStatus(status));
        }

        return  ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> editar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO requestDTO){
        return ResponseEntity.ok(usuarioService.editar(id, requestDTO));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id, @Valid @RequestBody InativarUsuarioRequestDTO requestDTO){
        usuarioService.inativar(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id){
        usuarioService.reativar(id);
        return ResponseEntity.noContent().build();
    }

}
