package io.github.lucianodacunha.music.controller;

import io.github.lucianodacunha.music.entity.Artista;
import io.github.lucianodacunha.music.entity.Musica;
import io.github.lucianodacunha.music.repository.ArtistaRepository;
import io.github.lucianodacunha.music.repository.MusicaRepository;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.Optional;
import java.util.Scanner;

public class CadastrarMusicas implements Command {

    private MusicaRepository musicaRepository;
    private ArtistaRepository artistaRepository;

    public CadastrarMusicas(MusicaRepository musicaRepository,
                            ArtistaRepository artistaRepository){
        this.musicaRepository = musicaRepository;
        this.artistaRepository = artistaRepository;
    }
    @Override
    public void execute() {
        Scanner input = new Scanner(System.in);
        System.out.println("Entre com o nome da música: ");
        String nomeDaMusica = input.nextLine();
        System.out.println("Entre com o nome do artista: ");
        String nomeDoArtista = input.nextLine();
        Optional<Artista> artista = artistaRepository.findByNomeIgnoreCase(nomeDoArtista);

        if (artista.isPresent()){
            Musica musica = new Musica(nomeDaMusica, artista.get());
            System.out.println("Música cadastrada com sucesso.");
            musicaRepository.save(musica);
        } else {
            System.out.println("Artista não encontrado.");
        }
    }
}
