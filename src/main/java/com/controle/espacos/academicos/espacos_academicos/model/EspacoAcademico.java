package com.controle.espacos.academicos.espacos_academicos.model;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusEspaco;
import com.controle.espacos.academicos.espacos_academicos.enums.TipoEspaco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Superclasse de domínio que modela os espaços acadêmicos compartilhados.
 * <p>
 * Utiliza herança {@link InheritanceType#JOINED} para comportar especializações como {@link Laboratorio}.
 * </p>
 *
 * <b>Requisitos atendidos:</b>
 * <ul>
 *   <li>Cadastro de espaços com tipo e capacidade.</li>
 *   <li>Vínculo mandatório a um único Centro Acadêmico.</li>
 *   <li>Definição automática inicial para DISPONIVEL.</li>
 *   <li>Inativação por manutenção sem exclusão com histórico.</li>
 * </ul>
 *
 */
@Entity
@Table(name = "tb_espaco_academico")
@Inheritance(strategy = InheritanceType.JOINED) //separando as tabelas em pais e filhas
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EspacoAcademico {

    /** Identificador primário do espaço físico. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Sigla ou código da sala/espaço (ex.: LAB-01, AUD-B). */
    @Column(nullable = false, length = 20)
    private String sigla;

    /** Nome por extenso do espaço. */
    @Column(nullable = false, length = 100)
    private String nome;

    /** Classificação da estrutura (SALA, LABORATORIO, AUDITORIO). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEspaco tipo;

    /** Observações sobre recursos do espaço (projetor, ar-condicionado, etc.). */
    @Column(length = 255)
    private String descricaoComplementar;

    /** Lotação máxima permitida no espaço em número de pessoas. */
    @Column(nullable = false)
    private int capacidadeMaxima;

    /** Bloco, prédio ou andar da localização física. */
    @Column(nullable = false, length = 150)
    private String localizacao;

    /** Data de cadastramento do espaço. */
    @Column(nullable = false)
    private LocalDate dataCadastro;

    /** Estado de disponibilidade operacional do espaço. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEspaco status;

    /** Centro Acadêmico responsável pelo espaço. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_academico_id", nullable = false)
    private CentroAcademico centroAcademico;

    @PrePersist
    public void prePersist(){
        if (this.dataCadastro == null){
            this.dataCadastro = LocalDate.now();
        }
        if (this.status == null){
            this.status = StatusEspaco.DISPONIVEL;
        }
    }

    /** Inativa o espaço para manutenção. */
    public void inativar(){
        this.status = StatusEspaco.INATIVO;
    }

    /** Alterna o status para INDISPONIVEL ao aprovar uma reserva. */
    public void marcarIndisponivel(){
        this.status = StatusEspaco.INDISPONIVEL;
    }

    /** Libera o espaço tornando-o DISPONIVEL para novas reservas. */
    public void marcarDisponivel(){
        this.status = StatusEspaco.DISPONIVEL;
    }
}
