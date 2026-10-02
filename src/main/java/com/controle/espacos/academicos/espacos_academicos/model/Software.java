package com.controle.espacos.academicos.espacos_academicos.model;

import com.controle.espacos.academicos.espacos_academicos.enums.TipoSoftware;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

/**
 * Entidade que cataloga programas e aplicações disponíveis ou solicitados para laboratórios.
 *
 * <b>Requisitos atendidos:</b>
 * <ul>
 *   <li>Detalhamento técnico de softwares em laboratórios.</li>
 *   <li>Informações para solicitação de novas instalações.</li>
 * </ul>
 */
@Entity
@Table(name = "tb_software")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Software {

    /** Identificador primário do software. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome comercial ou do pacote do software (ex.: Eclipse IDE, PostgreSQL). */
    @Column(nullable = false, length = 100)
    private String nome;

    /** URL oficial para download do instalador. */
    @Column(nullable = false, length = 255)
    private String urlDownload;

    /** Tipo de licenciamento do software (Proprietário ou Livre). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoSoftware tipo;

    /** Finalidade acadêmica do uso do programa. */
    @Column(nullable = false, length = 255)
    private String objetivoUso;

/** Disciplinas curriculares que demandam a ferramenta. */
    @Column(length = 255)
    private String disciplinasVinculadas;
}
