package com.codenamite.majorbot.util;

import java.util.List;
import java.util.Random;

public class RandomListElement {

    public static String randomize(List<String> values) {
        Random rand = new Random();
        return values.get(rand.nextInt(values.size()));
    }
}