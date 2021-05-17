package com.codenamite.majorbot.request.weather;

import com.codenamite.majorbot.model.weather.WeatherInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Objects;

@Singleton
public class WeatherApiClient {

    @Value("${api.weather.key}")
    private String apiKey;

    public String fetchWeatherInfo(String city) {
        ObjectMapper mapper = new ObjectMapper();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?units=metric&appid=" + apiKey + "&q=" + city)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WeatherInfo weatherInfo = null;
        try {
            assert response != null;
            weatherInfo = mapper.readValue(Objects.
                    requireNonNull(response.body())
                    .byteStream(), WeatherInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createWeatherString(weatherInfo);
    }

    private String createWeatherString(WeatherInfo weatherInfo) {
        if (weatherInfo.getCod() == 404) {
            return "Nie znaleziono miasta!";
        } else {
            return new StringBuilder()
                    .append("Aktualna pogoda dla miasta ")
                    .append(weatherInfo.getName())
                    .append(":\n")
                    .append("Temperatura: ").append(weatherInfo.getMain().getTemp()).append(" \u2103")
                    .append("\n")
                    .append("Temperatura odczuwalna: ").append(weatherInfo.getMain().getFeelsLike()).append(" \u2103")
                    .toString();
        }
    }

}
