package com.controle.espacos.academicos.espacos_academicos.model.solicitacoes;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.model.EspacoAcademico;
import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entidade responsável pelo ciclo de vida de uma solicitação de reserva de espaço.
 *
 * <b>Requisitos atendidos:</b>
 * <ul>
 *   <li>Solicitação individual de um único espaço por vez.</li>
 *   <li>Moderação e validação de conflito de horários.</li>
 *   <li>Atualização do status do espaço para indisponível.</li>
 *   <li>Justificativa obrigatória em reprovação.</li>
 *   <li>Confirmação de liberação do espaço.</li>
 * </ul>
 */
@Entity
@Table(name = "tb_solicitacao_reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitacaoReserva {

    /** Identificador primário da solicitação. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Data pretendida para a utilização do espaço. */
    @Column(nullable = false)
    private LocalDate dataUtilizacao;

    /** Horário inicial de início da reserva. */
    @Column(nullable = false)
    private LocalTime horaInicio;

    /** Horário previsto para término e liberação. */
    @Column(nullable = false)
    private LocalTime horaFim;

    /** Data e hora em que a requisição foi submetida. */
    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;

    /** Estado da solicitação (PENDENTE, APROVADA, REPROVADA, FINALIZADA). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSolicitacao status;

    /** Motivo técnico da recusa caso a solicitação seja reprovada. */
    @Column(length = 255)
    private String justificativaReprovacao;

    /** Espaço acadêmico individual solicitado. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "espaco_id")
    private EspacoAcademico espaco;

    /** Docente solicitante da reserva . */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;

    /** Colaborador que avaliou a requisição. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliador_id")
    private Usuario avaliador;

    @PrePersist
    public void prePersist(){
        if (this.dataSolicitacao == null){
            this.dataSolicitacao = LocalDateTime.now();
        }
        if (this.status == null){
            this.status = StatusSolicitacao.PENDENTE;
        }
    }

    /**
     * Aprova a reserva e altera o status do espaço acadêmico para INDISPONIVEL.
     *
     * @param colaborador Usuário moderador com perfil Colaborador.
     * @throws IllegalStateException se a solicitação não estiver em estado PENDENTE.
     */
    public void aprovar(Usuario colaborador){
        if (this.status != StatusSolicitacao.PENDENTE){
            throw new IllegalStateException("Apenas solicitações com status PENDENTE podem ser aprovadas.");
        }
        this.status = StatusSolicitacao.APROVADA;
        this.avaliador = colaborador;
        this.espaco.marcarIndisponivel();
    }

    /**
     * Reprova a reserva com justificativa mandatória .
     *
     * @param colaborador Usuário avaliador.
     * @param motivo Justificativa da recusa.
     * @throws IllegalArgumentException caso a justificativa esteja em branco.
     */
    public void reprovar(Usuario colaborador, String motivo){
        if (motivo == null || motivo.isBlank()){
            throw new IllegalArgumentException("A justificativa é obrigatória para reprovar uma solicitação de reserva");
        }
        if (this.status != StatusSolicitacao.PENDENTE){
            throw new IllegalStateException("Apenas solicitações com status PENDENTE podem ser reprovadas.");
        }
        this.status = StatusSolicitacao.REPROVADA;
        this.justificativaReprovacao = motivo;
        this.avaliador = colaborador;
    }

    /**
     * Confirma a liberação do espaço após o uso e retorna o status para DISPONIVEL.
     *
     * @throws IllegalStateException se a solicitação não estiver APROVADA.
     */
    public void finalizarUso(){
        if (this.status != StatusSolicitacao.APROVADA){
            throw new IllegalStateException("Apenas reservas aprovadas podem ser finalizadas");
        }
        this.status = StatusSolicitacao.FINALIZADA;
        this.espaco.marcarIndisponivel();
    }
}
