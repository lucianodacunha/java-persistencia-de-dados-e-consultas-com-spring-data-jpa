package io.github.lucianodacunha.music;

import io.github.lucianodacunha.music.controller.CadastrarArtistas;
import io.github.lucianodacunha.music.controller.CommandExecutor;
import io.github.lucianodacunha.music.main.Main;
import io.github.lucianodacunha.music.repository.ArtistaRepository;
import io.github.lucianodacunha.music.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class MusicApplication implements CommandLineRunner {

    @Autowired
    private CommandExecutor commandExecutor;
    @Autowired
    private ArtistaRepository artistaRepository;
    @Autowired
    private MusicaRepository musicaRepository;

    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Locale.setDefault(Locale.US);
        Main main = new Main(commandExecutor, artistaRepository, musicaRepository);
        main.menu();
    }
}
