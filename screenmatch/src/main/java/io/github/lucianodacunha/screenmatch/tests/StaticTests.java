package io.github.lucianodacunha.screenmatch.tests;

class MathUtils {
    public static int add(int a, int b) {
        return a + b;
    }
}

public class StaticTests{
    public static void main(String args[]){
        int result = MathUtils.add(5, 10);
        System.out.println(result);
    }
}