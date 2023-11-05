package io.github.lucianodacunha.music.controller;

import io.github.lucianodacunha.music.entity.Musica;
import io.github.lucianodacunha.music.repository.MusicaRepository;

import java.util.List;

public class ListarMusicas implements Command {

    private MusicaRepository musicaRepository;

    public ListarMusicas(MusicaRepository musicaRepository){
        this.musicaRepository = musicaRepository;
    }

    @Override
    public void execute() {
        List<Musica> musicas = musicaRepository.findAll();

        if (!musicas.isEmpty()){
            System.out.println("Lista de Músicas: ");

            musicas.forEach(System.out::println);
        } else {
            System.out.println("Nenhuma música cadastrada.");
        }
    }
}
