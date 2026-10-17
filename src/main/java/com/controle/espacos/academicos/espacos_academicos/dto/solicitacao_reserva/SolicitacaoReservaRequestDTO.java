package com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_reserva;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Payload de entrada para submissão de uma nova solicitação de reserva de espaço.
 *
 * @param dataUtilizacao Data pretendida para uso do espaço (deve ser hoje ou futura).
 * @param horaInicio Horário inicial da reserva.
 * @param horaFim Horário de término previsto.
 * @param espacoId Identificador primário do espaço acadêmico solicitado.
 * @param solicitanteId Identificador primário do docente solicitante.
 */

public record SolicitacaoReservaRequestDTO(
        @NotNull(message = "A data de utilizaçõa é obrigatória.")
        @FutureOrPresent(message = "A data de utilização deve ser hoje ou uma data futura.")
        LocalDate dataUtilizacao,

        @NotNull(message = "O horário de início é obrigatório.")
        LocalTime horaInicio,

        @NotNull(message = "O horário final é obrigatório.")
        LocalTime horaFim,

        @NotNull(message = "O ID do espaço acadêmico é obrigatório.")
        Long espacoId,

        @NotNull(message = "O ID do solicitante é obrigatório.")
        Long solicitanteId


) {}
