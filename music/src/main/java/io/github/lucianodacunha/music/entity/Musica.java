package io.github.lucianodacunha.music.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "musicas")
public class Musica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String titulo;
    @NonNull
    @ManyToOne
    private Artista artista;

    @Override
    public String toString(){
        return "Música: %s, Artista: %s".formatted(
                this.titulo, this.artista.getNome()
        );
    }
}