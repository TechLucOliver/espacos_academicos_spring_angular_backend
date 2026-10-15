package com.controle.espacos.academicos.espacos_academicos.config.security;

import com.controle.espacos.academicos.espacos_academicos.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * Filtro de segurança personalizado que intercepta todas as requisições HTTP recebidas pela aplicação.
 * <p>
 * Herda de {@link OncePerRequestFilter} para assegurar que a validação do cabeçalho de autorização
 * ocorra estritamente uma única vez por ciclo de solicitação do cliente.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    /**
     * Executa a lógica de extração do token, verificação criptográfica e autenticação no contexto de segurança.
     *
     * @param request Solicitação HTTP recebida.
     * @param response Resposta HTTP a ser devolvida.
     * @param filterChain Cadeia de filtros subsequentes do Spring Security.
     * @throws ServletException Caso ocorra erro na execução dos filtros do servlet.
     * @throws IOException Caso ocorra falha de I/O de rede.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenJWT = recuperarToken(request);

        if (tokenJWT != null){
            String loginUsuario = tokenService.validarToken(tokenJWT);

            if (loginUsuario != null){
                UserDetails usuario = usuarioRepository.findByEmailOrMatricula(loginUsuario).orElse(null);

                if (usuario != null){
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token bruto do cabeçalho {@code Authorization}, descartando o prefixo {@code Bearer }.
     *
     * @param request Requisição HTTP inspecionada.
     * @return O token JWT em formato texto, ou {@code null} se o cabeçalho não estiver presente ou for inválido.
     */
    private String recuperarToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            return authorizationHeader.substring(7).trim();
        }
        return null;
    }
}
