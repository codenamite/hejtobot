package com.codenamite;

import io.micronaut.runtime.Micronaut;

public class CodeBot {

    public static void main(String[] args) {
        Micronaut.build(args)
                .packages("com.codenamite")
                .start();
    }
}
