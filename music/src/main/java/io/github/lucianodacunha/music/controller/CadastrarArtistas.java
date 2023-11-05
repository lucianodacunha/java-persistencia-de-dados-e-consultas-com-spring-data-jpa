package io.github.lucianodacunha.music.controller;

import io.github.lucianodacunha.music.entity.Artista;
import io.github.lucianodacunha.music.entity.TipoArtista;
import io.github.lucianodacunha.music.repository.ArtistaRepository;

import java.util.Scanner;


public class CadastrarArtistas implements Command{

    private ArtistaRepository artistaRepository;

    public CadastrarArtistas(ArtistaRepository artistaRepository){
        this.artistaRepository = artistaRepository;
    }

    @Override
    public void execute() {
        Scanner input = new Scanner(System.in);
        System.out.println("Entre com o nome do artista: ");
        var nomeDoArtista = input.nextLine();
        System.out.println("Entre com o tipo do artista (solo, dupla, banda): ");
        var tipoDoArtista = input.nextLine();

        Artista artista = new Artista(nomeDoArtista, TipoArtista.fromStr(tipoDoArtista));
        artistaRepository.save(artista);

        System.out.println("Artista cadastrado com sucesso!\n");
    }
}
