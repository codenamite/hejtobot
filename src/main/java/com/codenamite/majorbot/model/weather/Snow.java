package com.codenamite.majorbot.model.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Snow {

    @JsonProperty("1h")
    private int oneHour;
    @JsonProperty("3h")
    private int threeHours;

    public Snow() {
    }

    public Snow(int oneHour, int threeHours) {
        this.oneHour = oneHour;
        this.threeHours = threeHours;
    }

    public int getOneHour() {
        return oneHour;
    }

    public void setOneHour(int oneHour) {
        this.oneHour = oneHour;
    }

    public int getThreeHours() {
        return threeHours;
    }

    public void setThreeHours(int threeHours) {
        this.threeHours = threeHours;
    }
}
