package com.controle.espacos.academicos.espacos_academicos.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Classe principal de configuração do Spring Security e controle de políticas de rede (CORS).
 * <p>
 * Define a política de sessão stateless, registra os filtros de segurança personalizados,
 * estabelece as regras de liberação de rotas públicas e habilita o controle de acesso
 * declarativo nos métodos através da anotação {@link EnableMethodSecurity}.
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    /**
     * Define a cadeia de filtros de segurança (Security Filter Chain) aplicada a todas as rotas da API.
     *
     * @param http Construtor de segurança HTTP do Spring.
     * @return Instância configurada de {@link SecurityFilterChain}.
     * @throws Exception Caso ocorra erro de inicialização das diretivas de segurança.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        //nosso endpoint
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configura as políticas de compartilhamento de recursos entre origens distintas (CORS).
     * <p>
     * Atende ao item de restrição arquitetural do edital, limitando o consumo dos recursos
     * estritamente à aplicação Front-End desenvolvida em Angular em execução na porta 4200.
     * </p>
     *
     * @return Fonte de configuração de CORS baseada em URLs.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); //origem do angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Expõe o gerenciador padrão de autenticação do Spring Security como um bean gerenciado.
     *
     * @param configuration Objeto de configuração global de autenticação.
     * @return O {@link AuthenticationManager} pronto para validar credenciais.
     * @throws Exception Caso o gerenciador não possa ser instanciado.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    /**
     * Define o algoritmo de codificação e validação criptográfica de senhas do sistema.
     * <p>
     * Utiliza o padrão da indústria {@link BCryptPasswordEncoder}, que implementa funções
     * de dispersão seguras com geração automática e embutida de salt.
     * </p>
     *
     * @return Instância do codificador BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
