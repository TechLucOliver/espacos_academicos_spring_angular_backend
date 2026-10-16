package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.config.logs.Auditavel;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_reserva.ReprovarSolicitacaoDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_reserva.SolicitacaoReservaRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.solicitacao_reserva.SolicitacaoReservaResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.PerfilAcesso;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.model.EspacoAcademico;
import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import com.controle.espacos.academicos.espacos_academicos.model.solicitacoes.SolicitacaoReserva;
import com.controle.espacos.academicos.espacos_academicos.repository.EspacoAcademicoRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.SolicitacaoReservaRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoReservaService {

    private final SolicitacaoReservaRepository reservaRepository;
    private final EspacoAcademicoRepository espacoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<SolicitacaoReservaResponseDTO> listarTodas() {
        return reservaRepository.findAll()
                .stream()
                .map(SolicitacaoReservaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoReservaResponseDTO buscarPorId(Long id) {
        SolicitacaoReserva solicitacao = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de reserva não encontrada com o ID: " + id));
        return SolicitacaoReservaResponseDTO.fromEntity(solicitacao);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoReservaResponseDTO> listarPorSolicitante(Long solicitanteId) {
        return reservaRepository.findBySolicitanteId(solicitanteId)
                .stream()
                .map(SolicitacaoReservaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoReservaResponseDTO> listarPorStatus(StatusSolicitacao status) {
        return reservaRepository.findByStatus(status)
                .stream()
                .map(SolicitacaoReservaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    @Auditavel(acao = "CADASTRAR_RESERVA")
    public SolicitacaoReservaResponseDTO solicitar(SolicitacaoReservaRequestDTO dto) {
        if (!dto.horaFim().isAfter(dto.horaInicio())) {
            throw new IllegalArgumentException("O horário de término deve ser posterior ao horário de início.");
        }

        if (dto.dataUtilizacao().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("A data de reserva não pode ser retroativa");
        }

        EspacoAcademico espaco = espacoRepository.findById(dto.espacoId())
                .orElseThrow(() -> new RuntimeException("Espaço acadêmico não encontrado com ID: " + dto.espacoId()));

        Usuario solicitante = usuarioRepository.findById(dto.solicitanteId())
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado com ID: " + dto.solicitanteId()));

        boolean conflito = reservaRepository.existeConflitoHorario(
                dto.espacoId(),
                dto.dataUtilizacao(),
                dto.horaInicio(),
                dto.horaFim()
        );

        if (conflito) {
            throw new IllegalStateException("Já existe uma reserva aprovada para este espaço no mesmo dia e intervalo de horário.");
        }

        SolicitacaoReserva reserva = SolicitacaoReserva.builder()
                .dataSolicitacao(LocalDateTime.now())
                .dataUtilizacao(dto.dataUtilizacao())
                .horaInicio(dto.horaInicio())
                .horaFim(dto.horaFim())
                .status(StatusSolicitacao.PENDENTE)
                .espaco(espaco)
                .solicitante(solicitante)
                .build();

        SolicitacaoReserva salva = reservaRepository.save(reserva);
        return SolicitacaoReservaResponseDTO.fromEntity(salva);
    }

    @Transactional
    @Auditavel(acao = "APROVAR_RESERVA")
    public SolicitacaoReservaResponseDTO aprovar(Long id, Long avaliadorId) {
        SolicitacaoReserva solicitacao = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de reserva não encontrada com o ID: " + id));

        Usuario avaliador = usuarioRepository.findById(avaliadorId)
                .orElseThrow(() -> new RuntimeException("Usuário avaliador não encontrado com ID: " + avaliadorId));

        boolean temPermissao = avaliador.possuiPerfil(PerfilAcesso.ADMINISTRADOR) || avaliador.possuiPerfil(PerfilAcesso.COLABORADOR);

        if (!temPermissao){
            throw new IllegalArgumentException("O utilizador não possui permissão para aprovar reservas");
        }

        boolean isAdmin = avaliador.getPerfis().contains(PerfilAcesso.ADMINISTRADOR);

        if (solicitacao.getSolicitante().getId().equals(avaliador.getId()) && !isAdmin){
            throw new IllegalArgumentException("O solicitante não pode avaliar seu próprio pedido de reserva");
        }

        EspacoAcademico espaco = solicitacao.getEspaco();
        if (espaco.getStatus() != StatusEspaco.DISPONIVEL){
            throw new IllegalStateException("O espaço acadêmico não está disponível para receber reservas (Status atual: " + espaco.getStatus() + ")");
        }

        boolean conflito = reservaRepository.existeConflitoHorario(
            espaco.getId(),
            solicitacao.getDataUtilizacao(),
            solicitacao.getHoraInicio(),
            solicitacao.getHoraFim()
        );

        if (conflito){
            throw new IllegalStateException("Não é possível aprovar, já existe outra reserva aprovada para este mesmo espaço e horário");
        }

        solicitacao.aprovar(avaliador);

        espaco.setStatus(StatusEspaco.INDISPONIVEL);
        espacoRepository.save(espaco);

        SolicitacaoReserva salva = reservaRepository.save(solicitacao);
        return SolicitacaoReservaResponseDTO.fromEntity(salva);
    }

    @Transactional
    @Auditavel(acao = "REPROVAR_RESERVA")
    public SolicitacaoReservaResponseDTO reprovar(Long id, ReprovarSolicitacaoDTO dto) {
        SolicitacaoReserva solicitacao = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de reserva não encontrada com o ID: " + id));

        Usuario avaliador = usuarioRepository.findById(dto.avaliadorId())
                .orElseThrow(() -> new RuntimeException("Usuário avaliador não encontrado com ID: " + dto.avaliadorId()));

        boolean isAdmin = avaliador.getPerfis().contains(PerfilAcesso.ADMINISTRADOR);

        if (solicitacao.getSolicitante().getId().equals(avaliador.getId()) && !isAdmin){
            throw new IllegalArgumentException("O solicitante não pode avaliar seu próprio pedido de reserva");
        }

        solicitacao.reprovar(avaliador, dto.motivo());
        SolicitacaoReserva salva = reservaRepository.save(solicitacao);
        return SolicitacaoReservaResponseDTO.fromEntity(salva);
    }

    @Transactional
    @Auditavel(acao = "FINALIZAR_RESERVA")
    public SolicitacaoReservaResponseDTO finalizarUso(Long id) {
        SolicitacaoReserva solicitacao = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação de reserva não encontrada com o ID: " + id));

        solicitacao.finalizarUso();

        EspacoAcademico espaco = solicitacao.getEspaco();
        espaco.setStatus(StatusEspaco.DISPONIVEL);
        espacoRepository.save(espaco);

        SolicitacaoReserva salva = reservaRepository.save(solicitacao);
        return SolicitacaoReservaResponseDTO.fromEntity(salva);
    }
}
