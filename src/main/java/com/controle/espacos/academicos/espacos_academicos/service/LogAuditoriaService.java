package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.dto.log.LogAuditoriaResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.repository.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogAuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    @Transactional(readOnly = true)
    public List<LogAuditoriaResponseDTO> listarTodos() {
        return logAuditoriaRepository.findAll()
                .stream()
                .map(LogAuditoriaResponseDTO::fromEntity)
                .toList();
    }
}
