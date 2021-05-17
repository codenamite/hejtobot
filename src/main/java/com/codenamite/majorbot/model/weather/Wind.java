package com.codenamite.majorbot.model.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {

    private BigDecimal speed;
    private int deg;
    private int gust;

    public Wind(BigDecimal speed, int deg, int gust) {
        this.speed = speed;
        this.deg = deg;
        this.gust = gust;
    }

    public Wind() {
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getGust() {
        return gust;
    }

    public void setGust(int gust) {
        this.gust = gust;
    }

}
