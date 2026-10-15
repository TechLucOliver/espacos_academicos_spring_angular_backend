package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.dto.relatorio.EmissorRelatorioDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.relatorio.RelatorioDocenteDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.relatorio.RelatorioEspacosSolicitacoesDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.relatorio.RelatorioEstruturalDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.PerfilAcesso;
import com.controle.espacos.academicos.espacos_academicos.model.CentroAcademico;
import com.controle.espacos.academicos.espacos_academicos.model.Curso;
import com.controle.espacos.academicos.espacos_academicos.model.InstituicaoEnsinoSuperior;
import com.controle.espacos.academicos.espacos_academicos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável por agregar dados operacionais e emitir relatórios consolidados do sistema.
 */
@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final InstituicaoEnsinoSuperiorRepository iesRepository;
    private final CentroAcademicoRepository centroRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspacoAcademicoRepository espacoRepository;
    private final SolicitacaoReservaRepository reservaRepository;
    private final SolicitacaoSoftwareRepository softwareRepository;

    /**
     * Emite o relatório com a estrutura completa da IES, Centros Acadêmicos e Cursos vinculados.
     */
    @Transactional(readOnly = true)
    public RelatorioEstruturalDTO gerarRelatorioEstrutural(){
        EmissorRelatorioDTO emissor = obterEmissorAtual();

        List<InstituicaoEnsinoSuperior> todasIes = iesRepository.findAll();
        List<CentroAcademico> todosCentros = centroRepository.findAll();
        List<Curso> todosCursos = cursoRepository.findAll();

        List<RelatorioEstruturalDTO.InstituicaoItemDTO> instituicoes = todasIes.stream()
                .map(ies -> {
                        List<RelatorioEstruturalDTO.CentroItemDTO> centrosDaIes = todosCentros.stream()
                                .filter(ca -> ca.getInstituicao() != null && ca.getInstituicao().getId().equals(ies.getId()))
                                .map(ca -> {
                                    List<RelatorioEstruturalDTO.CursoItemDTO> cursosDoCentro = todosCursos.stream()
                                            .filter(c -> c.getCentroAcademico() != null && c.getCentroAcademico().getId().equals(ca.getId()))
                                            .map(c -> new RelatorioEstruturalDTO.CursoItemDTO(
                                                    c.getId(),
                                                    c.getSigla(),
                                                    c.getDescricao(),
                                                    c.getTurno() != null ? c.getTurno().toString() : "Não informado",
                                                    c.getStatus().name()
                                            )).toList();

                                    return new RelatorioEstruturalDTO.CentroItemDTO(
                                            ca.getId(),
                                            ca.getNome(),
                                            ca.getNomeCoordenador(),
                                            ca.getStatus().name(),
                                            cursosDoCentro
                                    );
                                }).toList();

                        return new RelatorioEstruturalDTO.InstituicaoItemDTO(
                                ies.getId(),
                                ies.getSigla(),
                                ies.getNome(),
                                ies.getNomeReitor(),
                                ies.getStatus().name(),
                                centrosDaIes
                        );
                }).toList();

        return new RelatorioEstruturalDTO(emissor, instituicoes);
    }

    /**
     * Emite o relatório do corpo docente com a vinculação ao respectivo Centro Acadêmico.
     */
    @Transactional(readOnly = true)
    public RelatorioDocenteDTO gerarRelatorioDocentes(){
        EmissorRelatorioDTO emissor = obterEmissorAtual();

        List<RelatorioDocenteDTO.DocenteItemDTO> docentes = usuarioRepository.findAll().stream()
                .filter(u -> u.possuiPerfil(PerfilAcesso.USUARIO) || u.possuiPerfil(PerfilAcesso.COLABORADOR))
                .map(u -> new RelatorioDocenteDTO.DocenteItemDTO(
                        u.getId(),
                        u.getMatricula(),
                        u.getNomeCompleto(),
                        u.getEmail(),
                        u.getTelefone(),
                        u.getCentroAcademico() != null ? u.getCentroAcademico().getNome() : "Sem vínculo institucional",
                        u.getStatus().name(),
                        u.getPerfis().stream().map(Enum::name).toList()
                )).toList();

        return new RelatorioDocenteDTO(emissor, docentes);
    }

    /**
     * Emite o relatório com o cadastro dos espaços e o histórico de solicitações (reservas e softwares).
     */
    @Transactional(readOnly = true)
    public RelatorioEspacosSolicitacoesDTO gerarRelatorioEspacosESolicitacoes(){
        EmissorRelatorioDTO emissor = obterEmissorAtual();

        List<RelatorioEspacosSolicitacoesDTO.EspacoItemDTO> espacos = espacoRepository.findAll().stream()
                .map(e -> new RelatorioEspacosSolicitacoesDTO.EspacoItemDTO(
                        e.getId(),
                        e.getNome(),
                        e.getSigla(),
                        e.getTipo().name(),
                        e.getCapacidadeMaxima(),
                        e.getStatus().name(),
                        e.getCentroAcademico() != null ? e.getCentroAcademico().getNome() : "Não informado"
                )).toList();

        List<RelatorioEspacosSolicitacoesDTO.ReservaItemDTO> reservas = reservaRepository.findAll().stream()
                .map(r -> new RelatorioEspacosSolicitacoesDTO.ReservaItemDTO(
                        r.getId(),
                        r.getEspaco() != null ? r.getEspaco().getNome() : "Não informado",
                        r.getSolicitante() != null ? r.getSolicitante().getNomeCompleto() : "Não informado",
                        r.getDataUtilizacao(),
                        r.getHoraInicio(),
                        r.getHoraFim(),
                        r.getStatus().name(),
                        r.getJustificativaReprovacao(),
                        r.getAvaliador() != null ? r.getAvaliador().getNomeCompleto() : null
                )).toList();

        List<RelatorioEspacosSolicitacoesDTO.SoftwareItemDTO> softwares = softwareRepository.findAll().stream()
                .map(s -> new RelatorioEspacosSolicitacoesDTO.SoftwareItemDTO(
                        s.getId(),
                        s.getSoftware() != null ? s.getSoftware().getNome() : "Não informado",
                        s.getLaboratorio() != null ? s.getLaboratorio().getNome() : "Não informado",
                        s.getSolicitante() != null ? s.getSolicitante().getNomeCompleto() : "Não informado",
                        s.getStatus().name(),
                        s.getJustificativaReprovacao(),
                        s.getAvaliador() != null ? s.getAvaliador().getNomeCompleto() : null
                )).toList();

        return new RelatorioEspacosSolicitacoesDTO(emissor, espacos, reservas, softwares);
    }

    /**
     * Extrai os dados do usuário autenticado no Spring Security para carimbar o emissor do relatório.
     */
    private EmissorRelatorioDTO obterEmissorAtual(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = (auth != null) ? auth.getName() : "SISTEMA";

        return usuarioRepository.findByEmail(login)
                .or(() -> usuarioRepository.findByMatricula(login))
                .map(u -> new EmissorRelatorioDTO(
                        u.getId(),
                        u.getNomeCompleto(),
                        u.getMatricula() != null ? u.getMatricula() : u.getEmail(),
                        u.getPerfis().toString(),
                        LocalDateTime.now()
                ))
                .orElse(new EmissorRelatorioDTO(null, "Usuario autenticado", login, "N/A", LocalDateTime.now()));
    }
}
