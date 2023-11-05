package io.github.lucianodacunha.music.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "artistas")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String nome;
    @Enumerated(EnumType.STRING)
    @NonNull
    private TipoArtista tipo;
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL)
    private List<Musica> musicas = new ArrayList<>();

    @Override
    public String toString(){
        return "Artista: %s, Tipo: %s".formatted(
                this.getNome(), this.tipo
        );
    }
}
