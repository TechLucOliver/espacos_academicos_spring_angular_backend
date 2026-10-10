package com.controle.espacos.academicos.espacos_academicos.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa uma Pró-Reitoria vinculada a uma Instituição de Ensino Superior (IES).
 * <p>
 * Atende ao requisito de identificar os órgãos pró-reitoriais da universidade
 * e as suas respectivas nomeações de responsáveis.
 * </p>
 *
 */
@Entity
@Table(name = "tb_pro_reitoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProReitoria {

    /** Identificador primário da pró-reitoria. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome do órgão pró-reitorial (ex: Pró-Reitoria de Graduação - PROGRAD). */
    @Column(nullable = false, length = 150)
    private String nome;

    /** Nome e titulação da autoridade responsável nomeada para o órgão. */
    @Column(nullable = false, length = 100)
    private String nomeResponsavel;

    /** Instituição de Ensino Superior mantenedora da pró-reitoria. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ies_id", nullable = false)
    private InstituicaoEnsinoSuperior instituicao;
}
