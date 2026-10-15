package com.controle.espacos.academicos.espacos_academicos.dto.relatorio;

import java.time.LocalDateTime;

/**
 * Metadados de identificação do usuário responsável pela emissão do relatório e carimbo de data/hora.
 */
public record EmissorRelatorioDTO(
        Long id,
        String nome,
        String identificacao,
        String perfil,
        LocalDateTime dataHoraEmissao
) {
}
