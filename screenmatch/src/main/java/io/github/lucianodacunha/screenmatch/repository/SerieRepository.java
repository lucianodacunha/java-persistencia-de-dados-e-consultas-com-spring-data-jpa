package io.github.lucianodacunha.screenmatch.repository;

import io.github.lucianodacunha.screenmatch.model.entity.Categoria;
import io.github.lucianodacunha.screenmatch.model.entity.Episodio;
import io.github.lucianodacunha.screenmatch.model.entity.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeDoTitulo);

    List<Serie> findByAtoresContainingIgnoreCase(String nomeDoAtor);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    @Query("""
        SELECT s FROM Serie s WHERE s.totalTemporadas >= :temporadas AND s.avaliacao >= :avaliacao
        """)
    List<Serie> seriesPorTemporadaEAValiacao(Integer temporadas, Double avaliacao);

    @Query("""
        SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoDoTitulo%
        """)
    List<Episodio> buscarEpisodioPorTrecho(String trechoDoTitulo);

    @Query("""
        SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento
    """)
    List<Episodio> buscarEpisodioPorAnoDeLancamento(Serie serie, Integer anoLancamento);
}
