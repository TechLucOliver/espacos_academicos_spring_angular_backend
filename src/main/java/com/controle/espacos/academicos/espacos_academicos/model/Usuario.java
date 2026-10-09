package com.controle.espacos.academicos.espacos_academicos.model;

import com.controle.espacos.academicos.espacos_academicos.enums.PerfilAcesso;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade de domínio central para autenticação e gestão de usuários/docentes.
 *
 * <b>Requisitos atendidos:</b>
 * <ul>
 *   <li>Cadastro docente com dados funcionais e de contato.</li>
 *   <li>Vínculo obrigatório a um único Centro Acadêmico.</li>
 *   <li>Inativação com registro mandatório de justificativa.</li>
 *   <li>Suporte a múltiplos perfis de acesso.</li>
 * </ul>
 *
 */
@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    /** Identificador primário do usuário. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Matrícula ou registro funcional único (ex.: docente/colaborador)*/
    @Column(nullable = false, unique = true, length = 30)
    private String matricula;

    /** Nome completo do usuário. */
    @Column(nullable = false, length = 150)
    private String nomeCompleto;

    /** E-mail individual utilizado para comunicação e credenciais de login. */
    @Column(nullable = false, length = 100)
    private String email;

    /** Telefone de contato do usuário. */
    @Column(nullable = false, length = 20)
    private String telefone;

    /** Senha criptografada (hash) para autenticação segura via Spring Security (futuro). */
    @Column(nullable = false)
    private String senha;

    /** Data de cadastramento do usuário. */
    @Column(nullable = false)
    private LocalDate dataCadastramento;

    /** Situação cadastral da conta. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status;

    /** Motivo/ação de inativação registrada. */
    @Column(length = 255)
    private String motivoInativacao;

    /** Centro Acadêmico de lotação do docente. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_academico_id")
    private CentroAcademico centroAcademico;

    /** Conjunto de perfis atribuídos à conta. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_usuarios_perfis", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    @Builder.Default
    private Set<PerfilAcesso> perfis = new HashSet<>();

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
     * Inativa o cadastro do usuário exigindo justificativa obrigatória.
     *
     * @param motivoInativacao Justificativa da inativação (afastamento, licença, suspensão).
     * @throws IllegalArgumentException caso a justificativa seja nula ou vazia.
     */
    public void inativar(String motivoInativacao){
        if (motivoInativacao == null || motivoInativacao.isBlank()){
            throw  new IllegalArgumentException("A justificativa para inativação é obrigatória");
        }
        this.status = StatusCadastro.INATIVO;
        this.motivoInativacao = motivoInativacao;
    }

    /**
     * Reativa o cadastro do usuário no sistema caso ele esteja inativo.
     * <p>
     * Limpa o motivo da inativação anterior e restabelece o status ATIVO.
     * </p>
     *
     * @throws IllegalStateException se o usuário já estiver ativo no momento da operação.
     */
    public void reativar() {
        if (this.status == StatusCadastro.ATIVO) {
            throw new IllegalStateException("O usuário já está com o cadastro ativo.");
        }
        this.status = StatusCadastro.ATIVO;
        this.motivoInativacao = null;
    }

    /**
     * Verifica se o usuário detém determinado papel de segurança.
     *
     * @param perfil Perfil a ser checado.
     * @return {@code true} se o usuário detiver a permissão, ou {@code false} caso contrário.
     */
    public boolean possuiPerfil(PerfilAcesso perfil){
        return this.perfis != null && this.perfis.contains(perfil);
    }

}
