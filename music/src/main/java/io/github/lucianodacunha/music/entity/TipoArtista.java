package io.github.lucianodacunha.music.entity;

public enum TipoArtista {
    SOLO("solo"),
    DUPLA("dupla"),
    BANDA("banda");

    private String tipo;

    TipoArtista(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo(){
        return this.tipo;
    }

    public static TipoArtista fromStr(String str){
        for(TipoArtista tipo : TipoArtista.values()){
            if (tipo.getTipo().equalsIgnoreCase(str)){
                return tipo;
            }
        }
        throw new RuntimeException("nenhum tipo encontrado para a string informada " + str);
    }
}
