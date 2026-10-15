package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por carregar os dados de identidade do usuário durante o handshake de autenticação.
 */
@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailOrMatricula(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não localizado com a credencial informada: " + login));
    }
}
