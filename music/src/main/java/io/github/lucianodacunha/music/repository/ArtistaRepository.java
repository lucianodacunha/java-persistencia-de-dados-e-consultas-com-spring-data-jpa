package io.github.lucianodacunha.music.repository;

import io.github.lucianodacunha.music.entity.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    Optional<Artista> findByNomeIgnoreCase(String nomeDoArtista);
}
