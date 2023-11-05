package io.github.lucianodacunha.screenmatch.model.entity;

public enum Categoria {
    ACAO("Action", "ação"),
    ROMANCE("Romance", "romance"),
    COMEDIA("Comedy", "comédia"),
    DRAMA("Drama", "drama"),
    CRIME("Crime", "crime");

    private String categoriaBR;
    private String categoriaOMDB;

    Categoria(String categoriaOMDB, String categoriaBR) {
        this.categoriaBR = categoriaOMDB;
        this.categoriaOMDB = categoriaOMDB;
    }

    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaOMDB.equalsIgnoreCase(text)){
                return categoria;
            }
        }

        throw new RuntimeException("nenhuma categoria encontrada para a string informada " + text);
    }

    public static Categoria fromCategoriaBR(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaBR.equalsIgnoreCase(text)){
                return categoria;
            }
        }

        throw new RuntimeException("nenhuma categoria encontrada para a string informada " + text);
    }
}
