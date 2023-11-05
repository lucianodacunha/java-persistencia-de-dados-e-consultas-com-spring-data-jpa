package io.github.lucianodacunha.screenmatch;

import io.github.lucianodacunha.screenmatch.main.Principal;
import io.github.lucianodacunha.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository serieRepository;

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(serieRepository);
		principal.exibeMenu();
	}
}
