package io.github.lucianodacunha.screenmatch.tests;

    public class EnumTests {
        public static void main(String[] args) {
            for (Cor cor : Cor.values()){
                System.out.println(
                        "ENUM: %s, VALOR: %s".formatted(cor, cor.getValor())
                );
            }
        }
    }

    enum Cor {
        AMARELO("yellow"),
        VERDE("green"),
        RED("vermelho"),
        BLACK("preto");

        private final String valor;

        Cor(String cor) {
            this.valor = cor;
        }

        public String getValor(){
            return this.valor;
        }
    }