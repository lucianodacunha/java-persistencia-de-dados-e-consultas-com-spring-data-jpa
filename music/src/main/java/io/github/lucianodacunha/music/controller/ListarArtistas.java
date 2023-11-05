package io.github.lucianodacunha.music.controller;

import io.github.lucianodacunha.music.entity.Artista;
import io.github.lucianodacunha.music.repository.ArtistaRepository;

import java.util.List;

public class ListarArtistas implements Command {
    private final ArtistaRepository artistaRepository;

    public ListarArtistas(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    @Override
    public void execute() {
        System.out.println("Lista de artistas:");
        List<Artista> artistas = artistaRepository.findAll();

        if (!artistas.isEmpty()){
            artistas.forEach(System.out::println);
        }
    }
}
