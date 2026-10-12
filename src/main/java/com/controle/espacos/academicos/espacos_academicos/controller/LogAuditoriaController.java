package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.log.LogAuditoriaResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.service.LogAuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogAuditoriaController {

    private final LogAuditoriaService logAuditoriaService;

    @GetMapping
    public ResponseEntity<List<LogAuditoriaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(logAuditoriaService.listarTodos());
    }
}
