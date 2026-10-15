package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.dto.relatorio.RelatorioDocenteDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.relatorio.RelatorioEspacosSolicitacoesDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.relatorio.RelatorioEstruturalDTO;
import com.controle.espacos.academicos.espacos_academicos.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST dedicado à emissão dos relatórios institucionais e operacionais.
 */
@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    /**
     * Relatório consolidado da estrutura acadêmica (IES -> Centros -> Cursos).
     * Restrito a Administradores.
     */
    @GetMapping("/estrutural")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RelatorioEstruturalDTO> emitirRelatorioEstrutural(){
        return ResponseEntity.ok(relatorioService.gerarRelatorioEstrutural());
    }

    /**
     * Relatório do cadastro de docentes e vinculações a Centros Acadêmicos.
     * Restrito a Administradores.
     */
    @GetMapping("/docentes")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RelatorioDocenteDTO> emitirRelatorioDocentes() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioDocentes());
    }

    /**
     * Relatório com o estado dos espaços e histórico de aprovações/reprovações.
     * Acessível a Administradores, Colaboradores e Usuários (Docentes).
     */
    @GetMapping("/espacos-solicitacoes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR', 'USUARIO')")
    public ResponseEntity<RelatorioEspacosSolicitacoesDTO> emitirRelatorioEspacosESolicitacoes() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioEspacosESolicitacoes());
    }
}
