package com.controle.espacos.academicos.espacos_academicos.controller;

import com.controle.espacos.academicos.espacos_academicos.config.security.TokenService;
import com.controle.espacos.academicos.espacos_academicos.dto.login.LoginRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.login.LoginResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável por expor os pontos de extremidade de autenticação e emissão de tokens.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    /**
     * Autentica o usuário no sistema mediante e-mail/matrícula e senha, emitindo um token JWT em caso de sucesso.
     *
     * @param request Dados de entrada contendo a credencial de login e a senha.
     * @return {@link ResponseEntity} com status 200 (OK) e o DTO de resposta contendo o token e os perfis do usuário.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request){
        var authToken = new UsernamePasswordAuthenticationToken(request.login(), request.senha());
        Authentication authentication = authenticationManager.authenticate(authToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String tokenJWT = tokenService.gerarToken(usuario);

        LoginResponseDTO response = new LoginResponseDTO(
                tokenJWT,
                "Bearer",
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getMatricula(),
                usuario.getPerfis()
        );

        return ResponseEntity.ok(response);
    }
}
