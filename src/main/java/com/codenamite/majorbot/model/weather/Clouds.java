package com.codenamite.majorbot.model.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Clouds {
    private int all;

    public Clouds(int all) {
        this.all = all;
    }

    public Clouds() {}

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
