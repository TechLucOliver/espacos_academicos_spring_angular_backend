package com.controle.espacos.academicos.espacos_academicos.model;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade de domínio que representa uma Instituição de Ensino Superior (IES).
 * <p>
 * Mapeada para a tabela {@code tb_ies}, atua como a entidade raiz da hierarquia
 * académica, agregando os respetivos Centros Académicos (CAs).
 * </p>
 *
 * <b>Requisitos atendidos:</b>
 * <ul>
 *   <li>RF-001: Cadastro de IES com dados centrais, reitor e status.</li>
 *   <li>RF-002: Cada IES possui no máximo 1 reitor vinculado.</li>
 *   <li>RF-004: Inativação lógica da instituição.</li>
 *   <li>RF-005: Preservação de dados impedindo exclusão física com histórico.</li>
 * </ul>
 */

@Entity
@Table(name = "tb_ies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstituicaoEnsinoSuperior {
    /**
     * Identificador primário único gerado automaticamente pelo motor da base de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Sigla oficial da instituição (ex.: UCSAL, PUC-Rio).
     */
    @Column(nullable = false, length = 20)
    private String sigla;

    /**
     * Nome por extenso da instituição de ensino.
     */
    @Column(nullable = false, length = 150)
    private String nome;

    /**
     * Correio eletrónico central de contacto institucional.
     */
    @Column(nullable = false, length = 100)
    private String emailCentral;

    /**
     * Número de telefone principal da central da IES.
     */
    @Column(nullable = false, length = 20)
    private String telefoneCentral;

    /**
     * Identificação do reitor responsável pela instituição (limite de 1 por IES).
     */
    @Column(nullable = false, length = 100)
    private String nomeReitor;

    /**
     * Data em que o registo institucional foi inserido no sistema.
     */
    @Column(nullable = false)
    private LocalDate dataCadastramento;

    /**
     * Situação cadastral atual da IES (ATIVO ou INATIVO).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status;

    /**
     * Gatilho de ciclo de vida JPA disparado imediatamente antes da execução do {@code INSERT}.
     * Garante o preenchimento automático da data de criação e do status inicial ativo,
     * prevenindo valores nulos na persistência.
     */
    @PrePersist
    public void prePersist(){
        if(this.dataCadastramento == null){
            this.dataCadastramento = LocalDate.now();
        }
        if (this.status == null){
            this.status = StatusCadastro.ATIVO;
        }
    }

    /**
     * Realiza a inativação lógica da IES no sistema.
     */
    public void inativar(){
        this.status = StatusCadastro.INATIVO;
    }
}
