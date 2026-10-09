package com.controle.espacos.academicos.espacos_academicos.model;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade de domínio representativa de um Centro Acadêmico (CA) ou Escola Universitária.
 * <p>
 * Agrupa cursos, docentes e espaços físicos, estando subordinada a uma única IES.
 * </p>
 *
 * <b>Requisitos atendidos:</b>
 * <ul>
 *   <li>Cadastro de CA com dados de contato e coordenador.</li>
 *   <li>Vínculo obrigatório com uma única IES.</li>
 *   <li>Garantia de no máximo 1 coordenador por CA.</li>
 *   <li>Inativação lógica da escola.</li>
 *   <li>Bloqueio de exclusão física caso possua histórico.</li>
 * </ul>
 *
 */
@Entity
@Table(name = "tb_centro_academico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentroAcademico {

    /** Identificador único primário do CA. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome do Centro Acadêmico. */
    @Column(nullable = false, length = 150)
    private String nome;

    /** E-mail central de atendimento do centro. */
    @Column(nullable = false, length = 100)
    private String emailCentral;

    /** Telefone de atendimento do centro. */
    @Column(nullable = false,  length = 20)
    private String telefoneCentral;

    /** Nome do coordenador responsável pelo Centro Acadêmico. */
    @Column(nullable = false, length = 100)
    private String nomeCoordenador;

    /** Data em que o cadastro do CA foi registrado no sistema. */
    @Column(nullable = false)
    private LocalDate dataCadastramento;

    /** Situação cadastral do CA (ATIVO ou INATIVO). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status;

    /** Instituição de Ensino Superior à qual este CA está obrigatoriamente subordinado. */
    //LAZY para evitar bsucas desnecessárias no banco
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ies_id", nullable = false)
    private InstituicaoEnsinoSuperior instituicao;

    /**
     * Hook JPA para inicializar data e status padrão ativo no salvamento inicial.
     */
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
     * Inativa o cadastro do Centro Acadêmico no sistema.
     *
     * @throws IllegalStateException se o centro já estiver com o cadastro inativo.
     */
    public void inativar(){
        if (this.status == StatusCadastro.INATIVO){
            throw new IllegalStateException("O Centro Academico ja está com o cadastro inativo");
        }
        this.status = StatusCadastro.INATIVO;
    }

    /**
     * Reativa o cadastro do Centro Acadêmico no sistema.
     *
     * @throws IllegalStateException se o centro já estiver com o cadastro ativo.
     */
    public void reativar(){
        if(this.status == StatusCadastro.ATIVO){
            throw new IllegalStateException("O Centro Academico ja está com o cadastro ativo");
        }
        this.status = StatusCadastro.ATIVO;
    }
}
