package com.controle.espacos.academicos.espacos_academicos.repository;

import com.controle.espacos.academicos.espacos_academicos.model.log.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de acesso a dados e persistência para a entidade {@link LogAuditoria}.
 */
@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
}