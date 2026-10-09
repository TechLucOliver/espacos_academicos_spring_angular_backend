package com.controle.espacos.academicos.espacos_academicos.service;

import com.controle.espacos.academicos.espacos_academicos.dto.curso.CursoRequestDTO;
import com.controle.espacos.academicos.espacos_academicos.dto.curso.CursoResponseDTO;
import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.CentroAcademico;
import com.controle.espacos.academicos.espacos_academicos.model.Curso;
import com.controle.espacos.academicos.espacos_academicos.repository.CentroAcademicoRepository;
import com.controle.espacos.academicos.espacos_academicos.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviços para orquestração das regras de negócio de Cursos acadêmicos.
 * <p>
 * Centraliza validações de integridade hierárquica com o Centro Acadêmico,
 * garantia de unicidade de siglas por unidade e operações de ciclo de vida.
 * </p>
 *
 */
@Service
@RequiredArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;
    private final CentroAcademicoRepository centroAcademicoRepository;

    /**
     * Recupera todos os cursos cadastrados no sistema.
     *
     * @return Lista de DTOs contendo todos os cursos.
     */
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> listarTodos(){
        return cursoRepository.findAll()
                .stream()
                .map(CursoResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Localiza um curso específico a partir do seu identificador primário.
     *
     * @param id Identificador do curso desejado.
     * @return DTO correspondente ao curso localizado.
     * @throws RuntimeException se o curso não for localizado na base de dados.
     */
    @Transactional(readOnly = true)
    public CursoResponseDTO buscarPorId(Long id){
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o id: "+id));

        return CursoResponseDTO.fromEntity(curso);
    }

    /**
     * Lista todos os cursos vinculados a uma determinada unidade acadêmica.
     *
     * @param centroAcademicoId Identificador primário do Centro Acadêmico.
     * @return Lista contendo os cursos da referida unidade.
     */
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> listarPorCentroAcademico(Long centroAcademicoId){
        return cursoRepository.findByCentroAcademicoId(centroAcademicoId)
                .stream()
                .map(CursoResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Registra um novo curso aplicando regras de unicidade e integridade hierárquica.
     *
     * @param requestDTO Dados de entrada validados do novo curso.
     * @return DTO de resposta do curso salvo.
     * @throws RuntimeException         se o Centro Acadêmico não for localizado.
     * @throws IllegalStateException    se o Centro Acadêmico de vínculo estiver inativo.
     * @throws IllegalArgumentException se a sigla já estiver cadastrada para o mesmo centro.
     */
    @Transactional
    public CursoResponseDTO cadastrar(CursoRequestDTO requestDTO){
        CentroAcademico centroAcademico = centroAcademicoRepository.findById(requestDTO.centroAcademicoId())
                .orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado com o id: "+requestDTO.centroAcademicoId()));

        if (centroAcademico.getStatus() == StatusCadastro.INATIVO){
            throw new IllegalStateException("Não é possível cadastrar um curso em Centro Acadêmico inativo");
        }

        if (cursoRepository.existsBySiglaIgnoreCaseAndCentroAcademicoId(requestDTO.sigla(), requestDTO.centroAcademicoId())){
            throw new IllegalArgumentException("Já existe um curso cadastrado com a sigla "+requestDTO.sigla());
        }

        Curso novoCurso = Curso.builder()
                .sigla(requestDTO.sigla().toUpperCase())
                .descricao(requestDTO.descricao())
                .turno(requestDTO.turno())
                .nomeCoordenador(requestDTO.nomeCoordenador())
                .centroAcademico(centroAcademico)
                .build();

        Curso cursoSalvo = cursoRepository.save(novoCurso);
        return CursoResponseDTO.fromEntity(cursoSalvo);
    }

    /**
     * Atualiza os dados cadastrais de um curso existente.
     *
     * @param id         Identificador do curso a ser editado.
     * @param requestDTO Novos dados validados.
     * @return DTO de resposta com os dados atualizados.
     * @throws RuntimeException         se o curso ou o novo Centro Acadêmico não existirem.
     * @throws IllegalStateException    se o Centro Acadêmico informado estiver inativo.
     * @throws IllegalArgumentException se a nova sigla colidir com outro curso da mesma unidade.
     */
    @Transactional
    public CursoResponseDTO editar(Long id, CursoRequestDTO requestDTO){
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o id: "+id));

        CentroAcademico centroAcademico = centroAcademicoRepository.findById(requestDTO.centroAcademicoId())
                .orElseThrow(() -> new RuntimeException("Centro academico não econtrado com o id: "+requestDTO.centroAcademicoId()));

        if (centroAcademico.getStatus() == StatusCadastro.INATIVO){
            throw new IllegalStateException("Não é possível associar um curso a um Centro Acadêmico inativo");
        }

        if (cursoRepository.existsBySiglaIgnoreCaseAndCentroAcademicoIdAndIdNot(requestDTO.sigla(), requestDTO.centroAcademicoId(), id)){
            throw new IllegalArgumentException("Já existe outro curso com a sigla "+requestDTO.sigla()+" no Centro Academico informado");
        }

        curso.setSigla(requestDTO.sigla().toUpperCase());
        curso.setDescricao(requestDTO.descricao());
        curso.setTurno(requestDTO.turno());
        curso.setNomeCoordenador(requestDTO.nomeCoordenador());
        curso.setCentroAcademico(centroAcademico);

        Curso cursoAtualizado = cursoRepository.save(curso);
        return CursoResponseDTO.fromEntity(cursoAtualizado);
    }

    /**
     * Realiza a inativação lógica do curso (RF-015), preservando registros históricos.
     *
     * @param id Identificador do curso a ser inativado.
     * @throws RuntimeException se o curso não for localizado.
     */
    @Transactional
    public void inativar(Long id){
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o id: "+id));

        curso.inativar();
        cursoRepository.save(curso);
    }

    /**
     * Reativa o cadastro de um curso previamente inativado.
     * <p>
     * Aplica integridade hierárquica: o Centro Acadêmico mantenedor precisa estar ativo.
     * </p>
     *
     * @param id Identificador primário do curso.
     * @throws RuntimeException      se o curso não for encontrado.
     * @throws IllegalStateException se o Centro Acadêmico vinculado estiver inativo.
     */
    @Transactional
    public void reativar(Long id){
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o id: "+id));

        if(curso.getCentroAcademico().getStatus() == StatusCadastro.INATIVO){
            throw new IllegalStateException("Não é possível reativar um curso em um Centro Acadêmico inativo");
        }

        curso.reativar();
        cursoRepository.save(curso);
    }
}
