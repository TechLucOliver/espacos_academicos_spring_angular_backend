package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.InstituicaoEnsinoSuperior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface de persistência de dados para a entidade {@link InstituicaoEnsinoSuperior}.
 * <p>
 * O Spring Data JPA implementa esta interface dinamicamente via proxies dinâmicos em tempo
 * de execução, dispensando a escrita de SQL manual para operações comuns de CRUD.
 * </p>
 */

@Repository
public interface InstituicaoEnsinoSuperiorRepository extends JpaRepository<InstituicaoEnsinoSuperior, Long> {

    /**
     * Localiza uma instituição de ensino pela sua sigla, ignorando maiúsculas e minúsculas.
     *
     * @param sigla Sigla a ser pesquisada (ex.: "ucsal" ou "UCSAL").
     * @return {@link Optional} contendo a IES encontrada, ou vazio caso não exista.
     */
    Optional<InstituicaoEnsinoSuperior> findBySiglaIgnoreCase(String sigla);

    /**
     * Recupera todas as instituições filtradas pelo estado cadastral informado.
     *
     * @param status Situação da instituição (ex.: {@link StatusCadastro#ATIVO}).
     * @return Lista de instituições que possuem o status especificado.
     */
    List<InstituicaoEnsinoSuperior> findByStatus(StatusCadastro status);

    /**
     * Verifica se já existe um registo na base de dados com a mesma sigla institucional.
     * Utilizado para validação prévia de unicidade antes de persistir um novo registo.
     *
     * @param sigla Sigla a verificar.
     * @return {@code true} se já existir na base, ou {@code false} caso contrário.
     */
    boolean existsBySiglaIgnoreCase(String sigla);
}
