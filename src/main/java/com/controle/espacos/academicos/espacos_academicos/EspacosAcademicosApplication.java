package com.controle.espacos.academicos.espacos_academicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EspacosAcademicosApplication {

	static void main(String[] args) {
		SpringApplication.run(EspacosAcademicosApplication.class, args);
		//db url = jdbc:h2:mem:espacosdb
	}

}
