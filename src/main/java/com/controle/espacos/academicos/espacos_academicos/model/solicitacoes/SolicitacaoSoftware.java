package com.controle.espacos.academicos.espacos_academicos.model.solicitacoes;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusSolicitacao;
import com.controle.espacos.academicos.espacos_academicos.model.Laboratorio;
import com.controle.espacos.academicos.espacos_academicos.model.Software;
import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade de domínio para controle de pedidos de instalação de softwares em laboratórios.
 *
 * <b>Requisitos atendidos:</b>
 * <ul>
 *   <li>Solicitação de software para laboratório homologado.</li>
 *   <li>Moderação pelo colaborador.</li>
 *   <li>Justificativa obrigatória em caso de indeferimento.</li>
 * </ul>
 *
 */
@Entity
@Table(name = "tb_solicitacao_software")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitacaoSoftware {

    /** Identificador primário da solicitação. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Timestamp do envio da solicitação. */
    @Column(nullable = false)
    private LocalDateTime dataHoraSolicitacao;

    /** Status atual do pedido (PENDENTE, APROVADA, REPROVADA). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSolicitacao status;

    /** Justificativa informada em caso de recusa. */
    @Column(length = 255)
    private String justificativaReprovacao;

    /** Laboratório de informática de destino da instalação. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    /** Software solicitado para inclusão. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "software_id", nullable = false)
    private Software software;

    /** Docente solicitante. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Usuario solicitante;

    /** Colaborador responsável pela avaliação técnica. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliador_id", nullable = false)
    private Usuario avaliador;

    @PrePersist
    public void prePersist(){
        if (this.dataHoraSolicitacao == null){
            this.dataHoraSolicitacao = LocalDateTime.now();
        }
        if (this.status == null){
            this.status = StatusSolicitacao.PENDENTE;
        }
    }

    /**
     * Aprova o pedido e inclui o software na lista de instalados do laboratório.
     *
     * @param colaborador Colaborador que realizou a aprovação.
     * @throws IllegalStateException se o pedido não estiver PENDENTE.
     */
    public void aprovar(Usuario colaborador){
        if (this.status != StatusSolicitacao.PENDENTE){
            throw new IllegalStateException("Apenas pedidos pendentes podem ser aprovados");
        }
        this.status = StatusSolicitacao.APROVADA;
        this.avaliador = colaborador;
        this.laboratorio.getSoftwaresInstalados().add(this.software);
    }

    /**
     * Reprova o pedido registrando a justificativa técnica.
     *
     * @param colaborador Colaborador que indeferiu o pedido.
     * @param motivo Justificativa da recusa.
     * @throws IllegalArgumentException caso o motivo esteja em branco.
     */
    public void reprovar(Usuario colaborador, String motivo){
        if (motivo == null || motivo.isBlank()){
            throw  new IllegalArgumentException("A justificativa é obrigatória ao reprovar uma solicitação");
        }
        this.status = StatusSolicitacao.REPROVADA;
        this.justificativaReprovacao = motivo;
        this.avaliador = colaborador;
    }
}
