package com.controle.espacos.academicos.espacos_academicos.model;

import com.controle.espacos.academicos.espacos_academicos.enums.PerfilAcesso;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Entidade de domínio central para autenticação e gestão de usuários/docentes.
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
public class Usuario implements UserDetails {

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
        this.motivoInativacao = null;//quando o docente é reativado ele apaga o motivo atual
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

    /**
     * Mapeia os perfis de acesso da entidade para as autoridades reconhecidas pelo Spring Security.
     * <p>
     * Cada valor do enumerador {@link PerfilAcesso} atribuído ao usuário na tabela associativa
     * é convertido para uma {@link SimpleGrantedAuthority} com o prefixo padronizado {@code ROLE_}
     * (ex.: {@code ROLE_ADMINISTRADOR}, {@code ROLE_COLABORADOR}, {@code ROLE_USUARIO}),
     * viabilizando a proteção de rotas com {@code @PreAuthorize("hasRole('...')")}.
     * </p>
     *
     * @return Coleção com as permissões e papéis de segurança atribuídos ao usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority("ROLE_"+perfil.name()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna o hash da credencial de acesso armazenada no banco relacional.
     *
     * @return String contendo o hash criptográfico da senha (BCrypt).
     */
    @Override
    public String getPassword() {
        return this.senha;
    }

    /**
     * Retorna o identificador principal padrão utilizado internamente pela sessão de segurança.
     *
     * @return O e-mail cadastrado do usuário.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Indica se a conta do usuário expirou por decurso de prazo.
     *
     * @return {@code true}, indicando que as contas cadastradas não expiram temporariamente.
     */
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    /**
     * Indica se a conta do usuário está bloqueada para autenticação no sistema.
     * <p>
     * Cumpre o requisito de inativação docente: caso o status cadastral seja diferente
     * de {@link StatusCadastro#ATIVO}, o acesso é imediatamente negado pelo framework.
     * </p>
     *
     * @return {@code true} se o cadastro estiver ativo; {@code false} caso esteja inativo ou suspenso.
     */
    @Override
    public boolean isAccountNonLocked(){
        return this.status == StatusCadastro.ATIVO;
    }

    /**
     * Indica se a credencial de segurança (senha) está expirada e demanda renovação forçada.
     *
     * @return {@code true}, indicando que a credencial permanece válida.
     */
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    /**
     * Indica se o usuário está habilitado operacionalmente para autenticar-se.
     *
     * @return {@code true} se o status for {@link StatusCadastro#ATIVO}.
     */
    @Override
    public boolean isEnabled(){
        return this.status == StatusCadastro.ATIVO;
    }

}
