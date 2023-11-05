package io.github.lucianodacunha.screenmatch.tests;

public class EnvVarsTests {
    public static void main(String[] args) {
        System.getenv().forEach((k, v) -> {
            System.out.println("k: %s, v: %s".formatted(k, v));
        });
    }
}
