package com.codenamite.majorbot.model.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coord {

    private BigDecimal lon;
    private BigDecimal lat;

    public Coord(BigDecimal lon, BigDecimal lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public Coord() {}

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }
}
