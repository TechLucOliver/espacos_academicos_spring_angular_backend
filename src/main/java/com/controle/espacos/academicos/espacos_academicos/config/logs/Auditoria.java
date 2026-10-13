package com.controle.espacos.academicos.espacos_academicos.config.logs;

import com.controle.espacos.academicos.espacos_academicos.model.Usuario;
import com.controle.espacos.academicos.espacos_academicos.model.log.LogAuditoria;
import com.controle.espacos.academicos.espacos_academicos.repository.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@RequiredArgsConstructor
public class Auditoria {

    private final LogAuditoriaRepository repository;

    @AfterReturning(pointcut = "@annotation(auditavel)", returning = "resultado")
    public void registrarLog(JoinPoint joinPoint, Auditavel auditavel, Object resultado) {

        Class<?> classe = joinPoint.getTarget().getClass();
        String nome = classe.getSimpleName();
        String entidade = nome.replace("Service", "");


        Usuario usuario = Usuario.builder()
                .id(1L)
                .nomeCompleto("Usuário de Teste")
                .matricula("TESTE-999")
                .build();

        LocalDateTime dataHora = LocalDateTime.now();

        LogAuditoria log = LogAuditoria.builder()
                .dataHora(dataHora)
                .acao(auditavel.acao())
                .entidade(entidade)
                .usuario(usuario)
                .build();

        repository.save(log);
        arquivo(dataHora, auditavel.acao(), entidade, usuario);
    }

    private void arquivo(LocalDateTime dataHora, String acao, String entidade, Usuario usuario) {
        try {
            Path caminho = Paths.get("LOGS_DO_SISTEMA.txt");

            String nomeUsuario = usuario.getNomeCompleto();
            String matriculaUsuario = usuario.getMatricula();
            String dataFormatada = dataHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String linhaLog = String.format("[%s] AÇÃO: %s | ENTIDADE: %s | USUÁRIO: %s (Matrícula: %s)%n",
                    dataFormatada, acao, entidade, nomeUsuario, matriculaUsuario);

            Files.writeString(
                    caminho,
                    linhaLog,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            System.err.println("Erro ao escrever o log no arquivo TXT: " + e.getMessage());
        }
    }
}