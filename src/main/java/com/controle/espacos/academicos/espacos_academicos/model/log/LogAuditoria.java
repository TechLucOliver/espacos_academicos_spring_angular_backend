package com.controle.espacos.academicos.espacos_academicos.model.log;

import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade de registro histórico e auditoria do sistema.
 * <p>
 * Garante a rastreabilidade das operações de modificação.
 * </p>
 */
@Entity
@Table(name = "tb_log_auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogAuditoria {

    /** Identificador único do registro de auditoria. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Momento exato em que a ação foi disparada. */
    @Column(nullable = false)
    private LocalDateTime dataHora;

    /** Operação executada (ex: INATIVACAO_DOCENTE, CRIACAO_RESERVA, APROVACAO_SOFTWARE). */
    @Column(nullable = false, length = 100)
    private String acao;

    /** Nome da entidade de domínio afetada (ex: Usuario, SolicitacaoReserva). */
    @Column(nullable = false, length = 100)
    private String entidade;

    /**
     * Descrição detalhada do evento ou snapshot dos dados (armazena payloads extensos via TEXT).
     */
    @Column(columnDefinition = "TEXT")
    private String detalhes;

    /** Usuário responsável pela execução da ação. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @PrePersist
    public void prePersist(){
        if (this.dataHora == null){
            this.dataHora = LocalDateTime.now();
        }
    }
}
