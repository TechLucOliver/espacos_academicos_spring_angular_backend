package com.controle.espacos.academicos.espacos_academicos.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Especialização de {@link EspacoAcademico} voltada a laboratórios de informática.
 * <p>
 * Agrega especificidades de infraestrutura computacional e catálogo de softwares instalados.
 * </p>
 */
@Entity
@Table(name = "tb_laboratorio")
@PrimaryKeyJoinColumn(name = "espaco_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Laboratorio extends EspacoAcademico{

    /** Especificações de hardware dos computadores (ex: i7 16GB RAM SSD 512GB). */
    @Column(nullable = false, length = 150)
    private String tipoComputadores;

    /** Relação N:N dos softwares homologados e instalados nas máquinas do laboratório. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_laboratorio_software",
            joinColumns = @JoinColumn(name = "laboratorio_id"),
            inverseJoinColumns = @JoinColumn(name = "software_id")
    )
    @Builder.Default
    private Set<Software> softwaresInstalados = new HashSet<>();
}
