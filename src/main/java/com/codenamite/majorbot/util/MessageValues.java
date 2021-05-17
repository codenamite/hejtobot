package com.codenamite.majorbot.util;

import java.util.Arrays;
import java.util.List;

public enum MessageValues {

    NITRO(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

    public final List<String> values;

    MessageValues(List<String> values) {
        this.values = values;
    }
}