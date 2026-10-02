package com.controle.espacos.academicos.espacos_academicos.model;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.enums.Turno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade de domínio que representa um curso acadêmico ofertado pela instituição.
 *
 * <b>Requisitos atendidos:</b>
 * <ul>
 *   <li>Cadastro de curso com sigla, turnos e coordenador.</li>
 *   <li>Vínculo obrigatório com um único Centro Acadêmico.</li>
 *   <li>Inativação lógica quando o curso não for mais ofertado.</li>
 *   <li>Bloqueio de remoção física quando possuir histórico.</li>
 * </ul>
 *
 */
@Entity
@Table(name = "tb_curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    /** Identificador primário do curso. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Sigla identificadora do curso (ex.: BES, ADS) */
    @Column(nullable = false, length = 20)
    private String sigla;

    /** Descrição ou nome do curso (ex.: Bacharelado em Engenharia de Software)*/
    @Column(nullable = false, length = 200)
    private String descricao;

    /** Turno de oferta do curso. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Turno turno;

    /** Nome do docente coordenador do curso. */
    @Column(nullable = false, length = 100)
    private String nomeCoordenador;

    /** Data em que o curso foi cadastrado. */
    @Column(nullable = false)
    private LocalDate dataCadastramento;

    /** Status do cadastro do curso. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status;

    /** Centro Acadêmico responsável pelo curso. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_academico_id", nullable = false)
    private CentroAcademico centroAcademico;

    @PrePersist
    public void prePersist(){
        if (this.dataCadastramento == null){
            this.dataCadastramento = LocalDate.now();
        }
        if (this.status == null){
            this.status = StatusCadastro.ATIVO;
        }
    }

    /**
     * Inativa logicamente o curso.
     */
    public void inativar(){
        this.status = StatusCadastro.INATIVO;
    }
}
