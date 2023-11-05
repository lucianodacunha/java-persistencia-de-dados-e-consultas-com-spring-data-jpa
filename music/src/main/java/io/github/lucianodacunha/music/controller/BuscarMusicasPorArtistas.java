package io.github.lucianodacunha.music.controller;

import io.github.lucianodacunha.music.entity.Artista;
import io.github.lucianodacunha.music.entity.Musica;
import io.github.lucianodacunha.music.repository.ArtistaRepository;
import io.github.lucianodacunha.music.repository.MusicaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BuscarMusicasPorArtistas implements Command {

    private final MusicaRepository musicaRepository;
    private final ArtistaRepository artistaRepository;

    public BuscarMusicasPorArtistas(MusicaRepository musicaRepository, ArtistaRepository artistaRepository){
        this.musicaRepository = musicaRepository;
        this.artistaRepository = artistaRepository;
    }

    @Override
    public void execute() {
        Scanner input = new Scanner(System.in);
        System.out.println("Entre com o nome do artista: ");
        var nomeDoArtista = input.nextLine();
        Optional<Artista> artista = artistaRepository.findByNomeIgnoreCase(nomeDoArtista);

        if (artista.isPresent()){
            List<Musica> musicas = musicaRepository.findMusicaByArtista(artista.get());

            if (!musicas.isEmpty()){
                System.out.println("Músicas do " + artista.get().getNome());
                musicas.forEach(System.out::println);
            } else {
                System.out.println("Nenhuma música cadastrada.");
            }
        } else {
            System.out.println("Artista não encontrado");
        }
    }
}
