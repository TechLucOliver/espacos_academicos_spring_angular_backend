package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.dto.usuario.InativarUsuarioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.usuario.UsuarioRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.usuario.UsuarioResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.CentroAcademico;
import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import com.controle.espacos.academicos.espacos_academicos.repository.CentroAcademicoRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.UsuarioReposity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioReposity usuarioRepository;
    private final CentroAcademicoRepository centroRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos(){
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarPorStatus(StatusCadastro status){
        return usuarioRepository.findByStatus(status)
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO requestDTO){
        if (usuarioRepository.existsByEmailIgnoreCase(requestDTO.email())){
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        if (usuarioRepository.existsByMatricula(requestDTO.matricula())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esta matrícula.");
        }

        CentroAcademico centroAcademico = centroRepository.findById(requestDTO.centroAcademicoId())
                .orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado com ID: " + requestDTO.centroAcademicoId()));

        //lembrar de fazer a criptografia da senha
        Usuario usuario = Usuario.builder()
                .matricula(requestDTO.matricula())
                .nomeCompleto(requestDTO.nomeCompleto())
                .email(requestDTO.email())
                .telefone(requestDTO.telefone())
                .senha(requestDTO.senha())
                .dataCadastramento(LocalDate.now())
                .status(StatusCadastro.ATIVO)
                .centroAcademico(centroAcademico)
                .perfis(new HashSet<>(requestDTO.perfis()))
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(usuarioSalvo);
    }

    @Transactional
    public UsuarioResponseDTO editar(Long id, UsuarioRequestDTO requestDTO){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(requestDTO.email(), id)){
            throw new IllegalArgumentException("Já existe outro usuário cadastrado com este e-mail.");
        }

        if (usuarioRepository.existsByMatriculaAndIdNot(requestDTO.matricula(), id)){
            throw new IllegalArgumentException("Já existe outro usuário cadastrado com esta matrícula.");
        }

        CentroAcademico centroAcademico = centroRepository.findById(requestDTO.centroAcademicoId())
                .orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado com ID: " + requestDTO.centroAcademicoId()));

        usuario.setMatricula(requestDTO.matricula());
        usuario.setNomeCompleto(requestDTO.nomeCompleto());
        usuario.setEmail(requestDTO.email());
        usuario.setTelefone(requestDTO.telefone());
        usuario.setCentroAcademico(centroAcademico);
        usuario.setPerfis(new HashSet<>(requestDTO.perfis()));

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(usuarioAtualizado);
    }

    @Transactional
    public void inativar(Long id, InativarUsuarioRequestDTO requestDTO){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        usuario.inativar(requestDTO.motivo());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void reativar(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        usuario.reativar();
        usuarioRepository.save(usuario);
    }
}
