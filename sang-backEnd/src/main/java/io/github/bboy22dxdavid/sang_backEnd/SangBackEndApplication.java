package io.github.bboy22dxdavid.sang_backEnd;

import io.github.bboy22dxdavid.sang_backEnd.model.Avaliacao;
import io.github.bboy22dxdavid.sang_backEnd.service.AvaliacaoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SangBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(SangBackEndApplication.class, args);
	}


}
