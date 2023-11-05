package io.github.lucianodacunha.screenmatch.tests;

import org.springframework.beans.factory.annotation.Value;


public class PropertiesTests {
    public static void main(String[] args) {

        System.out.println(new Test().getValor());
    }
}

class Test {
    @Value("${chatgpt.apikey}")
    private  String apikey;

    public  String getValor(){
        return this.apikey;
    }
}