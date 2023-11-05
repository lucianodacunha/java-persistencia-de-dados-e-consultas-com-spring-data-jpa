package io.github.lucianodacunha.music.repository;

import io.github.lucianodacunha.music.entity.Artista;
import io.github.lucianodacunha.music.entity.Musica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicaRepository extends JpaRepository<Musica, Long> {
    List<Musica> findMusicaByArtista(Artista artista);
}
