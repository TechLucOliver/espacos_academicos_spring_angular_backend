package com.controle.espacos.academicos.espacos_academicos.config;

import com.controle.espacos.academicos.espacos_academicos.enums.StatusCadastro;
import com.controle.espacos.academicos.espacos_academicos.model.InstituicaoEnsinoSuperior;
import com.controle.espacos.academicos.espacos_academicos.repository.InstituicaoEnsinoSuperiorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    CommandLineRunner iniciarDB(InstituicaoEnsinoSuperiorRepository iesRepositorio){
        return args -> {
          if (iesRepositorio.count() == 0){
              InstituicaoEnsinoSuperior ucsal = InstituicaoEnsinoSuperior.builder()
                      .sigla("UCSAL")
                      .nome("Universidade Católica do Salvador")
                      .emailCentral("coordenaçao@ucsal.br")
                      .telefoneCentral("71999998888")
                      .nomeReitor("Oswaldo")
                      .status(StatusCadastro.ATIVO)
                      .build();

              iesRepositorio.save(ucsal);
              System.out.println("Banco de dados iniciado com sucesso, IES "+ucsal.getSigla()+" cadastrada com sucesso");
          }
        };
    }
}
