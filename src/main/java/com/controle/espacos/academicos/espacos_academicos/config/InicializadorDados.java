package com.controle.espacos.academicos.espacos_academicos.config;

import com.controle.espacos.academicos.espacos_academicos.enums.*;
import com.controle.espacos.academicos.espacos_academicos.model.*;
import com.controle.espacos.academicos.espacos_academicos.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Componente de configuração encarregado de semear a base de dados em memória (H2)
 * com dados iniciais de teste assim que o servidor sobe.
 *
 */
@Configuration
public class InicializadorDados {

    /**
     * Inicializa registros base na aplicação caso a base esteja vazia.
     *
     * @param iesRepositorio Repositório para persistir os registos de demonstração.
     * @return Instância de {@link CommandLineRunner} executada no bootstrap do Spring.
     */
    @Bean
    CommandLineRunner iniciarDB(InstituicaoEnsinoSuperiorRepository iesRepositorio, CentroAcademicoRepository centroAcademicoRepository,
                                CursoRepository cursoRepository, EspacoAcademicoRepository espacoRepository, LaboratorioRepository laboratorioRepository,
                                SoftwareRepository softwareRepository){
        return args -> {
          if (iesRepositorio.count() == 0){
              InstituicaoEnsinoSuperior ucsal = InstituicaoEnsinoSuperior.builder()
                      .sigla("UCSAL")
                      .nome("Universidade Católica do Salvador")
                      .emailCentral("coordenaçao@ucsal.edu.br")
                      .telefoneCentral("71999998888")
                      .nomeReitor("Oswaldo")
                      .status(StatusCadastro.ATIVO)
                      .build();

              iesRepositorio.save(ucsal);
              System.out.println("Banco de dados iniciado com sucesso, IES "+ucsal.getSigla()+" cadastrada com sucesso");

              CentroAcademico ccet = CentroAcademico.builder()
                      .nome("Escola de Exatas e Tecnologia")
                      .emailCentral("ccet@ucsal.edu.br")
                      .telefoneCentral("71977775555")
                      .nomeCoordenador("Prof. Asdrubal")
                      .instituicao(ucsal)
                      .status(StatusCadastro.ATIVO)
                      .build();

              CentroAcademico ccetSalvo = centroAcademicoRepository.save(ccet);
              System.out.println("IES e Centro Academico devidamente cadastrados e lincados");

              Curso bes = Curso.builder()
                      .sigla("BES")
                      .descricao("Bacharelado em Engenharia de Software")
                      .turno(Turno.MATUTINO)
                      .nomeCoordenador("Prof. Oswaldo")
                      .centroAcademico(ccetSalvo)
                      .status(StatusCadastro.ATIVO)
                      .build();

              cursoRepository.save(bes);
              System.out.println("Curso " + bes.getSigla() + " cadastrado e vinculado ao " + ccetSalvo.getNome());

              EspacoAcademico sala222 = EspacoAcademico.builder()
                      .sigla("SALA-222")
                      .nome("Sala 222 - Bloco B")
                      .tipo(TipoEspaco.SALA)
                      .descricaoComplementar("Equipada com projetor multimídia")
                      .capacidadeMaxima(45)
                      .localizacao("Pavilhão de Aulas, 4º Andar")
                      .status(StatusEspaco.DISPONIVEL)
                      .centroAcademico(ccetSalvo)
                      .build();

              espacoRepository.save(sala222);
              System.out.println("Espaço academico cadastrado no centro "+ccet.getNome());

              Laboratorio lab01 = Laboratorio.builder()
                      .sigla("LAB-01")
                      .nome("Laboratório de Informática 01")
                      .tipo(TipoEspaco.LABORATORIO)
                      .descricaoComplementar("Equipado com projetor e ar condicionado")
                      .capacidadeMaxima(40)
                      .localizacao("Pavilhão Tecnológico, Térreo")
                      .status(StatusEspaco.DISPONIVEL)
                      .tipoComputadores("Core i7, 16GB RAM, SSD 512GB")
                      .centroAcademico(ccetSalvo)
                      .build();

              laboratorioRepository.save(lab01);
              System.out.println("Laboratorio cadastrado com sucesso no centro "+lab01.getCentroAcademico());

              Software vscode = Software.builder()
                      .nome("Visual Studio Code")
                      .urlDownload("https://code.visualstudio.com/Download")
                      .tipo(TipoSoftware.SOFTWARE_LIVRE)
                      .objetivoUso("Ambiente de desenvolvimento de código-fonte multiplataforma")
                      .disciplinasVinculadas("Algoritmos, Programação Web, Engenharia de Software")
                      .build();

              Software postgre = Software.builder()
                      .nome("PostgreSQL 16")
                      .urlDownload("https://www.postgresql.org/download/")
                      .tipo(TipoSoftware.SOFTWARE_LIVRE)
                      .objetivoUso("Sistema de gerenciamento de banco de dados relacional")
                      .disciplinasVinculadas("Banco de Dados I, Banco de Dados II")
                      .build();

              softwareRepository.saveAll(List.of(vscode, postgre));

              lab01.getSoftwaresInstalados().add(vscode);
              lab01.getSoftwaresInstalados().add(postgre);
              laboratorioRepository.save(lab01);

              ucsal.adicionarProReitoria(ProReitoria.builder()
                      .nome("Pró-Reitoria de Graduação")
                      .nomeResponsavel("Prof. Dr. João Silva")
                      .instituicao(ucsal)
                      .build());

              ucsal.adicionarProReitoria(ProReitoria.builder()
                      .nome("Pró-Reitoria de Pesquisa e Pós-Graduação")
                      .nomeResponsavel("Prof. Dra. Maria Oliveira")
                      .instituicao(ucsal)
                      .build());

                InstituicaoEnsinoSuperior ucsalAtualizada = iesRepositorio.save(ucsal);
          }
        };
    }
}
