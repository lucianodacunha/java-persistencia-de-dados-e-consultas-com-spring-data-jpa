package io.github.lucianodacunha.music.main;

import io.github.lucianodacunha.music.controller.*;
import io.github.lucianodacunha.music.repository.ArtistaRepository;
import io.github.lucianodacunha.music.repository.MusicaRepository;

import java.util.Scanner;

public class Main {
    private final MusicaRepository musicaRepository;
    private Scanner input = new Scanner(System.in);
    private CommandExecutor commandExecutor;
    private ArtistaRepository artistaRepository;

    public Main(CommandExecutor  commandExecutor, ArtistaRepository artistaRepository, MusicaRepository musicaRepository) {
        this.commandExecutor = commandExecutor;
        this.artistaRepository = artistaRepository;
        this.musicaRepository = musicaRepository;
    }

    public void menu() {
        while (true) {
            System.out.println(
            """
                Escolha uma opção abaixo:
                1: Cadastrar artistas      
                2: Cadastrar músicas                        
                3: Listar artistas                        
                4: Listar músicas
                5: Buscar músicas por artistas                        
                6: Pesquisar dados sobre um artista                        
                9: Sair                                
            """);

            int opcao = Integer.parseInt(input.nextLine());

            switch (opcao){
                case 1 ->  commandExecutor.executor(new CadastrarArtistas(artistaRepository));
                case 2 ->  commandExecutor.executor(new CadastrarMusicas(musicaRepository, artistaRepository));
                case 3 ->  commandExecutor.executor(new ListarArtistas(artistaRepository));
                case 4 ->  commandExecutor.executor(new ListarMusicas(musicaRepository));
                case 5 ->  commandExecutor.executor(new BuscarMusicasPorArtistas(musicaRepository, artistaRepository));
                case 6 ->  commandExecutor.executor(new PesquisarDadosSobreUmArtista());
                case 9 ->  System.exit(0);
                default -> System.out.println("Opção inválida");
            }
        }
    }
}
