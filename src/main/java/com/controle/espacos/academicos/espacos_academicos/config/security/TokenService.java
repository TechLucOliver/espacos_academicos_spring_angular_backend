package com.controle.espacos.academicos.espacos_academicos.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço responsável pela emissão, assinatura criptográfica e validação de tokens JWT (JSON Web Token).
 * <p>
 * Centraliza a geração de credenciais de acesso temporárias utilizadas nas requisições da API REST,
 * garantindo a integridade dos dados e a identidade do usuário através do algoritmo HMAC256.
 * </p>
 */
@Service
public class TokenService {

    /**
     * Chave secreta utilizada na assinatura e decodificação do token, injetada a partir
     * das propriedades da aplicação ou mantida em valor padrão para o ambiente de desenvolvimento.
     */
    @Value("${api.security.token.secret:segredo-padrao-espacos-academicos-2026}")
    private String secret;

    /**
     * Emissor oficial dos tokens da aplicação.
     */
    private static final String ISSUER = "espacos-academicos-api";

    /**
     * Emite um novo token JWT compactado com claims informativas do usuário autenticado.
     *
     * @param usuario Instância da entidade do usuário cujos dados e papéis serão encapsulados no token.
     * @return String contendo o token JWT assinado digitalmente.
     * @throws RuntimeException Caso ocorra falha interna de configuração do algoritmo ou na assinatura.
     */
    public String gerarToken(Usuario usuario){
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withClaim("nome", usuario.getNomeCompleto())
                    .withClaim("matricula", usuario.getMatricula())
                    .withClaim("perfis", usuario.getPerfis().stream().map(Enum::name).toList())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algoritmo);
        }catch (JWTCreationException e){
            throw new RuntimeException("Erro ao gerar token JWT de autenticação.", e);
        }
    }

    /**
     * Valida a assinatura digital e o tempo de expiração do token JWT recebido no cabeçalho HTTP.
     *
     * @param tokenJWT String contendo o token JWT enviado pelo cliente.
     * @return O identificador principal (e-mail) associado ao token caso este seja válido; ou {@code null} se estiver corrompido ou expirado.
     */
    public String validarToken(String tokenJWT){
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }catch (JWTVerificationException e){
            return null;
        }
    }

    /**
     * Calcula o instante exato de expiração do token de segurança.
     * <p>
     * O tempo de validade é de 4 horas a partir do momento da emissão, considerando
     * o fuso horário oficial de Brasília/Salvador (UTC-3).
     * </p>
     *
     * @return {@link Instant} indicando a data e hora de expiração.
     */
    private Instant gerarDataExpiracao(){
        return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-03:00"));
    }
}
